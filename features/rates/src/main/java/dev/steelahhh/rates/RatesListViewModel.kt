package dev.steelahhh.rates

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext

/*
 * Author: steelahhh
 * 2/1/20
 */

class RatesListViewModel(
    initialState: RatesListState
) : BaseMvRxViewModel<RatesListState>(initialState = initialState) {
    companion object : MvRxViewModelFactory<RatesListViewModel, RatesListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: RatesListState
        ): RatesListViewModel? {
            return RatesListViewModel(state)
        }
    }
}
