package dev.steelahhh.rates.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dev.steelahh.core.converters.MoshiBigDecimalConverter
import dev.steelahhh.rates.data.CurrencyRatesApi
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Named
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

/*
 * Author: steelahhh
 * 4/1/20
 */

@Module
object RatesListModule {

    @Provides
    @Named("BASE_URL")
    fun provideBaseUrl(): String = "https://revolut.duckdns.org/"

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(MoshiBigDecimalConverter)
        .build()

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(3L, TimeUnit.SECONDS)
        .readTimeout(1L, TimeUnit.SECONDS)
        .build()

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
        @Named("BASE_URL")
        url: String
    ): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()

    @Provides
    fun provideRetrofitApi(retrofit: Retrofit): CurrencyRatesApi = retrofit.create()
}
