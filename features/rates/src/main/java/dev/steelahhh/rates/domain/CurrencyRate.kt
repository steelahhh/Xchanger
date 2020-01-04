package dev.steelahhh.rates.domain

import java.math.BigDecimal

/*
 * Author: steelahhh
 * 4/1/20
 */

data class CurrencyRate(
    val key: String,
    val name: String,
    val coefficient: BigDecimal
)
