package com.aacoptics.data.analysis.exception;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.common.web.exception.DefaultGlobalExceptionHandlerAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandlerAdvice extends DefaultGlobalExceptionHandlerAdvice {

    @ExceptionHandler(value = {BusinessException.class})
    public Result businessException(BusinessException ex) {
        log.error("业务异常", ex);

        return Result.fail(WlgReportErrorType.BUSINESS_EXCEPTION, ex.getLocalizedMessage());
    }
}