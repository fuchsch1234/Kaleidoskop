import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm")
    kotlin("plugin.jpa")
    kotlin("plugin.spring")
    id("org.springframework.boot") version "2.1.4.RELEASE"
}

apply(plugin="io.spring.dependency-management")

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group="junit", module="junit")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api:${rootProject.extra["junitVersion"]}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${rootProject.extra["junitVersion"]}")
    testRuntimeOnly("com.h2database:h2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
    sourceCompatibility = "1.8"
}

springBoot {
    mainClassName = "${group}.server.Server"
}

tasks.withType<Test> {
    useJUnitPlatform()
}
