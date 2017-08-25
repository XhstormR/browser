package com.xhstormr.browse.entity

import java.nio.file.Files
import java.nio.file.Path
import java.text.NumberFormat
import java.util.*

data class PathWrapper(private val path: Path) {
    val isDir = Files.isDirectory(path)

    val date = Date.from(Files.getLastModifiedTime(path).toInstant())

    val size = Files.size(path)

    val name = path.fileName.toString()

    val link = "./$name".run { return@run if (isDir) this.plus('/') else this }

    companion object {
        private val numberFormat = NumberFormat.getInstance().apply {
            maximumFractionDigits = 1
            minimumFractionDigits = 1
        }
    }

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
