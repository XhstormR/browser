package com.xhstormr.browser

import io.ktor.http.ContentDisposition

fun attachmentHeaders(fileName: String) = ContentDisposition.Attachment
    .withParameter(ContentDisposition.Parameters.FileName, fileName)
    .withParameter(ContentDisposition.Parameters.FileNameAsterisk, fileName.toByteArray().toString(Charsets.ISO_8859_1))
    .toString()
