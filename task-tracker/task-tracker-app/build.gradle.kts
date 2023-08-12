import com.gorylenko.GitPropertiesPluginExtension

plugins {
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("com.palantir.git-version")
    id("com.palantir.docker")
    id("com.gorylenko.gradle-git-properties")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation(kotlin("stdlib"))

    api(project(":task-tracker:task-tracker-api"))
    api(project(":async-api:employee-async-common"))
    api(project(":async-api:task-async-common"))

    // SPRING-BOOT
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // SPRING-KAFKA
    implementation("org.springframework.kafka:spring-kafka")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("com.vladmihalcea:hibernate-types-55")

    // JACKSON
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation(project(":headers-resolver:web-spring-boot-starter"))

    // SWAGGER
    implementation("org.springdoc:springdoc-openapi-ui")
    implementation("org.springdoc:springdoc-openapi-webmvc-core")
    implementation("org.springdoc:springdoc-openapi-kotlin")

    // LOG
    implementation("com.frimastudio:slf4j-kotlin-extensions")
    implementation("net.logstash.logback:logstash-logback-encoder")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

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
