buildscript {
    repositories {
        mavenCentral()
        google()
        jcenter()
        maven("https://www.jitpack.io")
        maven("https://maven.fabric.io/public")
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(Libs.androidGradlePlugin)
        classpath(Libs.kotlinPlugin)
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()
        maven("https://www.jitpack.io")
        maven("https://plugins.gradle.org/m2/")
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
