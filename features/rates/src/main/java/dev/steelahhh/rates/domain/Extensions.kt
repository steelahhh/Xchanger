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

internal val DEFAULT_MATH_CONTEXT = MathContext(12, RoundingMode.HALF_EVEN)

fun CurrencyRatesResponse.toDomain() = (rates?.entries?.map { (key, value) ->
    CurrencyRate(
        key = key,
        name = Currency.getInstance(key).displayName,
        coefficient = value
    )
} ?: emptyList()) + getBaseCurrency()

private fun CurrencyRatesResponse.getBaseCurrency(): List<CurrencyRate> {
    if (base == null) return emptyList()
    return listOf(
        CurrencyRate(
            key = base,
            name = Currency.getInstance(base).displayName,
            coefficient = BigDecimal.ONE
        )
    )
}

fun CurrencyRate.toUi(
    baseValue: BigDecimal,
    isEditable: Boolean,
    mathContext: MathContext = DEFAULT_MATH_CONTEXT
) = CurrencyRateUi(
    key = key,
    name = name,
    value = (coefficient * baseValue).formatRate(mathContext),
    isEditable = isEditable
)

internal fun BigDecimal.formatRate(mathContext: MathContext): String = round(mathContext)
    .setScale(2, mathContext.roundingMode)
    .stripTrailingZeros()
    .toPlainString()
