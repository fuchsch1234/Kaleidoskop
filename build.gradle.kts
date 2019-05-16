plugins {
    id("base")
    kotlin("jvm") version "1.3.31" apply false
}

allprojects {
    group = "de.fuchsch.kaleidoskop"
    version = "0.1.0-RC"

    val kotlinVersion = "1.3.31"

    repositories {
        mavenCentral()
    }
}

dependencies {
    subprojects.forEach {
        archives(it)
    }
}
