plugins {
    kotlin("multiplatform") version "1.7.10"
}

group = "me.xander"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
    sourceSets {
        val nativeMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-native-mt")
                // Core library
                implementation("org.gtk-kt:gtk:1.0.0-alpha1")

                // DSL library to make things easier to write
                implementation("org.gtk-kt:dsl:0.1.0-alpha0")

                // Extra libraries for convenience and interoperability with Kotlin
                implementation("org.gtk-kt:coroutines:0.1.0-alpha0")
                implementation("org.gtk-kt:ktx:0.1.0-alpha0")

                // Base libraries if you want to work with them standalone
                implementation("org.gtk-kt:cairo:0.1.0-alpha0")
                implementation("org.gtk-kt:gdk-pixbuf:0.1.0-alpha0")
                implementation("org.gtk-kt:gio:2.68.0-alpha0") // messed up versioning
                implementation("org.gtk-kt:glib:0.1.0-alpha1")
                implementation("org.gtk-kt:gobject:0.1.0-alpha1")
                implementation("org.gtk-kt:pango:0.1.0-alpha0")

            }
        }
        val nativeTest by getting
    }
}
