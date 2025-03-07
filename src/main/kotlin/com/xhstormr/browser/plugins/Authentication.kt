package com.xhstormr.browser.plugins

import com.xhstormr.browser.models.UserSession
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserPasswordCredential
import io.ktor.server.auth.form
import io.ktor.server.auth.session
import io.ktor.server.config.ApplicationConfig
import java.time.Instant

fun Application.configureAuthentication(config: ApplicationConfig) {
    val username = config.property("security.username").getString()
    val password = config.property("security.password").getString()
    val credential = UserPasswordCredential(username, password)

    install(Authentication) {
        form("auth-form") {
            validate { input ->
                if (credential == input) UserSession(input.name, Instant.now().epochSecond)
                else null
            }
            challenge("/login.html")
        }

        session<UserSession>("auth-session")
    }
}
