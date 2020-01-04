package dev.steelahhh.rates.domain

import dev.steelahhh.rates.data.CurrencyRatesResponse
import java.math.BigDecimal
import java.util.Currency

/*
 * Author: steelahhh
 * 4/1/20
 */

fun CurrencyRatesResponse.toDomain() = listOf(
    CurrencyRate(
        key = base,
        name = Currency.getInstance(base).displayName,
        coefficient = BigDecimal.ONE
    )
) + rates.entries.map { (key, value) ->
    CurrencyRate(key = key, name = Currency.getInstance(key).displayName, coefficient = value)
}
