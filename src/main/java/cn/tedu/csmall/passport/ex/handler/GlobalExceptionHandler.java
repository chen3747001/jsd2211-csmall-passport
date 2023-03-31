package cn.tedu.csmall.passport.ex.handler;


import cn.tedu.csmall.passport.ex.ServiceException;
import cn.tedu.csmall.passport.web.JsonResult;
import cn.tedu.csmall.passport.web.ServiceCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public void GlobalExceptionHandler(){
        log.info("全局异常处理器开始启动");
    }

    @ExceptionHandler
    public JsonResult handleServiceException(ServiceException e){
        log.info("捕获到异常 ServiceException: {}",e.getMessage());

        return JsonResult.fail(e);
    }

    @ExceptionHandler
    public JsonResult handleBindException(BindException e){
        log.debug("捕获到BindException：{}",e.getMessage());
        //显示单个错误
        String massage=e.getFieldError().getDefaultMessage();
        return JsonResult.fail(ServiceCode.ERR_BAD_REQUEST,massage);

        //显示多个错误
//        StringBuilder stringBuilder=new StringBuilder();
//        List<FieldError> fieldErrors = e.getFieldErrors();
//        for(FieldError fieldError : fieldErrors){
//            stringBuilder.append(fieldError.getDefaultMessage());
//        }
//        return JsonResult.fail(ServiceCode.ERR_BAD_REQUEST,stringBuilder.toString());
    }

    @ExceptionHandler
    public JsonResult handleConstraintViolationException(ConstraintViolationException e) {
        log.debug("捕获到ConstraintViolationException：{}", e.getMessage());
        StringBuilder stringBuilder = new StringBuilder();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            stringBuilder.append(constraintViolation.getMessage());
        }

        return JsonResult.fail(ServiceCode.ERR_BAD_REQUEST, stringBuilder.toString());
    }

    @ExceptionHandler
    public JsonResult<Void> handleNullPointerException(NullPointerException e){
        log.info("捕获到异常 NullPointerException");
        e.printStackTrace();
        return JsonResult.fail(ServiceCode.ERR_CONFLICT,"遇到空指针异常，请查看控制台输出");
    }

    @ExceptionHandler
    public String handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        log.info("捕获到异常 HttpRequestMethodNotSupportedException: {}",e.getMessage());
        return "请求错误，非法访问";
    }

    @ExceptionHandler
    public String handleThrowable(Throwable e){
        log.info("捕获到异常 Throwable: {}",e.getMessage());
        e.printStackTrace();
        return "服务器运行过程中出现了未知错误";
    }
}
