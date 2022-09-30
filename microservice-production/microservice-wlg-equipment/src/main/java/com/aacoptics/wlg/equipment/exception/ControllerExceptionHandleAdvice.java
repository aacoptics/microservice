package com.aacoptics.wlg.equipment.exception;

import com.aacoptics.common.core.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandleAdvice {

    @ExceptionHandler
    public Result handler(HttpServletRequest req, HttpServletResponse res, Exception e) {
        log.info("Restful Http请求发生异常...");
        res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        if (e instanceof NullPointerException) {
            log.error("发生空指针异常：" + e.getMessage(), e);
            return Result.fail("发生空指针异常");
        } else if (e instanceof IllegalArgumentException) {
            log.error("请求参数类型不匹配：" + e.getMessage(), e);
            return Result.fail("请求参数类型不匹配");
        } else if (e instanceof MissingRequestHeaderException) {
            log.error("缺少对应的标头：" + e.getMessage(), e);
            res.setStatus(HttpStatus.BAD_REQUEST.value());
            return Result.fail("缺少对应的标头，详细信息：" + e.getMessage());
        } else {
            log.error(e.getMessage(), e);
            return Result.fail(e.getMessage());
        }
    }
}
