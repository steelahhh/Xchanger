plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}
android {
    compileSdkVersion(Config.targetSdk)

    defaultConfig {
        applicationId = Config.applicationId
        minSdkVersion(Config.minSdk)
        targetSdkVersion(Config.targetSdk)
        versionCode = Config.versionCode
        versionName = Config.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    androidExtensions {
        isExperimental = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    arrayOf(
        Modules.core
    ).forEach { module ->
        implementation(project(module))
    }

    arrayOf(
        Libs.kotlin,
        Libs.coreKtx,
        Libs.appcompat,
        Libs.materialComponents,
        Libs.constraintLayout,
        Libs.adapterDelegates,
        Libs.mvrx,
        Libs.rxJava,
        Libs.rxKotlin,
        Libs.rxAndroid,
        Libs.retrofit,
        Libs.daggerCore
    ).forEach { dep ->
        implementation(dep)
    }

    kapt(Libs.daggerCompiler)

    testImplementation(Libs.junit)
    androidTestImplementation(Libs.junitExtensions)
    androidTestImplementation(Libs.espresso)
}
