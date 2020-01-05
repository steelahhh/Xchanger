package dev.steelahhh.rates.data

import io.reactivex.Single

/*
 * Author: steelahhh
 * 5/1/20
 */

interface CurrencyRatesRepository {
    fun get(currency: String): Single<CurrencyRatesResponse>
    val pollingRate: Long
}
