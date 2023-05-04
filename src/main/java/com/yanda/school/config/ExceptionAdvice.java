package com.yanda.school.config;

import com.yanda.school.utils.EmosException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常捕获类
 */
@Slf4j
@RestControllerAdvice  //此注解捕获MVC抛出的各种异常
public class ExceptionAdvice {
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)   //规定异常返回的状态码 500
    @ExceptionHandler(Exception.class)  //只要是Exception类型的异常都可以捕获
    public String exceptionHandler(Exception e){
        log.error("执行异常",e);
        if(e instanceof MethodArgumentNotValidException){  //后端验证抛出的异常
            MethodArgumentNotValidException exception= (MethodArgumentNotValidException) e;
            return exception.getBindingResult().getFieldError().getDefaultMessage();
        }
        else if(e instanceof EmosException){  //普通异常
            EmosException exception= (EmosException) e;
            return exception.getMsg();
        }
        else if(e instanceof UnauthorizedException){   //未授权类型的的异常
            return "你不具备相关权限";
        }
        else{
            return "后端执行异常";
        }
    }
}
