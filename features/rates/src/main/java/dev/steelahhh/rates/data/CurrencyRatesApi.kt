package dev.steelahhh.rates.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/*
 * Author: steelahhh
 * 4/1/20
 */

interface CurrencyRatesApi {
    @GET("latest")
    fun currencyRates(@Query("base") baseCurrency: String): Single<CurrencyRatesResponse>
}
