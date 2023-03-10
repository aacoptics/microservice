package com.aacoptics.wlg.dashboard.exception;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.common.web.exception.DefaultGlobalExceptionHandlerAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandlerAdvice extends DefaultGlobalExceptionHandlerAdvice {

    @SuppressWarnings("unchecked")
    @ExceptionHandler(value = {BusinessException.class})
    public Result<String> businessException(BusinessException ex) {
        log.error("业务异常", ex);

        return Result.fail(CommonErrorType.BUSINESS_EXCEPTION, ex.getLocalizedMessage());
    }
}