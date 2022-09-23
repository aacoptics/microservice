package com.aacoptics.feishu.photo.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aacoptics.feishu.photo.config.FeishuAppKeyConfig;
import com.aacoptics.feishu.photo.entity.po.FeishuUser;
import com.aacoptics.feishu.photo.exception.BusinessException;
import com.aacoptics.feishu.photo.mapper.FeishuUserMapper;
import com.aacoptics.feishu.photo.provider.FeishuApiProvider;
import com.aacoptics.feishu.photo.provider.SapJcoProvider;
import com.aacoptics.feishu.photo.service.FeishuService;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FeishuServiceImpl implements FeishuService {

    @Resource
    private FeishuApiProvider feishuApiProvider;

    @Resource
    private FeishuAppKeyConfig feishuAppKeyConfig;

    @Resource
    private FeishuUserMapper feishuUserMapper;


    @Resource
    SapJcoProvider sapJcoProvider;


    @Override
    public FeishuUser getFeishuUser(String employeeNo) {
        return feishuUserMapper.selectOne(new LambdaQueryWrapper<FeishuUser>().eq(FeishuUser::getEmployeeNo, employeeNo));
    }

    @Override
    public String fetchAccessToken() {
        final JSONObject result = feishuApiProvider.fetchAccessToken(feishuAppKeyConfig);
        final String throwable = result.get("Throwable", String.class);
        if (StrUtil.isNotEmpty(throwable))
            throw new BusinessException(throwable);

        return result.get("code", Integer.class) == 0
                ? StrUtil.format("Bearer {}", result.get("tenant_access_token"))
                : null;
    }

    @Override
    public String fetchUploadFileKey(String accessToken, String fileName, byte[] fileContent) throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        String photoPath = tempDir + "/" + fileName;
        OutputStream sos = Files.newOutputStream(Paths.get(photoPath));
        sos.write(fileContent, 0, fileContent.length);
        sos.flush();
        sos.close();
        File file = new File(photoPath);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            FileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
            IOUtils.copy(fileInputStream, fileItem.getOutputStream());
            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
            final JSONObject result = feishuApiProvider.fetchUploadFileKey(accessToken, fileName, multipartFile);
            final String throwable = result.get("Throwable", String.class);
            if (StrUtil.isNotEmpty(throwable))
                throw new BusinessException(throwable);
            return result.get("code", Integer.class) == 0
                    ? result.get("data", JSONObject.class).get("file", JSONObject.class).get("file_id", String.class)
                    : null;
        }
    }

    @Override
    public boolean updateUserPhoto(String employeeNo, byte[] fileContent) throws IOException {
        final String accessToken = fetchAccessToken();
        if (StrUtil.isEmpty(accessToken)) throw new BusinessException("获取AccessToken失败，请检查！");

        FeishuUser feishuUser = getFeishuUser(employeeNo);
        if (ObjectUtil.isNull(feishuUser))
            throw new BusinessException("飞书用户不存在，请检查！");

        final String faceKey = fetchUploadFileKey(accessToken, employeeNo + ".jpg", fileContent);
        if(StrUtil.isBlank(faceKey))
            throw new BusinessException("上传照片失败，请检查！");

        JSONObject jsonObject = JSONUtil.createObj()
                .set("user_id", feishuUser.getUserId())
                .set("face_key", faceKey);

        JSONObject jsonBody = JSONUtil.createObj().set("user_setting", jsonObject);

        final JSONObject result = feishuApiProvider.updateUserPhoto(accessToken, FeishuService.EMPLOYEE_TYPE_USER_ID, jsonBody);
        final String throwable = result.get("Throwable", String.class);
        if (StrUtil.isNotEmpty(throwable))
            throw new BusinessException(throwable);
        return result.get("code", Integer.class) == 0;
    }

    @Override
    public void uploadUserPhotoFromSAP(int idFlag) {
        try {
            //从SAP获取照片
            JSONArray employeeInfoList = sapJcoProvider.getEmployeeInfoList(idFlag);

            List<String> failEmployeeNo = new ArrayList<>();

            for (int i = 0; i < employeeInfoList.size(); i++) {
                com.alibaba.fastjson.JSONObject employeeJsonObject = employeeInfoList.getJSONObject(i);
                String employeeNo = employeeJsonObject.getString("PERNR");

                String base64Photo = employeeJsonObject.getString("PHOTO");
                if(StringUtils.isEmpty(base64Photo))
                {
                    log.warn("工号【" + employeeNo + "】照片不存在");
                    continue;
                }
                // 对字节数组字符串进行Base64解码并生成图片
                BASE64Decoder decoder = new BASE64Decoder();
                // Base64解码
                byte[] employeePhoto = decoder.decodeBuffer(base64Photo);
                for (int j = 0; j < employeePhoto.length; ++j) {
                    if (employeePhoto[j] < 0) {// 调整异常数据
                        employeePhoto[j] += 256;
                    }
                }
                Boolean result = null;
                try {
                    result = this.updateUserPhoto(employeeNo, employeePhoto);
                } catch (Exception e)
                {
                    failEmployeeNo.add(employeeNo);
                    log.error(i + " 工号【" + employeePhoto + "】, exception:", e);
                    continue;
                }
                log.info(i + "工号【" + employeeNo + "】, result:" + result);
            }

            log.info("上传异常的员工，共" + failEmployeeNo.size());
            for(int k=0; k<failEmployeeNo.size(); k++) {
                log.info(failEmployeeNo.get(k));
            }
        } catch (Exception e) {
            log.error("上传照片到飞书异常", e);
        }
    }
}
