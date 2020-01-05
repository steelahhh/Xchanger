plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}
android {
    compileSdkVersion(Config.targetSdk)

    defaultConfig {
        minSdkVersion(Config.minSdk)
        targetSdkVersion(Config.targetSdk)
        versionCode = Config.versionCode
        versionName = Config.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    androidExtensions {
        isExperimental = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    arrayOf(
        Libs.kotlin,
        Libs.coreKtx,
        Libs.adapterDelegates,
        Libs.mvrx,
        Libs.moshi,
        Libs.moshiRetrofitAdapter,
        Libs.rxJava,
        Libs.rxAndroid,
        Libs.daggerCore
    ).forEach { dep ->
        implementation(dep)
    }

    kapt(Libs.daggerCompiler)

    testImplementation(Libs.junit)
    androidTestImplementation(Libs.junitExtensions)
    androidTestImplementation(Libs.espresso)
}
