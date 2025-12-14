// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.0.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
}

// Force Gradle to use JDK 17 for compilation
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_17.toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
}

// Add a validation task to ensure JDK 17 is being used
tasks.register("validateJdk") {
    doLast {
        val javaVersion = System.getProperty("java.version")
        if (!javaVersion.startsWith("17")) {
            throw GradleException("This project requires JDK 17. Current JDK: $javaVersion")
        } else {
            println("Using correct JDK: $javaVersion")
        }
    }
}

// Make all tasks depend on the JDK validation
tasks.whenTaskAdded {
    if (name != "validateJdk") {
        dependsOn("validateJdk")
    }
}