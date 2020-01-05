package dev.steelahhh.rates.presentation

import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.PersistState
import java.math.BigDecimal

/*
 * Author: steelahhh
 * 2/1/20
 */

data class RatesListState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    @PersistState
    val convertValue: BigDecimal = BigDecimal.valueOf(100L),
    @PersistState
    val currency: String = "EUR",
    val rates: List<CurrencyRateUi> = emptyList()
) : MvRxState
