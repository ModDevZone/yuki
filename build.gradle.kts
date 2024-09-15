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
    //jcenter()
    maven {
        name = "m2-dv8tion"
        url = uri("https://m2.dv8tion.net/releases")
    }
    maven {
        name = "jda-chewtils"
        url = uri("https://m2.chew.pro/snapshots")
    }
    maven {
        name = "fabric"
        url  = uri("https://maven.fabricmc.net")
    }
    maven {
        name = "quilt"
        url = uri("https://maven.quiltmc.org/repository/snapshot")
    }
    maven {
        name = "jitpack"
        url = uri("https://jitpack.io")
    }
}

dependencies {
    // ----- Core Bot Dependencies ----- //
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("pw.chew:jda-chewtils:2.0-SNAPSHOT")
    implementation("com.google.guava:guava:32.0.1-jre")

    // ----- Logging & Configuration ----- //
    implementation("com.electronwill.night-config:toml:3.6.5")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // ----- Database & Storage ----- //
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("org.flywaydb:flyway-core:8.4.1")
    implementation("org.jdbi:jdbi3-core:3.24.1")
    implementation("org.jdbi:jdbi3-sqlobject:3.24.1")
    compileOnly("org.jetbrains:annotations:23.0.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.isIncremental = true

    // Set this to the version of Java you want to use,
    // the minimum required for JDA is 1.8
    sourceCompatibility = "21"
    targetCompatibility = "21"
}