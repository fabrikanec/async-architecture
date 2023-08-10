dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":headers-resolver:headers"))
    implementation("org.springframework:spring-webmvc")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("io.mockk:mockk")
    testImplementation("org.assertj:assertj-core")
}
