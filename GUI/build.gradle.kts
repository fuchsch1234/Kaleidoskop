import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    application
}

application {
    mainClassName = "$group.gui.Kaleidoskop"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    val retrofitVersion = "2.5.0"
    val koinVersion = "2.0.1"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-jackson:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.7")
    implementation("no.tornado:tornadofx:1.7.17")
    implementation("io.reactivex.rxjava2:rxjava:2.0.4")
    implementation("com.github.thomasnield:rxkotlinfx:2.2.2")
    implementation("org.koin:koin-core:$koinVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${rootProject.extra["junitVersion"]}")
    testImplementation("org.hamcrest:hamcrest:2.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${rootProject.extra["junitVersion"]}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
    sourceCompatibility = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}
