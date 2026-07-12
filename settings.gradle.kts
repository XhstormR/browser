rootProject.name = "browser"

pluginManagement {
    repositories {
        maven("https://repo.huaweicloud.com/repository/maven/")
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://repo.huaweicloud.com/repository/maven/")
        mavenCentral()
    }
}
