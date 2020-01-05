package dev.steelahhh.rates.domain

import dev.steelahhh.rates.data.CurrencyRatesResponse
import dev.steelahhh.rates.presentation.CurrencyRateUi
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.Currency

/*
 * Author: steelahhh
 * 4/1/20
 */

fun CurrencyRatesResponse.toDomain() = rates.entries.map { (key, value) ->
    CurrencyRate(
        key = key,
        name = Currency.getInstance(key).displayName,
        coefficient = value
    )
} + CurrencyRate(
    key = base,
    name = Currency.getInstance(base).displayName,
    coefficient = BigDecimal.ONE
)

fun CurrencyRate.toUi(
    baseValue: BigDecimal,
    isEditable: Boolean,
    mathContext: MathContext = MathContext(12, RoundingMode.HALF_EVEN)
) = CurrencyRateUi(
    key = key,
    name = name,
    value = (coefficient * baseValue).round(mathContext)
        .apply { setScale(2, mathContext.roundingMode) }
        .toPlainString(),
    isEditable = isEditable
)
