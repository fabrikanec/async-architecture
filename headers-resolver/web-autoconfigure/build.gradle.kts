dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":headers-resolver:web"))
    implementation("org.springframework:spring-webmvc")
    annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
}
