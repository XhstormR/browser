package com.xhstormr.browser.plugins

import com.xhstormr.browser.attachmentHeaders
import com.xhstormr.browser.models.PathWrapper
import com.xhstormr.browser.models.UserSession
import com.xhstormr.browser.models.breadcrumbs
import com.xhstormr.browser.models.createHTML
import com.xhstormr.browser.sanitizePath
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.receiveMultipart
import io.ktor.server.request.uri
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import io.ktor.utils.io.copyAndClose
import io.ktor.utils.io.streams.asByteWriteChannel
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.io.path.notExists
import kotlin.io.path.outputStream

fun Application.configureRouting(config: ApplicationConfig) {
    val location = config.property("resource.location").getString().let { Path(it) }
    val enableUpload = config.property("resource.enable-upload").getString().toBoolean()

    routing {
        staticResources("/", "public", "login.html")

        authenticate("auth-form") {
            post("/login") {
                call.sessions.set(call.principal<UserSession>())
                call.respondRedirect("/browser")
            }
        }

        authenticate("auth-session") {
            post("/logout") {
                call.sessions.clear<UserSession>()
                call.respondRedirect("/")
            }

            route("/browser") {
                get {
                    val key = sanitizePath(call.parameters["key"])
                    val path = location.resolve(key)

                    when {
                        path.isDirectory() -> {
                            call.application.log.info("list: {}", path.toUri())
                            val breadcrumbs = breadcrumbs(key)

                            val paths = path.listDirectoryEntries()
                                .map { PathWrapper(it) }
                                .sorted()

                            val block = createHTML(key, enableUpload, paths, breadcrumbs)
                            call.respondHtml(block = block)
                        }

                        path.isRegularFile() -> {
                            call.application.log.info("download: {}", path.toUri())
                            call.response.header(HttpHeaders.ContentDisposition, attachmentHeaders(path.name))
                            call.respondFile(path.toFile())
                        }

                        path.notExists() -> call.respond(HttpStatusCode.NotFound)
                        else -> call.respond(HttpStatusCode.BadRequest)
                    }
                }

                post {
                    if (!enableUpload) call.respond(HttpStatusCode.BadRequest)

                    val key = sanitizePath(call.parameters["key"])
                    val path = location.resolve(key)

                    call.receiveMultipart().forEachPart { part ->
                        if (part is PartData.FileItem) {
                            var dest = path.resolve(part.originalFileName!!)
                            var i = 0
                            while (dest.exists()) {
                                dest = dest.resolveSibling("${part.originalFileName}.${++i}")
                            }
                            call.application.log.info("upload: {}", dest.toUri())
                            part.provider().copyAndClose(dest.outputStream().asByteWriteChannel())
                        }
                        part.dispose()
                    }
                    call.respondRedirect(call.request.uri)
                }
            }
        }
    }
}
