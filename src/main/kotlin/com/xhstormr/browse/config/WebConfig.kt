package com.xhstormr.browse.config

import com.xhstormr.browse.interceptor.LoginInterceptor
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import java.nio.file.Path
import java.nio.file.Paths

@Configuration
@ConfigurationProperties("config")
open class WebConfig : WebMvcConfigurerAdapter() {
    lateinit var username: String
    lateinit var password: String
    lateinit var basePath: String
    lateinit var enableUpload: String

    @Bean
    open fun username(): String = username

    @Bean
    open fun password(): String = password

    @Bean
    open fun basePath(): Path = Paths.get(basePath)

    @Bean
    open fun enableUpload(): Boolean = enableUpload.toBoolean()

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry
                .addInterceptor(LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/assets/**")
    }
}
