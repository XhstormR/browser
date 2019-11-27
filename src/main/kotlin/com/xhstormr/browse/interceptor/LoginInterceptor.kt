package com.xhstormr.browse.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoginInterceptor : HandlerInterceptorAdapter() {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean =
            if (request.session.getAttribute("isLogin") == true) {
                true
            } else {
                response.sendRedirect(request.contextPath + "/login")
                false
            }
}
