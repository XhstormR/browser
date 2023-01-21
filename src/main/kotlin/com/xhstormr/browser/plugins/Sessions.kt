package com.xhstormr.browser.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.sessions.SessionStorageMemory
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie

fun Application.configureSessions() {
    install(Sessions) {
        cookie<UserIdPrincipal>("JSESSIONID", SessionStorageMemory()) {
            cookie.maxAgeInSeconds = 0
        }
    }
}
