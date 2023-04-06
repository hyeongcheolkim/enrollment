package com.khc.enrollment.aop;

import com.khc.enrollment.exception.exceptoin.NotAuthenticatedException;
import com.khc.enrollment.session.SessionConst;
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
@Order(1)
public class LoginAnySessionAuthenticator {

    @Around("@annotation(com.khc.enrollment.aop.annotation.PermitProfessor)")
    public Object doAnnotation(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
        if (session == null)
            throw new NotAuthenticatedException();

        if (session.getAttribute(SessionConst.LOGIN_STUDENT) == null
                && session.getAttribute(SessionConst.LOGIN_PROFESSOR) == null
                && session.getAttribute(SessionConst.LOGIN_ADMIN) == null)
            throw new NotAuthenticatedException();

        return proceedingJoinPoint.proceed();
    }
}
