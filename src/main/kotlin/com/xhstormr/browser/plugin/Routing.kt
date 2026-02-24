package com.xhstormr.browser.plugin

import com.xhstormr.browser.config.ResourceConfig
import com.xhstormr.browser.route.authRoutes
import com.xhstormr.browser.route.browserRoutes
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.config.property
import io.ktor.server.http.content.staticResources
import io.ktor.server.resources.Resources
import io.ktor.server.routing.routing
import kotlin.io.path.Path

fun Application.configureRouting() {
    val resourceConfig = property<ResourceConfig>("resource")
    val location = Path(resourceConfig.location)
    val enableUpload = resourceConfig.enableUpload

    log.info("List Directory: {}", location.toRealPath().toUri())

    install(Resources)

    routing {
        staticResources("/", "public", "login.html")
        authRoutes()
        browserRoutes(location, enableUpload)
    }
}
