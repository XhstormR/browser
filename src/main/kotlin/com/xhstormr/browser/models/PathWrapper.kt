package com.xhstormr.browser.models

import java.nio.file.Path
import java.text.NumberFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.io.path.fileSize
import kotlin.io.path.getLastModifiedTime
import kotlin.io.path.isDirectory
import kotlin.io.path.name

data class PathWrapper(val path: Path) {

    val isDir = path.isDirectory()

    val time = path.getLastModifiedTime().toInstant()

    val size = path.fileSize()

    val name = path.name

    companion object {

        private val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault())

        private val numberFormat = NumberFormat.getInstance()
            .apply { maximumFractionDigits = 1 }
    }

    fun formatTime() = timeFormatter.format(time)

    fun formatSize(): String {
        val divisor: Int
        val unit: String
        when {
            size > 1024 * 1024 * 1024 -> {
                divisor = 1024 * 1024 * 1024
                unit = "GB"
            }

            size > 1024 * 1024 -> {
                divisor = 1024 * 1024
                unit = "MB"
            }

            size > 1024 -> {
                divisor = 1024
                unit = "KB"
            }

            else -> {
                divisor = 1
                unit = "B"
            }
        }
        return "${numberFormat.format(size * 1.0 / divisor)} $unit"
    }
}
