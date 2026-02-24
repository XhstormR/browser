package com.xhstormr.browser.model

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val name: String,
    val loginAt: Long,
)
