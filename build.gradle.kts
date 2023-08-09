import com.palantir.gradle.docker.DockerExtension
import fr.brouillard.oss.gradle.plugins.JGitverPluginExtensionBranchPolicy
import fr.brouillard.oss.jgitver.Strategies
import io.gitlab.arturbosch.detekt.Detekt
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar


val kotlinVersion: String by project
val springBootVersion: String by project
val springCloudVersion: String by project
val springSecurityOauth2Version: String by project
val springSecurityCoreVersion: String by project
val springDocOpenApiVersion: String by project
val jacocoToolVersion: String by project
val openapiGeneratorVersion: String by project

val postgresVersion: String by project
val flywayVersion: String by project
val hibernateTypesVersion: String by project

val logstashLogbackEncoderVersion: String by project
val slf4jKotlinExtensionVersion: String by project

val testContainers: String by project
val junitPlatformLauncherVersion: String by project
val mockkVersion: String by project
val springMockkVersion: String by project

plugins {
    kotlin("jvm") apply false
    kotlin("plugin.spring") apply false
    kotlin("plugin.jpa") apply false
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management") apply false
    id("io.gitlab.arturbosch.detekt") apply false
    id("org.jlleitschuh.gradle.ktlint") apply false
    id("com.palantir.git-version") apply false
    id("com.palantir.docker") apply false
    id("com.gorylenko.gradle-git-properties") apply false

    id("fr.brouillard.oss.gradle.jgitver")
    id("jacoco")
    `maven-publish`
}

jgitver {
    strategy = Strategies.PATTERN
    versionPattern = "\${M}.\${m}.\${p}-\${meta.GIT_SHA1_8}\${-rc~meta.QUALIFIED_BRANCH_NAME}-SNAPSHOT"
    nonQualifierBranches = "master"
    policy(closureOf<JGitverPluginExtensionBranchPolicy> {
        pattern = "(feature.*)"
        transformations = listOf("IGNORE")
    })
    policy(closureOf<JGitverPluginExtensionBranchPolicy> {
        pattern = "(bugfix.*)"
        transformations = listOf("IGNORE")
    })
    policy(closureOf<JGitverPluginExtensionBranchPolicy> {
        pattern = "release(.*)"
        transformations = listOf("REPLACE_UNEXPECTED_CHARS_UNDERSCORE", "UPPERCASE")
    })
    policy(closureOf<JGitverPluginExtensionBranchPolicy> {
        pattern = "(dev)"
        transformations = listOf("IGNORE")
    })
}

allprojects {
    group = "com.taskmanagement"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
        mavenLocal()
    }
}

subprojects {
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "kotlin")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "jacoco")
    apply(plugin = "maven-publish")

    val jacocoCoverageFile = "$buildDir/jacocoReports/test/jacocoTestReport.xml"

    tasks.withType<JacocoReport> {
        reports {
            xml.apply {
                required.set(true)
                outputLocation.set(file(jacocoCoverageFile))
            }
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            events = setOf(
                TestLogEvent.FAILED,
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED
            )
            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
        }
        finalizedBy("jacocoTestReport")
    }

    val detektConfigFilePath = "$rootDir/gradle/detekt-config.yml"

    tasks.withType<Detekt> {
        exclude("resources/")
        exclude("build/")
        config.setFrom(detektConfigFilePath)
        buildUponDefaultConfig = true
    }

    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
            mavenBom("org.testcontainers:testcontainers-bom:$testContainers")
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion") {
                bomProperty("kotlin.version", kotlinVersion)
            }
        }
        dependencies {
            dependency("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

            dependency("org.springdoc:springdoc-openapi-ui:$springDocOpenApiVersion")
            dependency("org.springdoc:springdoc-openapi-webmvc-core:$springDocOpenApiVersion")
            dependency("org.springdoc:springdoc-openapi-security:$springDocOpenApiVersion")
            dependency("org.springdoc:springdoc-openapi-kotlin:$springDocOpenApiVersion")
            dependency("org.springdoc:springdoc-openapi-data-rest:$springDocOpenApiVersion")

            dependency("org.springframework.security.oauth:spring-security-oauth2:$springSecurityOauth2Version")
            dependency("org.springframework.security:spring-security-core:$springSecurityCoreVersion")

            dependency("org.flywaydb:flyway-core:$flywayVersion")
            dependency("org.postgresql:postgresql:$postgresVersion")
            dependency("com.vladmihalcea:hibernate-types-55:$hibernateTypesVersion")

            dependency("net.logstash.logback:logstash-logback-encoder:$logstashLogbackEncoderVersion")
            dependency("com.frimastudio:slf4j-kotlin-extensions:$slf4jKotlinExtensionVersion")

            dependency("org.openapitools:openapi-generator-gradle-plugin:$openapiGeneratorVersion")

            dependency("org.junit.platform:junit-platform-launcher:$junitPlatformLauncherVersion")
            dependency("io.mockk:mockk:$mockkVersion")
            dependency("com.ninja-squad:springmockk:$springMockkVersion")
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }

    jacoco {
        toolVersion = jacocoToolVersion
        reportsDirectory.set(file("$buildDir/jacocoReports"))
    }

    val sourcesJar by tasks.creating(Jar::class) {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        description = "Assembles sources JAR"
        archiveClassifier.set("sources")
        from(project.the<SourceSetContainer>()["main"].allSource)
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                versionMapping {
                    usage("java-api") {
                        fromResolutionOf("runtimeClasspath")
                    }
                    usage("java-runtime") {
                        fromResolutionResult()
                    }
                }
                from(components["java"])
                afterEvaluate {
                    artifact(sourcesJar)
                }
            }
        }
    }

    afterEvaluate {
        if (tasks.withType<BootJar>().isNotEmpty()) {
            tasks.withType<BootJar> {
                layered {}
            }

            val bootJar: BootJar by tasks
            bootJar.apply {
                archiveClassifier.set("application")
            }

            val explodeJar by tasks.register<JavaExec>("explodeJar") {
                group = "build"
                description = "Explodes layered fat jar into build/libs"

                dependsOn(bootJar)

                classpath(fileTree(bootJar.archiveFile))
                workingDir(mkdir(bootJar.destinationDirectory.dir("exploded")))
                jvmArgs("-Djarmode=layertools")
                args("extract")
                outputs.dir(bootJar.destinationDirectory.dir("exploded"))
            }

            tasks {
                configure<DockerExtension> {
                    files(explodeJar.outputs)
                    dependsOn(explodeJar)
                }
            }
        }
    }
}
