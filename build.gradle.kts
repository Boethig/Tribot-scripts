import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        maven {
            setUrl("https://gitlab.com/api/v4/projects/22245399/packages/maven")
        }
    }
    dependencies {
        classpath("org.tribot.wastedbro:tribot-gradle-plugin:+")
    }
}

plugins {
    java
    kotlin("jvm") version "1.4.10"
    id("org.openjfx.javafxplugin") version "0.0.9"
    id("io.freefair.lombok") version "5.3.0"
}

group = "org.tribot.wastedbro"
version = "1.0.0"

val baseDir = projectDir

allprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "org.openjfx.javafxplugin")
    apply(plugin = "io.freefair.lombok")

    repositories {
        jcenter()
        // Tribot Central
        maven {
            setUrl("https://gitlab.com/api/v4/projects/20741387/packages/maven")
        }
    }

    dependencies {
        api(files("${baseDir.absolutePath}/allatori-annotations-7.5.jar"))
        api("org.tribot:tribot-client:+")
    }

    sourceSets {
        main {
            java {
                setSrcDirs(listOf("src"))
            }
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    configurations.all {
        // Ensures that our dependencies will update timely
        resolutionStrategy.cacheDynamicVersionsFor(5, "minutes")
        resolutionStrategy.cacheChangingModulesFor(5, "minutes")
    }

    javafx {
        modules("javafx.controls", "javafx.fxml", "javafx.swing", "javafx.web")
    }
}

val repoCopy = tasks.create("repoCopy") {
    group = "tribot"
    subprojects.forEach {
        it.tasks["build"]?.let { this.dependsOn(it) }
    }
    doLast {
        val repoDeployDir = this.project.projectDir.resolve("build/repo-deploy").also { it.mkdirs() }
        allprojects
                .filter { it.path != project.path }
                .map { it.projectDir.resolve("build/repo-deploy") }
                .filter { it.exists() }
                .mapNotNull { it.listFiles()?.getOrNull(0) }
                .forEach { it.copyTo(repoDeployDir.resolve(it.name), overwrite = true) }
    }
}

tasks.build {
    finalizedBy(repoCopy)
}
