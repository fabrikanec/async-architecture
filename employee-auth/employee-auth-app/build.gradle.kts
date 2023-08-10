import com.gorylenko.GitPropertiesPluginExtension

plugins {
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("kapt")
    id("org.springframework.boot")
    id("com.palantir.git-version")
    id("com.palantir.docker")
    id("com.gorylenko.gradle-git-properties")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":async-api:employee-async-common"))
    implementation(project(":employee-auth:employee-auth-api"))
    implementation(project(":employee-auth:employee"))
    implementation(project(":employee-auth:employee-auth-token:employee-auth-token-api"))
    implementation(project(":employee-auth:employee-auth-token:employee-auth-token-support-starter"))
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure")

    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // SPRING-KAFKA
    implementation("org.springframework.kafka:spring-kafka")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    kapt("org.hibernate:hibernate-jpamodelgen")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("com.vladmihalcea:hibernate-types-55")

    implementation("org.springdoc:springdoc-openapi-ui")
    implementation("org.springdoc:springdoc-openapi-webmvc-core")
    implementation("org.springdoc:springdoc-openapi-kotlin")

    implementation("net.logstash.logback:logstash-logback-encoder")
    implementation("com.frimastudio:slf4j-kotlin-extensions")

    implementation("commons-codec:commons-codec")
    implementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("io.mockk:mockk")
    testImplementation("com.ninja-squad:springmockk")
    testImplementation("org.testcontainers:postgresql")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

configure<GitPropertiesPluginExtension> {
    dateFormat = "yyyy-MM-dd'T'HH:mmZ"
    keys = listOf(
        "git.branch",
        "git.commit.id",
        "git.commit.id.abbrev",
        "git.commit.time",
        "git.tags",
        "git.closest.tag.name",
        "git.closest.tag.commit.count",
        "git.total.commit.count"
    )
}

tasks {
    docker {
        val dockerName = project.name
        name = "$dockerName:$version"
        tag("latest", "$dockerName:latest")
        tag("version", "$dockerName:$version")
        noCache(true)
    }
}

springBoot {
    buildInfo()
}
