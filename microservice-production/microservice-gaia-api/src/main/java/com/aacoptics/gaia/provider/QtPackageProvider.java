package com.aacoptics.gaia.provider;

import com.alibaba.fastjson.JSONObject;
import feign.Headers;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.Serializable;

@Component
@FeignClient(name = "qt-package-server",
        url = "http://openapi.qtechglobal.com:8003/api",
        fallback = QtPackageProviderFallback.class)
public interface QtPackageProvider {

    @PostMapping(value = "/Token/CreateToken")
    @Headers("Content-Type: application/json")
    JSONObject getToken(@RequestBody QtUserInfo userInfo);

    @PostMapping(value = "/Vendor/VendorCarton")
    @Headers("Content-Type: application/json")
    JSONObject uploadQtPackageInfo(@RequestBody JSONObject qtPackageParam, @RequestHeader("Header") String token);


    @Data
    @EqualsAndHashCode()
    @Accessors(chain = true)
    class QtUserInfo implements Serializable {

        private String UserName;

        private String Pwd;

        public QtUserInfo(String userName, String pwd){
            UserName = userName;
            Pwd = pwd;
        }
    }
}