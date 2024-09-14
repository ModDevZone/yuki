plugins {
    java
    idea
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "zone.moddev"
version = "1.0"

application {
    applicationName = "yuki"
    mainClass = "zone.moddev.yuki.Yuki"
    applicationDefaultJvmArgs = listOf("--enable-preview")
    executableDir = "run"
}

val jdaVersion = "5.1.0"
val logbackVersion = "1.5.8"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.isIncremental = true

    // Set this to the version of Java you want to use,
    // the minimum required for JDA is 1.8
    sourceCompatibility = "21"
    targetCompatibility = "21"
}