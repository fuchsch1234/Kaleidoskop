plugins {
    val kotlinVersion = "1.3.31"
    id("base")
    kotlin("jvm") version kotlinVersion apply false
    kotlin("plugin.jpa") version kotlinVersion apply false
    kotlin("plugin.spring") version kotlinVersion apply false
}

buildscript {
    val kotlinVersion by extra { "1.3.31" }
    val junitVersion by extra { "5.1.0" }
}

allprojects {
    group = "de.fuchsch.kaleidoskop"
    version = "0.1.0-RC"

    repositories {
        mavenCentral()
        jcenter()
    }
}

dependencies {
    subprojects.forEach {
        archives(it)
    }
}
