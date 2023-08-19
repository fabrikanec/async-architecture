pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val gradleDependencyManagementVersion: String by settings
    val detektVersion: String by settings
    val ktlintVersion: String by settings
    val gitPropertiesVersion: String by settings
    val palantirDockerVersion: String by settings
    val palantirGitVersion: String by settings
    val jGitVerVersion: String by settings
    val openapiGeneratorVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
        kotlin("kapt") version kotlinVersion apply false
        kotlin("plugin.spring") version kotlinVersion apply false
        kotlin("plugin.jpa") version kotlinVersion apply false
        `maven-publish`
        id("org.springframework.boot") version springBootVersion apply false
        id("io.spring.dependency-management") version gradleDependencyManagementVersion apply false
        id("io.gitlab.arturbosch.detekt") version detektVersion apply false
        id("org.jlleitschuh.gradle.ktlint") version ktlintVersion apply false
        id("com.palantir.git-version") version palantirGitVersion apply false
        id("com.palantir.docker") version palantirDockerVersion apply false
        id("com.gorylenko.gradle-git-properties") version gitPropertiesVersion apply false
        id("jacoco")
        id("fr.brouillard.oss.gradle.jgitver") version jGitVerVersion
        id("org.openapi.generator") version openapiGeneratorVersion apply false
    }

    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "task-management-system"

include(
    "task-tracker:task-tracker-api",
    "task-tracker:task-tracker-app",
    "accounting:accounting-api",
    "accounting:accounting-app",
    "api-gateway",
    "async-api:employee-async-api",
    "async-api:employee-async-common",
    "async-api:task-async-api",
    "async-api:task-async-common",
    "employee-auth:employee-auth-api",
    "employee-auth:employee",
    "employee-auth:employee-auth-app",
    "employee-auth:employee-auth-token:employee-auth-token-api",
    "employee-auth:employee-auth-token:employee-auth-token-support",
    "employee-auth:employee-auth-token:employee-auth-token-support-starter",
    "headers-resolver:headers",
    "headers-resolver:web",
    "headers-resolver:web-autoconfigure",
    "headers-resolver:web-spring-boot-starter",
)
