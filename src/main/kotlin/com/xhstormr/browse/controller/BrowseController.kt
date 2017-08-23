package com.xhstormr.browse.controller

import com.xhstormr.browse.entity.Breadcrumb
import com.xhstormr.browse.entity.PathWrapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.yaml.snakeyaml.util.UriEncoder
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/", "/{:^assets.+|(?!assets).*$}/**") //https://stackoverflow.com/questions/1240275/how-to-negate-specific-word-in-regex#3688210
class BrowseController {
    @Autowired
    private lateinit var basePath: Path

    @GetMapping
    fun list(request: HttpServletRequest, response: HttpServletResponse, model: Model): String? {
        val path = request.servletPath.substring(1)
        val file = basePath.resolve(path)

        when {
            Files.isDirectory(file) -> {
                val breadcrumbs = path.split('/')
                        .filter { it.isNotBlank() }
                        .toMutableList().apply { this.add(0, "/") }
                        .reversed()
                        .mapIndexed { index, str -> Breadcrumb(crumb(index), str) }
                        .reversed()

                val pathMap = Files.list(file)
                        .collect(Collectors.partitioningBy { Files.isRegularFile(it) })
                        .mapValues { it.value.map { PathWrapper(it) } }

                val fileCount = pathMap.getValue(true).size
                val direCount = pathMap.getValue(false).size

                model.addAttribute("breadcrumbs", breadcrumbs)
                model.addAttribute("pathMap", pathMap)
                model.addAttribute("fileCount", fileCount)
                model.addAttribute("direCount", direCount)
                return "index"
            }
            Files.isRegularFile(file) -> {
                response.contentType = MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE
                response.setHeader("Content-Disposition", "attachment; filename=${UriEncoder.encode(file.fileName.toString())}")
                Files.copy(file, response.outputStream)
                return null
            }
            Files.notExists(file) -> throw FileNotFoundException(file.toString())
            else -> throw IllegalStateException()
        }
    }

    @PostMapping
    fun save(request: HttpServletRequest, @RequestPart file: MultipartFile): String {
        val path = request.servletPath.substring(1)
        var dest = basePath.resolve(path).resolve(file.originalFilename)
        var i = 0
        while (Files.exists(dest)) {
            dest = dest.resolveSibling("${file.originalFilename}.${++i}")
        }
        file.transferTo(dest.toFile())
        return "redirect:/${UriEncoder.encode(path)}"
    }

    @ExceptionHandler(Exception::class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleError(e: Exception): String {
        println(e.toString())
        return "NOT FOUND"
    }

    private fun crumb(count: Int): String {
        val str = StringBuilder()
        for (i in 0 until count) {
            str.append("../")
        }
        return str.toString()
    }
}
