package com.xhstormr.browser.route

import com.xhstormr.browser.model.UserSession
import com.xhstormr.browser.plugin.Auth
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set

fun Route.authRoutes() {
    get<HomeApi> {
        call.respondRedirect("/login.html")
    }

    authenticate(Auth.FORM) {
        post<HomeApi.Login> {
            call.sessions.set(call.principal<UserSession>())
            call.respondRedirect("/browser")
        }
    }

    authenticate(Auth.SESSION) {
        post<HomeApi.Logout> {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/")
        }
    }
}
