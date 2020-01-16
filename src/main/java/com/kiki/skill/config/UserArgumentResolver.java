package com.kiki.skill.config;

import com.kiki.skill.domain.SkillUser;
import com.kiki.skill.redis.SkillUserKey;
import com.kiki.skill.service.SkillUserService;
import com.kiki.skill.service.impl.SkillUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kiKi
 * @since 5/12
 */

//将UserArgumentResolver注册到config里面去
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    SkillUserService skillUserService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //返回参数类型
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == SkillUser.class;
    }

    //参数容器Handler
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        HttpServletResponse response = (HttpServletResponse) nativeWebRequest.getNativeResponse();
        String paramToToken = request.getParameter(SkillUserServiceImpl.COOKIE_TOKEN_NAME);
        String cookieToToken = getCookieValue(request,SkillUserServiceImpl.COOKIE_TOKEN_NAME);
        if(StringUtils.isEmpty(paramToToken) && StringUtils.isEmpty(cookieToToken)){
            return null;
        }
        String token = StringUtils.isEmpty(paramToToken) ? cookieToToken : paramToToken;
        SkillUser user = skillUserService.getByToken(response, token);
        return user;
    }


    public  String getCookieValue(HttpServletRequest request, String cookieTokenName) {
        //遍历cookie
        Cookie[] cookies = request.getCookies();
        // jmeter 压力测试 出现nullpointException
        if(cookies == null || cookies.length <= 0){
            return null;
        }
        for(Cookie cookie :cookies){
            if(cookie.getName().equals(cookieTokenName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
