package com.xhstormr.browser.plugin

import com.xhstormr.browser.config.SecurityConfig
import com.xhstormr.browser.model.UserSession
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserPasswordCredential
import io.ktor.server.auth.form
import io.ktor.server.auth.session
import io.ktor.server.config.property
import java.time.Instant

object Auth {
    const val FORM = "auth-form"
    const val SESSION = "auth-session"
}

fun Application.configureAuthentication() {
    val (username, password) = property<SecurityConfig>("security")
    val credential = UserPasswordCredential(username, password)

    install(Authentication) {
        form(Auth.FORM) {
            validate { input ->
                if (credential == input) UserSession(input.name, Instant.now().epochSecond)
                else null
            }
            challenge("/login.html")
        }

        session<UserSession>(Auth.SESSION)
    }
}
