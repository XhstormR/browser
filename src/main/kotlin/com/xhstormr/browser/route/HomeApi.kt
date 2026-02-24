package com.xhstormr.browser.route

import io.ktor.resources.Resource

@Resource("/")
class HomeApi {

    @Resource("/login")
    class Login(val parent: HomeApi = HomeApi())

    @Resource("/logout")
    class Logout(val parent: HomeApi = HomeApi())

    @Resource("/browser")
    class Browser(val parent: HomeApi = HomeApi(), val key: String? = null)
}
