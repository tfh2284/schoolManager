package com.xxxx.school.aspect;

import com.xxxx.school.annoation.RequiredPermission;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@Aspect
public class PermissionProxy {

    @Resource
    private HttpSession session;

    @Around(value = "@annotation(com.xxxx.school.annoation.RequiredPermission)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 获得对应角色的权限（session中）
        Object result = null;
        List<String> permissions = (List<String>) session.getAttribute("permissions");
        // 判断权限类型
        if (permissions == null || permissions.size() < 1) {
            // 若权限为空则抛出异常
            throw new AuthException();
        }
        // 得到对应目标
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        // 得到方法上的注解
        RequiredPermission requiredPermission = methodSignature.getMethod().getDeclaredAnnotation(RequiredPermission.class);
        // 得到注解上对应的状态码
        if (!(permissions.contains(requiredPermission.code()))) {
            throw new AuthException();
        }
        result = pjp.proceed();
        return result;
    }
}
