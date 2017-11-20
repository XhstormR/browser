package com.xhstormr.browse.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/login")
class LoginController(
        private val username: String,
        private val password: String
) {
    @GetMapping
    fun get() = "login"

    @PostMapping
    fun post(@RequestParam("username") usernameP: String,
             @RequestParam("password") passwordP: String, session: HttpSession): String =
            if (username == usernameP && password == passwordP) {
                session.setAttribute("isLogin", true)
                "redirect:/"
            } else {
                "redirect:/login"
            }
}
