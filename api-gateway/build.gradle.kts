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

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")

    implementation(project(":employee-auth:employee"))
    implementation(project(":employee-auth:employee-auth-api"))

    // SPRING-BOOT
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // SPRING-CLOUD
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")

    // SPRING-SECURITY
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure")

    implementation(project(":headers-resolver:headers"))

    // LOG
    implementation("com.frimastudio:slf4j-kotlin-extensions")
    implementation("net.logstash.logback:logstash-logback-encoder")

    // JACKSON
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("io.mockk:mockk")
    testImplementation("com.ninja-squad:springmockk")
    testImplementation("io.strikt:strikt-core")

    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
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
