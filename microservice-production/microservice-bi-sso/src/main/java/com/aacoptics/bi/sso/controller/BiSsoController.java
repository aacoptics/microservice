package com.aacoptics.bi.sso.controller;

import com.aacoptics.bi.sso.service.BiSsoService;
import com.aacoptics.bi.sso.service.impl.BiSsoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/biSsoController")
@Api("biSsoController")
@Slf4j
public class BiSsoController {

    @Autowired
    BiSsoService biSsoService;

    @ApiOperation(value = "BI单点登录", notes = "BI单点登录")
    @ApiResponses(
            @ApiResponse(code = 200, message = "处理成功")
    )

    @GetMapping(value = "/redirectBi")
    public void redirectBi(HttpServletRequest request, HttpServletResponse response) {
        try {
            String redirectUrl = "";
            Map<String,String[]> parameterMaps = request.getParameterMap();
            if(parameterMaps == null)
            {
                response.sendRedirect(BiSsoServiceImpl.BI_URL);
                return;
            }
            if(parameterMaps.get("code") == null || parameterMaps.get("code").length == 0)
            {
                response.sendRedirect(BiSsoServiceImpl.BI_URL);
                return;
            }
            String[] code = parameterMaps.get("code");
            redirectUrl = biSsoService.getRedirectBiUrl(code[0]);

            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            log.error("重定向登录BI失败", e);
            try {
                response.sendRedirect(BiSsoServiceImpl.BI_URL);
            } catch (IOException ioException) {
                log.error("重定向登录BI失败", ioException);
            }
        }
    }
}