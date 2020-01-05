package dev.steelahhh.rates.presentation

/*
 * Author: steelahhh
 * 4/1/20
 */

data class CurrencyRateUi(
    val key: String,
    val name: String,
    val value: String,
    val isEditable: Boolean = false
)
