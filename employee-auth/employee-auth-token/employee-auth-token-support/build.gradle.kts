dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":employee-auth:employee"))

    api(project(":employee-auth:employee-auth-token:employee-auth-token-api"))

    implementation("org.springframework.security.oauth:spring-security-oauth2")

    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("com.frimastudio:slf4j-kotlin-extensions")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("io.mockk:mockk")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
