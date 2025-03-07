package com.xhstormr.browser.plugins

import com.xhstormr.browser.models.UserSession
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.sessions.SessionStorageMemory
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie

fun Application.configureSessions() {
    install(Sessions) {
        cookie<UserSession>("JSESSIONID", SessionStorageMemory())
    }
}
