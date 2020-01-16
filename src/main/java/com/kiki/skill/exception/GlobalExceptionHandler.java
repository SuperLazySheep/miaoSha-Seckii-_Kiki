package com.kiki.skill.exception;

import com.kiki.skill.result.MsgCode;
import com.kiki.skill.result.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 全局异常处理
 * @author Kiki
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    //拦截异常
    @ExceptionHandler(Exception.class)
    public ResultData exceptionHandle(HttpServletRequest request, Exception e){
        e.printStackTrace();
        if(e instanceof BindException){
           BindException be  = (BindException)e;
            List<ObjectError> errors = be.getAllErrors();
            ObjectError error = errors.get(0);
            String message = error.getDefaultMessage();
            log.error(message);
            return ResultData.error(MsgCode.BIND_ERROR.findArgs(message));
        }else if(e instanceof MyGlobalException){
            MyGlobalException me = (MyGlobalException)e;
            log.error(me.getMessage());
            return ResultData.error(me.getCm());
        }else{
            log.error(e.getMessage());
            return ResultData.error(MsgCode.SERVER_ERROR);
        }
    }
}
