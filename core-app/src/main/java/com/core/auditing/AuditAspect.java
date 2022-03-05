package com.core.auditing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class AuditAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditAspect.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();


    /**
     * @param joinPoint Around annotation method is added on this method,
     *                  which will add the ProceedingJoinPoint parameter by default.
     *                  this parameter will be used to fetch the invoked method details, like signature, etc.
     * @return This method will return Object type, if the calling method has return type void then it will return null.
     * @throws Throwable If any kind of exception occurs on target method invocation, it will throw the same.
     *                   <p>
     *                   This is an aspect, which is responsible to add audit logs of the method on which @Auditable annotation is added.
     */
    @Around(value = "@annotation(com.core.auditing.Auditable)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object methodResponse = null;
        Throwable exception = null;
        try {
            methodResponse = joinPoint.proceed();

            return methodResponse;
        } catch (Throwable e) {
            exception = e;
            throw e;
        } finally {
            logRequestResponse(joinPoint, methodResponse, exception);
        }
    }

    private void logRequestResponse(ProceedingJoinPoint joinPoint, Object methodResponse, Throwable exception) {
        try {
            Object t = joinPoint.getTarget();
            StandardEvaluationContext context = new StandardEvaluationContext(t);

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Auditable audit = method.getAnnotation(Auditable.class);

            if (!Boolean.valueOf(audit.isEnabled())) {
                return;
            }

            Map<String, Object> logMap = new HashMap<>();
            logMap.put("Method", method.getDeclaringClass().getName() + "." + method.getName());

            logMap.put("Request", getRequestParam(signature, method, joinPoint));

            if (exception != null) {
                logMap.put("Error", exception.getStackTrace());
            } else {

                logMap.put("Response", methodResponse);
            }
            LOGGER.info(MAPPER.writeValueAsString(logMap));

        } catch (Exception e) {
            LOGGER.warn("exception occurred while audit logging {}", e.getMessage());
        }

    }

    /**
     * @param signature : signature object on which @Auditable annotation added
     * @param method    : method name on which @Auditable annotation added
     * @param joinPoint
     * @return It will return the HashMap in response. which will contain the request parameter name of the annotated method.
     */
    private Map<String, Object> getRequestParam(MethodSignature signature, Method method, JoinPoint joinPoint) {

        Parameter[] parameters = method.getParameters();
        Auditable audit = method.getAnnotation(Auditable.class);

        if (parameters == null || parameters.length == 0) {
            return null;
        }

        String[] parameterNames = audit.parameterNames();

        if (parameterNames == null || parameterNames.length != method.getParameterCount()) {
            if (signature.getParameterNames() == null) {
                parameterNames = Arrays.stream(method.getParameters()).map(y -> y.getName()).toArray(String[]::new);
            } else {
                parameterNames = signature.getParameterNames();
            }
        }

        Object[] args = joinPoint.getArgs();
        Map<String, Object> parameterMap = new HashMap<>();

        for (int i = 0; i < parameterNames.length; i++) {
            parameterMap.put(parameterNames[i], args[i]);
        }
        return parameterMap;
    }

    private String resolve(ConfigurableBeanFactory beanFactory, String value) {
        if (StringUtils.hasText(value)) {

            BeanExpressionResolver resolver = beanFactory.getBeanExpressionResolver();
            String resolved = beanFactory.resolveEmbeddedValue(value);
            if (resolver == null) {
                return resolved;
            }
            Object evaluateValue = resolver.evaluate(resolved, new BeanExpressionContext(beanFactory, null));
            if (evaluateValue != null) {
                return String.valueOf(evaluateValue);
            }
            return null;
        }
        return value;
    }
}
