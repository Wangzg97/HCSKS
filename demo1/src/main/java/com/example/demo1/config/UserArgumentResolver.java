package com.example.demo1.config;

import com.example.demo1.access.UserContext;
import com.example.demo1.domain.UserInf;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    //返回true表示要解析该参数
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //获取要解析的参数类型
        Class<?> clazz = parameter.getParameterType();
        return clazz == UserInf.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return UserContext.getUser();
    }
}
