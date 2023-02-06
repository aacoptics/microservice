package com.aacoptics.wlg.broadcast.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.broadcast.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

@RestController
@RequestMapping("/broadcast")
@Api("broadcast")
@Slf4j
public class BroadcastController {

    @ApiOperation(value = "测试", notes = "测试")
    @GetMapping(value = "/helloWorld")
    public Result downloadVoiceFile() {
        return Result.success("hello world!");
    }


    @ApiOperation(value = "下载mp3语音文件", notes = "下载mp3语音文件")
    @GetMapping(value = "/downloadPerformanceDataAvailable")
    public Result downloadVoiceFile(HttpServletResponse response) {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("voice/performanceDataAvailable.mp3");
            if(inputStream == null)
            {
                throw new BusinessException("mp3文件不存在");
            }
            //强制下载不打开
            response.setContentType("application/force-download");
            OutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("性能数据已出", "UTF-8"));
            int b = 0;
            byte[] buffer = new byte[1000000];
            while (b != -1) {
                b = inputStream.read(buffer);
                if (b != -1) {
                    out.write(buffer, 0, b);
                }
            }
            inputStream.close();
            out.close();
            out.flush();
            return Result.success("文件下载成功");
        } catch (IOException e) {
            log.error("mp3文件下载异常", e);
            return Result.fail("文件下载错误" + e.getLocalizedMessage());
        }

    }

}