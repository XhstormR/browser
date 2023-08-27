import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

version = "2.0-SNAPSHOT"

plugins {
    idea
    application
    val kotlinVersion = libs.versions.kotlin
    kotlin("jvm") version kotlinVersion
    alias(libs.plugins.ktlint)
    alias(libs.plugins.ktor)
}

application {
    applicationName = project.name
    mainClass = "com.xhstormr.browser.ApplicationKt"
}

dependencies {
    implementation(libs.bundles.ktor)

    implementation(libs.logback.classic)
}

kotlin {
    jvmToolchain(11)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
        }
    }

    withType<JavaCompile> {
        with(options) {
            encoding = Charsets.UTF_8.name()
            isFork = true
            isIncremental = true
        }
    }

    withType<Wrapper> {
        gradleVersion = libs.versions.gradle.get()
        distributionType = Wrapper.DistributionType.ALL
    }
}
