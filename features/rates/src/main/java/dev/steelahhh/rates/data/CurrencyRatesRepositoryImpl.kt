package dev.steelahhh.rates.data

import dev.steelahhh.rates.di.RatesListScope
import io.reactivex.Single
import javax.inject.Inject

/*
 * Author: steelahhh
 * 4/1/20
 */

@RatesListScope
class CurrencyRatesRepositoryImpl @Inject constructor(
    private val api: CurrencyRatesApi
) : CurrencyRatesRepository {
    override fun get(currency: String): Single<CurrencyRatesResponse> = api.currencyRates(currency)

    /**
     * This potentially could be stored in SharedPreferences or something similar
     */
    override val pollingRate: Long = 1000L
}
