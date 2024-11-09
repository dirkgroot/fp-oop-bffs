plugins {
    kotlin("jvm") version "2.2.20"
}

group = "nl.avisi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(24)
    compilerOptions {
        freeCompilerArgs.addAll("-Xconsistent-data-class-copy-visibility")
    }
}

tasks.test {
    useJUnitPlatform()
}
