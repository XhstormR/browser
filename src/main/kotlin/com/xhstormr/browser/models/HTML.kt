package com.xhstormr.browser.models

import kotlinx.html.FormEncType
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.fileInput
import kotlinx.html.footer
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.header
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.main
import kotlinx.html.meta
import kotlinx.html.onChange
import kotlinx.html.onKeyUp
import kotlinx.html.postForm
import kotlinx.html.script
import kotlinx.html.span
import kotlinx.html.styleLink
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.title
import kotlinx.html.tr
import kotlinx.html.unsafe
import java.nio.file.Path
import kotlin.collections.set

fun createHTML(
    key: Path,
    enableUpload: Boolean,
    paths: List<PathWrapper>,
    breadcrumbs: List<Breadcrumb>,
): HTML.() -> Unit = {
    head {
        meta("viewport", "width=device-width, initial-scale=1.0", "utf-8")
        title(breadcrumbs.lastOrNull()?.text ?: "/")
        styleLink("/css/main.css")
    }
    body {
        unsafe { raw(SVG) }
        header {
            h1 {
                a(BASE_HREF) { text("/") }
                breadcrumbs.forEach {
                    a(BASE_HREF + it.link) { text(it.text) }
                    text("/")
                }
            }
        }
        main {
            div("meta") {
                div {
                    id = "summary"
                    span("meta-item") { text(paths.count { it.isDir }.toString() + " directory") }
                    span("meta-item") { text(paths.count { !it.isDir }.toString() + " file") }
                    span("meta-item") {
                        input {
                            id = "filter"
                            onKeyUp = "filter()"
                            placeholder = "过滤"
                        }
                    }
                }
            }
            div("listing") {
                table {
                    thead {
                        tr {
                            th { text("Name") }
                            th { text("Size") }
                            th(classes = "hideable") { text("Modified") }
                        }
                    }
                    tbody {
                        if (breadcrumbs.isNotEmpty()) {
                            tr {
                                td { a(BASE_HREF + (key.parent ?: "")) { span("name") { text("Go up") } } }
                                td { text("—") }
                                td("hideable") { text("—") }
                            }
                        }
                        paths.forEach {
                            tr("file") {
                                td {
                                    a(BASE_HREF + key.resolve(it.name)) {
                                        unsafe { raw(if (it.isDir) SVG_DIR else SVG_FILE) }
                                        span("name") { text(it.name) }
                                    }
                                }
                                td { text(if (it.isDir) "—" else it.formatSize()) }
                                td("hideable") { text(it.formatTime()) }
                            }
                        }
                        if (enableUpload) {
                            tr {
                                td {
                                    colSpan = "3"
                                    postForm(BASE_HREF + key, FormEncType.multipartFormData) {
                                        onChange = "submit()"
                                        label {
                                            id = "box"
                                            htmlFor = "uploadFile"
                                            text("选择文件进行上传")
                                        }
                                        fileInput(name = "file") {
                                            id = "uploadFile"
                                            attributes["style"] = "display: none;"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        footer {
            text("© 2023 ")
            a("https://github.com/XhstormR/browser") {
                text("XhstormR")
            }
        }
        script { src = "/js/main.js" }
    }
}

private const val BASE_HREF = "/browser?key=/"

private const val SVG = """
<svg xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg" height="0" width="0" style="position: absolute;">
    <defs>
        <g id="folder" fill="none">
            <path d="M285.22 37.55h-142.6L110.9 0H31.7C14.25 0 0 16.9 0 37.55v75.1h316.92V75.1c0-20.65-14.26-37.55-31.7-37.55z" fill="#FFA000"></path>
            <path d="M285.22 36H31.7C14.25 36 0 50.28 0 67.74v158.7c0 17.47 14.26 31.75 31.7 31.75H285.2c17.44 0 31.7-14.3 31.7-31.75V67.75c0-17.47-14.26-31.75-31.7-31.75z" fill="#FFCA28"></path>
        </g>
        <g id="file" stroke="#000" stroke-width="25" fill="#FFF" fill-rule="evenodd" stroke-linecap="round" stroke-linejoin="round">
            <path d="M13 24.12v274.76c0 6.16 5.87 11.12 13.17 11.12H239c7.3 0 13.17-4.96 13.17-11.12V136.15S132.6 13 128.37 13H26.17C18.87 13 13 17.96 13 24.12z"></path>
            <path d="M129.37 13L129 113.9c0 10.58 7.26 19.1 16.27 19.1H249L129.37 13z"></path>
        </g>
    </defs>
</svg>
"""
private const val SVG_DIR =
    """<svg width="1.5em" height="1em" viewBox="0 0 317 259"><use xlink:href="#folder"></use></svg>"""
private const val SVG_FILE =
    """<svg width="1.5em" height="1em" viewBox="0 0 265 323"><use xlink:href="#file"></use></svg>"""
