package dev.steelahhh.rates.presentation

import com.airbnb.mvrx.MvRxState

/*
 * Author: steelahhh
 * 2/1/20
 */

data class RatesListState(
    val isLoading: Boolean = true,
    val currency: String = "EUR"
) : MvRxState
