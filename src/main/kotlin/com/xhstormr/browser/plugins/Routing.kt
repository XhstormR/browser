package com.xhstormr.browser.plugins

import com.xhstormr.browser.attachmentHeaders
import com.xhstormr.browser.models.PathWrapper
import com.xhstormr.browser.models.breadcrumbs
import com.xhstormr.browser.models.createHTML
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.defaultResource
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.http.content.staticBasePackage
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
import io.ktor.util.normalizeAndRelativize
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.io.path.notExists

fun Application.configureRouting(config: ApplicationConfig) {
    val location = config.property("resource.location").getString().let { Path(it) }
    val enableUpload = config.property("resource.enable-upload").getString().toBoolean()

    routing {
        static {
            staticBasePackage = "public"
            resources(".")
            defaultResource("login.html")
        }

        authenticate("auth-form") {
            post("/login") {
                call.sessions.set(call.principal<UserIdPrincipal>())
                call.respondRedirect("/browser")
            }
        }

        authenticate("auth-session") {
            post("/logout") {
                call.sessions.clear<UserIdPrincipal>()
                call.respondRedirect("/")
            }

            route("/browser") {
                get {
                    val key = (call.parameters["key"] ?: "/").let { Path(it).normalizeAndRelativize() }
                    val path = location.resolve(key)

                    when {
                        path.isDirectory() -> {
                            call.application.log.info("list: {}", path)
                            val breadcrumbs = breadcrumbs(key)

                            val paths = path.listDirectoryEntries()
                                .map { PathWrapper(it) }
                                .sortedByDescending { it.isDir }

                            val block = createHTML(key, enableUpload, paths, breadcrumbs)
                            call.respondHtml(block = block)
                        }

                        path.isRegularFile() -> {
                            call.application.log.info("download: {}", path)
                            call.response.header(HttpHeaders.ContentDisposition, attachmentHeaders(path.name))
                            call.respondFile(path.toFile())
                        }

                        path.notExists() -> call.respond(HttpStatusCode.NotFound)
                        else -> call.respond(HttpStatusCode.BadRequest)
                    }
                }

                post {
                    if (!enableUpload) call.respond(HttpStatusCode.BadRequest)

                    val key = (call.parameters["key"] ?: "/").let { Path(it).normalizeAndRelativize() }
                    val path = location.resolve(key)

                    call.receiveMultipart().forEachPart { part ->
                        if (part is PartData.FileItem) {
                            var dest = path.resolve(part.originalFileName!!)
                            var i = 0
                            while (dest.exists()) {
                                dest = dest.resolveSibling("${part.originalFileName}.${++i}")
                            }
                            call.application.log.info("upload: {}", dest)
                            Files.copy(part.streamProvider(), dest)
                        }
                        part.dispose()
                    }
                    call.respondRedirect(call.request.uri)
                }
            }
        }
    }
}
