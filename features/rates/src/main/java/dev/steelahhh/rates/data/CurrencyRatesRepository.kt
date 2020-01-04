package dev.steelahhh.rates.data

import dev.steelahhh.rates.di.RatesListScope
import javax.inject.Inject

/*
 * Author: steelahhh
 * 4/1/20
 */

@RatesListScope
class CurrencyRatesRepository @Inject constructor(
    private val api: CurrencyRatesApi
) {
    fun get(currency: String = DEFAULT_CURRENCY) = api.currencyRates(currency)

    companion object {
        private const val DEFAULT_CURRENCY = "EUR"
    }
}
