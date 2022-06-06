package com.aacoptics.notification.utils;

import cn.hutool.core.codec.Base64Encoder;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    public static JSONArray doPost(String url, Map<String, Object> param) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-type", "application/json;charset=utf-8");
        httpPost.setHeader("Connection", "Close");
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(param);
        StringEntity entity = new StringEntity(jsonObject.toString(), StandardCharsets.UTF_8);
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        CloseableHttpResponse httpResponse;
        String result = "";
        try {
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray jsonResult= JSONArray.parseArray(result);
        return jsonResult;
    }

    public static JSONObject postRequest(String url, Map<String, Object> param) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-type", "application/json;charset=utf-8");
        httpPost.setHeader("Connection", "Close");
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(param);
        StringEntity entity = new StringEntity(jsonObject.toString(), StandardCharsets.UTF_8);
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        CloseableHttpResponse httpResponse;
        String result = "";
        try {
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonResult= JSONObject.parseObject(result);
        return jsonResult;
    }

    public static JSONObject doGet(String url, List<NameValuePair> list, String userName, String passWord) {
        URIBuilder uri = null;
        try {
            uri = new URIBuilder(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        uri.setParameters(list);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String auth = userName + ":" + passWord;
        String encodedAuth = Base64Encoder.encode(auth);
        String authHeader = "Basic " + encodedAuth;
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(uri.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        httpGet.setHeader("Content-type", "application/json;charset=utf-8");
        httpGet.setHeader("Connection", "Close");
        httpGet.setHeader("Authorization", authHeader);
        CloseableHttpResponse httpResponse;
        String result = "";
        try {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonResult= JSONObject.parseObject(result);
        return jsonResult;
    }

}
