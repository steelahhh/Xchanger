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
    implementation(project(Modules.core))

    arrayOf(
        Libs.kotlin,
        Libs.coreKtx,
        Libs.materialComponents,
        Libs.recyclerView,
        Libs.adapterDelegates,
        Libs.mvrx,
        Libs.moshi,
        Libs.moshiRetrofitAdapter,
        Libs.retrofitRxAdapter,
        Libs.rxJava,
        Libs.rxKotlin,
        Libs.rxAndroid,
        Libs.daggerCore
    ).forEach { dep ->
        implementation(dep)
    }

    kapt(Libs.daggerCompiler)
    kapt(Libs.moshiCodegen)

    testImplementation(Libs.mockk)
    testImplementation(Libs.junit)
    testImplementation(Libs.mvrxTesting)
    androidTestImplementation(Libs.junitExtensions)
    androidTestImplementation(Libs.espresso)
}
