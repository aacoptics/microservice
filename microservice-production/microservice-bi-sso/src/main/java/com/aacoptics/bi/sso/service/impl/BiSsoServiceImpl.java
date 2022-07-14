package com.aacoptics.bi.sso.service.impl;

import com.aacoptics.bi.sso.http.service.OkHttpCli;
import com.aacoptics.bi.sso.provider.FeiShuApi;
import com.aacoptics.bi.sso.service.BiSsoService;
import com.aacoptics.bi.sso.util.BIRsaEncrypt;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class BiSsoServiceImpl  implements BiSsoService {

    public static final String BI_URL = "https://bi.aacoptics.com:8181/webroot/decision";

    @Resource
    OkHttpCli okHttpCli;

    @Resource
    FeiShuApi feiShuApi;

    @Override
    public String getRedirectBiUrl(String code, String callBackUrl) throws Exception {
        log.info("开始生成BI自动登录URL，code=" + code);
        //1 获取app access token
        Map<String, String> params = new HashMap<>();
        params.put("app_id", feiShuApi.getBiAppKey());
        params.put("app_secret", feiShuApi.getBiAppSecret());

        String accessTokenResponse = okHttpCli.doPost("https://open.feishu.cn/open-apis/auth/v3/app_access_token/internal", params);
        /**
         * {
         *     "app_access_token": "t-7df392191603ff787a30acb1f59d443809a66fd1",
         *     "code": 0,
         *     "expire": 2421,
         *     "msg": "ok",
         *     "tenant_access_token": "t-7df392191603ff787a30acb1f59d443809a66fd1"
         * }
         */
        JSONObject accessTokenJsonObject = JSONObject.parseObject(accessTokenResponse);
        if(!"0".equals(accessTokenJsonObject.getString("code")))
        {
            log.error("生成BI自动登录URL失败，获取app_access_token失败，" + accessTokenResponse);
            return BI_URL;
        }
        String appAccessToken = accessTokenJsonObject.getString("app_access_token");
        if(StringUtils.isEmpty(appAccessToken))
        {
            log.error("生成BI自动登录URL失败，app_access_token为空");
            return BI_URL;
        }

        //2 获取当前登录用户ID信息
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("grant_type", "authorization_code");
        jsonParams.put("code", code);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + appAccessToken);
        headers.put("Content-Type", "application/json; charset=utf-8");

        String accessUserInfoResponse = okHttpCli.doPostJson("https://open.feishu.cn/open-apis/authen/v1/access_token", jsonParams, headers);
        /**
         * {
         *     "code": 0,
         *     "data": {
         *         "access_token": "u-09_a0iO199u9nf4ELw6dGt41lLI4k5KbWo001hg02LBn",
         *         "avatar_big": "https://s1-imfile.feishucdn.com/static-resource/v1/v2_455a471d-8098-4850-a9c7-3dc92c7bbc0g~?image_size=640x640&cut_type=&quality=&format=image&sticker_format=.webp",
         *         "avatar_middle": "https://s3-imfile.feishucdn.com/static-resource/v1/v2_455a471d-8098-4850-a9c7-3dc92c7bbc0g~?image_size=240x240&cut_type=&quality=&format=image&sticker_format=.webp",
         *         "avatar_thumb": "https://s1-imfile.feishucdn.com/static-resource/v1/v2_455a471d-8098-4850-a9c7-3dc92c7bbc0g~?image_size=72x72&cut_type=&quality=&format=image&sticker_format=.webp",
         *         "avatar_url": "https://s1-imfile.feishucdn.com/static-resource/v1/v2_455a471d-8098-4850-a9c7-3dc92c7bbc0g~?image_size=72x72&cut_type=&quality=&format=image&sticker_format=.webp",
         *         "en_name": "潘蕾",
         *         "expires_in": 6900,
         *         "name": "潘蕾",
         *         "open_id": "ou_c6b4ee01538b5c238343caec0323b45e",
         *         "refresh_expires_in": 2591700,
         *         "refresh_token": "ur-0EzCid2OpeoHZ14bWb7D_i41lf44k54bOE0001402Hwm",
         *         "tenant_key": "11ae648bb4819758",
         *         "token_type": "Bearer",
         *         "union_id": "on_d6f12df97d3acd062529c924b0294580"
         *     },
         *     "msg": "success"
         * }
         */
        JSONObject accessUserInfoJsonObject = JSONObject.parseObject(accessUserInfoResponse);
        if(!"0".equals(accessUserInfoJsonObject.getString("code")))
        {
            log.error("生成BI自动登录URL失败，当前登录用户ID信息失败，" + accessUserInfoResponse);
            return BI_URL;
        }

        String data = accessUserInfoJsonObject.getString("data");
        if(StringUtils.isEmpty(data))
        {
            log.error("生成BI自动登录URL失败，获取当前登录用户ID信息失败，open_id为空");
            return BI_URL;
        }
        String openId = JSONObject.parseObject(data).getString("open_id"); //获取用户ID
        if(StringUtils.isEmpty(openId))
        {
            log.error("生成BI自动登录URL失败，获取当前登录用户ID信息失败，open_id为空");
            return BI_URL;
        }

        //3 获取用户信息
        Map<String, String> userInfoRequestHeader = new HashMap<>();
        userInfoRequestHeader.put("Authorization", "Bearer " + appAccessToken);
        String userInfoResponse = okHttpCli.doGet("https://open.feishu.cn/open-apis/contact/v3/users/"+openId, null, userInfoRequestHeader);
        /**
         * {
         *     "code": 0,
         *     "data": {
         *         "user": {
         *             "avatar": {
         *                 "avatar_240": "https://s1-imfile.feishucdn.com/static-resource/v1/v2_455a471d-8098-4850-a9c7-3dc92c7bbc0g~?image_size=240x240&cut_type=&quality=&format=png&sticker_format=.webp",
         *                 "avatar_640": "https://s1-imfile.feishucdn.com/static-resource/v1/v2_455a471d-8098-4850-a9c7-3dc92c7bbc0g~?image_size=640x640&cut_type=&quality=&format=png&sticker_format=.webp",
         *                 "avatar_72": "https://s1-imfile.feishucdn.com/static-resource/v1/v2_455a471d-8098-4850-a9c7-3dc92c7bbc0g~?image_size=72x72&cut_type=&quality=&format=png&sticker_format=.webp",
         *                 "avatar_origin": "https://s1-imfile.feishucdn.com/static-resource/v1/v2_455a471d-8098-4850-a9c7-3dc92c7bbc0g~?image_size=noop&cut_type=&quality=&format=png&sticker_format=.webp"
         *             },
         *             "city": "",
         *             "country": "",
         *             "department_ids": [
         *                 "0"
         *             ],
         *             "description": "",
         *             "employee_no": "80000061",
         *             "employee_type": 1,
         *             "en_name": "",
         *             "gender": 0,
         *             "is_tenant_manager": true,
         *             "job_title": "",
         *             "join_time": 0,
         *             "mobile_visible": true,
         *             "name": "潘蕾",
         *             "open_id": "ou_c6b4ee01538b5c238343caec0323b45e",
         *             "orders": [
         *                 {
         *                     "department_id": "0",
         *                     "department_order": 1,
         *                     "user_order": 0
         *                 }
         *             ],
         *             "status": {
         *                 "is_activated": true,
         *                 "is_exited": false,
         *                 "is_frozen": false,
         *                 "is_resigned": false,
         *                 "is_unjoin": false
         *             },
         *             "union_id": "on_d6f12df97d3acd062529c924b0294580",
         *             "work_station": "1234567890"
         *         }
         *     },
         *     "msg": "success"
         * }
         */
        if(StringUtils.isEmpty(userInfoResponse))
        {
            log.error("生成BI自动登录URL失败，获取当前登录用户工号信息失败，工号相关信息为空");
            return BI_URL;
        }
        JSONObject userInfoResponseJsonObject = JSONObject.parseObject(userInfoResponse);
        if(!"0".equals(userInfoResponseJsonObject.getString("code")))
        {
            log.error("生成BI自动登录URL失败，获取当前登录用户工号信息失败，" + userInfoResponse);
            return BI_URL;
        }
        JSONObject userDataJsonObject = userInfoResponseJsonObject.getJSONObject("data");
        if(userDataJsonObject == null)
        {
            log.error("生成BI自动登录URL失败，获取当前登录用户工号信息失败，工号相关信息为空");
            return BI_URL;
        }
        JSONObject userJsonObject = userDataJsonObject.getJSONObject("user");
        if(userJsonObject == null)
        {
            log.error("生成BI自动登录URL失败，获取当前登录用户工号信息失败，工号相关信息为空");
            return BI_URL;
        }
        String employeeNo = userJsonObject.getString("employee_no");
        if(StringUtils.isEmpty(employeeNo))
        {
            log.error("生成BI自动登录URL失败，获取当前登录用户工号信息失败，工号为空");
            return BI_URL;
        }
        log.info("登录工号为：" + employeeNo);

        //4 获取BI单点登录token
        String ssoToken = BIRsaEncrypt.getSsoToken(employeeNo);

        String redirectUrl = "";
        if(StringUtils.isNotEmpty(callBackUrl))
        {
            if(callBackUrl.contains("?"))
            {
                redirectUrl = callBackUrl + "&ssoToken=" + ssoToken;
            }
            else
            {
                redirectUrl = callBackUrl + "?ssoToken=" + ssoToken;
            }
        }
        else
        {
            redirectUrl = BI_URL + "?ssoToken=" + ssoToken;
        }

        log.info("完成生成BI自动登录URL，ssoToken=" + ssoToken);
        return redirectUrl;
    }

}
