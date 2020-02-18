package com.henu.mall.exception;

import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

import static com.henu.mall.enums.ResponseEnum.ERROR;


/**
 * @author lv
 * @date 2020-01-21 10:14
 */
@Slf4j
@ControllerAdvice
public class MallExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
//	@ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseVo handle(RuntimeException e) {
        return ResponseVo.error(ERROR, e.getMessage());
    }

    @ExceptionHandler(UserLoginException.class)
    @ResponseBody
    public ResponseVo userLoginHandle() {
        return ResponseVo.error(ResponseEnum.NEED_LOGIN);
    }

    @ExceptionHandler(UserTokenException.class)
    @ResponseBody
    public ResponseVo userTokenHandle() {
        return ResponseVo.error(ResponseEnum.TOKEN_EXPIRE_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVo notValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        Objects.requireNonNull(bindingResult.getFieldError());
        return ResponseVo.error(ResponseEnum.PARAM_ERROR,bindingResult.getFieldError().getField()+ "" +bindingResult.getFieldError().getDefaultMessage());
    }
}
