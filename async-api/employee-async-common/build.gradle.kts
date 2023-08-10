dependencies {
    implementation(kotlin("stdlib"))

    api("com.fasterxml.jackson.core:jackson-databind")
    api("org.springframework.kafka:spring-kafka")

    api(project(":async-api:employee-async-api"))
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}
