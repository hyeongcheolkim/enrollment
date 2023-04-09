package com.khc.enrollment.aop;

import com.khc.enrollment.exception.exceptoin.NotAuthenticatedException;
import com.khc.enrollment.session.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@Component
@Aspect
@Slf4j
public class LoginStudentSessionAuthenticator {

    @Around("@annotation(com.khc.enrollment.aop.annotation.PermitStudent)")
    public Object doAnnotation(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
        if(session == null || session.getAttribute(SessionConst.LOGIN_STUDENT) == null) {
            log.info("접근 거부");
            throw new NotAuthenticatedException();
        }
        return proceedingJoinPoint.proceed();
    }
}
