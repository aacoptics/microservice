package com.aacoptics.feishu.photo.scheduler;

import com.aacoptics.feishu.photo.service.FeishuService;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class FeiShuPhotoHandle {

    @Resource
    FeishuService feishuService;

    @XxlJob("feishuPhotoHandle")
    public void feishuPhotoHandle() {
      /*  1：全量：取所有人员信息
        2：增量：有变更的人员数据
        3：增量：单个同步
        4：全量zhrdc026-中间表获取
        5：全量-无照片*/
        Integer idFlag = 4;

        String param = XxlJobHelper.getJobParam(); //执行参数
        JSONObject jobParam = JSONObject.parseObject(param);
        try {
            if(jobParam != null && jobParam.containsKey("idFlag")) {
                idFlag = jobParam.getInteger("idFlag");
            }
            feishuService.uploadUserPhotoFromSAP(idFlag);
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        }
    }
}