package com.xhstormr.browse

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment

@PropertySource("classpath:/config.properties")
@SpringBootApplication
open class Application {
    @Bean
    open fun init(environment: Environment) = CommandLineRunner {
        println("正在监听 ${environment.getProperty("server.address", "0.0.0.0")}:${environment.getProperty("server.port")}")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}
