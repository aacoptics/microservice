package com.aacoptics.ldap.sync.util;

import cn.hutool.core.codec.Base64Encoder;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpClientUtil {
    public static String doPost(String userName, String passWord, String url, Map<String, Object> param) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        String auth = userName + ":" + passWord;
        String encodedAuth = Base64Encoder.encode(auth);
        String authHeader = "Basic " + encodedAuth;
        httpPost.setHeader("Authorization", authHeader);
        httpPost.setHeader("Content-type", "application/json;charset=utf-8");
        httpPost.setHeader("Connection", "Close");
        JSONObject jsonObject = (JSONObject)JSONObject.toJSON(param);
        StringEntity entity = new StringEntity(jsonObject.toString(), StandardCharsets.UTF_8);
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        CloseableHttpResponse httpResponse;
        String result = "";
        try{
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "UTF-8");//
        }catch(IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
