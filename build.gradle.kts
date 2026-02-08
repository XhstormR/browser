import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

version = "2.0-SNAPSHOT"

plugins {
    application
    val kotlinVersion = libs.versions.kotlin
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    alias(libs.plugins.ktor)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.native)
}

application {
    applicationName = project.name
    mainClass = "com.xhstormr.browser.ApplicationKt"
}

graalvmNative {
    metadataRepository {
        enabled = true
    }

    binaries.all {
        resources.autodetect()
    }
}

dependencies {
    implementation(libs.bundles.ktor)
    implementation(libs.logback.classic)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        compilerOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
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

/*
./gradlew -Pagent run &
./gradlew metadataCopy --task run/session-385-20230827T144641Z --dir src/main/resources/META-INF/native-image
./gradlew nativeCompile
*/
