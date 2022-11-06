package com.jk.sleuth;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@Component
public class CorrelationIdInterceptor implements HandlerInterceptor {

    private final static String CORRELATION_ID_NAME = "correlation-id";

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final String correlationId = getOrGenerateCorrelationId(request);
        MDC.put(CORRELATION_ID_NAME, correlationId);
        log.info("New correlation id: {}", correlationId);
        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(CORRELATION_ID_NAME);
    }

    private String getOrGenerateCorrelationId(final HttpServletRequest request) {
        final String correlationId = request.getHeaders(CORRELATION_ID_NAME).toString();
        if(correlationId.isEmpty()) {
            return UUID.randomUUID().toString();
        }
        return correlationId;
    }

}