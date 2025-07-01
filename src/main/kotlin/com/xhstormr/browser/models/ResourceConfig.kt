package com.xhstormr.browser.models

import kotlinx.serialization.Serializable

@Serializable
data class ResourceConfig(
    val location: String,
    val enableUpload: Boolean,
)
