import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.wrapper.Wrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.run.BootRunTask

version = "1.0-SNAPSHOT"

plugins {
    idea
    application
}

apply {
    plugin("kotlin")
    plugin("org.springframework.boot")
}

task<Wrapper>("wrapper") {
    gradleVersion = "4.1"
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

buildscript {
    extra["kotlin_version"] = "+"
    extra["spring_version"] = "+"

    extra["thymeleaf.version"] = "+"
    extra["thymeleaf-layout-dialect.version"] = "+"

    repositories {
        maven { setUrl("http://maven.aliyun.com/nexus/content/groups/public/") }
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${extra["kotlin_version"]}")
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${extra["spring_version"]}")
    }
}

repositories {
    maven { setUrl("http://maven.aliyun.com/nexus/content/groups/public/") }
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jre8")
    compile("org.springframework.boot:spring-boot-starter-thymeleaf")
    compile("org.springframework.boot:spring-boot-devtools")
}
