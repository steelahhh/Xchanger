object Libs {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.agp}"

    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val junitExtensions = "androidx.test.ext:junit:${Versions.junitExtensions}"
    const val junit = "junit:junit:${Versions.junit}"

    const val appcompat = "androidx.appcompat:appcompat:${Versions.aacStable}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.aacStable}"
    const val materialComponents =
        "com.google.android.material:material:${Versions.materialComponents}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val adapterDelegates =
        "com.hannesdorfmann:adapterdelegates4-kotlin-dsl:${Versions.adapterDelegates}"

    const val daggerCore = "com.google.dagger:dagger:${Versions.dagger}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"
    const val moshiRetrofitAdapter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    const val mvrx = "com.airbnb.android:mvrx:${Versions.mvrx}"
}
