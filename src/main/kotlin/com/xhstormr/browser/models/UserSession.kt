package com.xhstormr.browser.models

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val name: String,
    val loginAt: Long,
)
