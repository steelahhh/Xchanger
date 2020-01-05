package dev.steelahhh.rates.di

import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dev.steelahh.core.converters.MoshiBigDecimalConverter
import dev.steelahhh.rates.data.CurrencyRatesApi
import dev.steelahhh.rates.data.CurrencyRatesRepository
import dev.steelahhh.rates.data.CurrencyRatesRepositoryImpl
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
abstract class RatesListModule {
    @Module
    companion object {
        private const val CONNECT_TIMEOUT = 3000L
        private const val READ_TIMEOUT = 1000L
        @Provides
        @JvmStatic
        @Named("BASE_URL")
        fun provideBaseUrl(): String = "https://revolut.duckdns.org/"

        @Provides
        @JvmStatic
        fun provideMoshi(): Moshi = Moshi.Builder()
            .add(MoshiBigDecimalConverter)
            .build()

        @Provides
        @JvmStatic
        fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .build()

        @Provides
        @JvmStatic
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
        @JvmStatic
        fun provideRetrofitApi(retrofit: Retrofit): CurrencyRatesApi = retrofit.create()
    }

    @Binds
    abstract fun provideRepository(repository: CurrencyRatesRepositoryImpl): CurrencyRatesRepository
}
