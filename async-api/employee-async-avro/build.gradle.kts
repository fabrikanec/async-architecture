plugins {
    id("com.github.davidmc24.gradle.plugin.avro")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    api(project(":employee-auth:employee"))
}

tasks {
    generateAvroJava {
        source("src/avro")
        setOutputDir(file("generated/avro"))
    }
}
