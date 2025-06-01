package me.rui.fdyzrank.controller.advice;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public void globalException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        JSONObject result = new JSONObject();
        result.put("success", false);
        result.put("exceptionType", e.getClass().getSimpleName());
        result.put("msg", e.getMessage());
        result.put("cookies", request.getCookies());

        response.setStatus(500);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(result.toJSONString());
        response.getWriter().flush();
        log.warn(result.toJSONString(), e);
    }
}
