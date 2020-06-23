package com.wals.pipe.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zcj
 * @Description TODO
 * @createTime 2020-05-04 12:00:00
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String uri = request.getRequestURI();
        if ( (uri.equals("") ||  uri.equals("/") || uri.equals("/index")
                 || uri.equals("/index.html") || uri.equals("/classify/categories")
                || uri.equals("/banner/carousels")
                || uri.equals("/product/goods/edit")
                || uri.equals("/profile"))
                && null == request.getSession().getAttribute("loginUser")) {
            if (!uri.equals("/")){
                request.getSession().setAttribute("errorMsg", "请登陆");
            }

            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        } else {
            request.getSession().removeAttribute("errorMsg");
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}