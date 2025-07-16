package com.xhstormr.browser.models

import java.nio.file.Path
import java.text.NumberFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.io.path.extension
import kotlin.io.path.fileSize
import kotlin.io.path.getLastModifiedTime
import kotlin.io.path.isDirectory
import kotlin.io.path.name

data class PathWrapper(val path: Path) : Comparable<PathWrapper> {

    val name = path.name

    val isDir = path.isDirectory()

    val size = path.fileSize()

    val time = path.getLastModifiedTime().toInstant()

    val timeFormatted = timeFormatter.format(time)

    val sizeFormatted
        get(): String {
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
            val num = numberFormat.format(size * 1.0 / divisor)
            return "$num $unit"
        }

    companion object {
        private val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault())

        private val numberFormat = NumberFormat.getInstance()
            .apply { maximumFractionDigits = 1 }
    }

    override fun compareTo(other: PathWrapper) =
        compareValuesBy(other, this, { it.isDir }, { it.path.extension }, { it.name })
}
