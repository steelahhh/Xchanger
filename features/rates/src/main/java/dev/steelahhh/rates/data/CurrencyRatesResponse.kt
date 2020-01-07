package dev.steelahhh.rates.data

import com.squareup.moshi.JsonClass
import java.math.BigDecimal

/*
 * Author: steelahhh
 * 4/1/20
 */

@JsonClass(generateAdapter = true)
data class CurrencyRatesResponse(
    val base: String?,
    val rates: Map<String, BigDecimal>?
)
