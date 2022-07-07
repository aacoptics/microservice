package com.aacoptics.jacob.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class OkHttpCli {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");

    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
				.connectTimeout(60, TimeUnit.SECONDS)
				.writeTimeout(120, TimeUnit.SECONDS)
				.build();;

    /**
     * get 请求
     *
     * @param url 请求url地址
     * @return string
     */
    public String doGet(String url) {
        return doGet(url, null, null);
    }


    /**
     * get 请求
     *
     * @param url    请求url地址
     * @param params 请求参数 map
     * @return string
     */
    public String doGet(String url, Map<String, String> params) {
        return doGet(url, params, null);
    }

    /**
     * get 请求
     *
     * @param url     请求url地址
     * @param params  请求参数 map
     * @param headers 请求头字段 map
     * @return string
     */
    public String doGet(String url, Map<String, String> params, Map<String, String> headers) {
        StringBuilder sb = new StringBuilder(url);
        if (params != null && params.keySet().size() > 0) {
            boolean firstFlag = true;
            for (String key : params.keySet()) {
                if (firstFlag) {
                    sb.append("?").append(key).append("=").append(params.get(key));
                    firstFlag = false;
                } else {
                    sb.append("&").append(key).append("=").append(params.get(key));
                }
            }
        }

        Request.Builder builder = new Request.Builder();
        if (headers != null && headers.keySet().size() > 0) {
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
        }

        Request request = builder.url(sb.toString()).build();
        return execute(request);
    }

    /**
     * post 请求
     *
     * @param url    请求url地址
     * @param params 请求参数 map
     * @return string
     */
    public String doPost(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();

        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();

        return execute(request);
    }


    /**
     * post 请求, 请求数据为 json 的字符串
     *
     * @param url        请求url地址
     * @param jsonObject 请求数据
     * @return M
     */
    public <T, M> M doPostJson(String url, T jsonObject, Class<M> clz) {
        try {
            String responseStr = executePost(url, new ObjectMapper().writeValueAsString(jsonObject), JSON);
            if (Objects.isNull(responseStr)) return null;
            return new ObjectMapper().readValue(responseStr, clz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * post 请求, 请求数据为 json 的字符串
     *
     * @param url        请求url地址
     * @param jsonObject 请求数据
     * @return string
     */
    public <T> String doPostJson(String url, T jsonObject) {
        try {
            return executePost(url, new ObjectMapper().writeValueAsString(jsonObject), JSON);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * post 请求, 请求数据为 xml 的字符串
     *
     * @param url 请求url地址
     * @param xml 请求数据, xml 字符串
     * @return string
     */
    public String doPostXml(String url, String xml) {
        return executePost(url, xml, XML);
    }

    private String executePost(String url, String data, MediaType contentType) {
        RequestBody requestBody = RequestBody.create(contentType, data);
        Request request = new Request.Builder().url(url).post(requestBody).build();

        return execute(request);
    }

    private String execute(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return Objects.requireNonNull(response.body()).string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
