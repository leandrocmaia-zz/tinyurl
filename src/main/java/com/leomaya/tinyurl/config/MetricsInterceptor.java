package com.leomaya.tinyurl.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Getter
class MetricsInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    MetricsConfig metricsConfig;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        int status = response.getStatus();
        if (status > 499 && status < 600) {
            metricsConfig.getInternalServerError().increment();
        } else if (status == 404) {
            metricsConfig.getNotFound().increment();
        } else if (status == 400) {
            metricsConfig.getBadRequest().increment();
        } else if (ex != null) {
            metricsConfig.getError().increment();
        }
        super.afterCompletion(request, response, handler, ex);
    }
}
