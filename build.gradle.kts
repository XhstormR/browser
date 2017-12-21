import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.run.BootRunTask

buildscript {
    repositories {
        maven("http://maven.aliyun.com/nexus/content/groups/public/")
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${extra["kotlin_version"]}")
    }
}

repositories {
    maven("http://maven.aliyun.com/nexus/content/groups/public/")
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compile("org.springframework.boot:spring-boot-starter-thymeleaf")
    compile("org.springframework.boot:spring-boot-starter-actuator")
    compile("org.springframework.boot:spring-boot-devtools")
}

version = "1.0-SNAPSHOT"

plugins {
    idea
    application
    kotlin("jvm") version "1.2.10"
    id("org.springframework.boot") version "1.5.9.RELEASE"
}

task<Wrapper>("wrapper") {
    gradleVersion = "4.4.1"
    distributionType = Wrapper.DistributionType.ALL
}

tasks.withType<Jar> {
    baseName = "browse"
    version = ""
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

//应用于 bootRun
tasks.withType<BootRunTask> {
    addResources = true
    jvmArgs = listOf("-Dconfig.enable_upload=true")
}

//应用于 run
application {
    mainClassName = "com.xhstormr.browse.Application"
    applicationDefaultJvmArgs = listOf(
            "-Dspring.profiles.active=dev",
            "-Dconfig.enable_upload=true")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
