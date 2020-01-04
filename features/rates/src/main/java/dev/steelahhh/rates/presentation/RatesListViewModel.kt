package dev.steelahhh.rates.presentation

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dev.steelahh.core.injector
import dev.steelahhh.rates.data.CurrencyRatesRepository
import dev.steelahhh.rates.di.DaggerRatesListComponent
import dev.steelahhh.rates.domain.toDomain
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import java.util.concurrent.TimeUnit

/*
 * Author: steelahhh
 * 2/1/20
 */

class RatesListViewModel(
    initialState: RatesListState,
    private val currencyRatesRepository: CurrencyRatesRepository
) : BaseMvRxViewModel<RatesListState>(initialState = initialState) {

    private var disposables = CompositeDisposable()

    fun onStart() = withState { state ->
        disposables += currencyRatesRepository.get(state.currency).map { it.toDomain() }
            .retry()
            .repeatWhen { it.delay(POLLING_RATE, TimeUnit.MILLISECONDS) }
            .toObservable()
            .execute {
                copy(isLoading = it is Loading)
            }
            .disposeOnClear()
    }

    fun onStop() = disposables.clear()

    fun changeCurrency(newCurrency: String) = resubscribe {
        setState {
            copy(currency = newCurrency)
        }
    }

    private fun resubscribe(action: () -> Unit) {
        onStop()
        action()
        onStart()
    }

    companion object : MvRxViewModelFactory<RatesListViewModel, RatesListState> {
        private const val POLLING_RATE = 1000L

        override fun create(
            viewModelContext: ViewModelContext,
            state: RatesListState
        ): RatesListViewModel? {
            val fragment = (viewModelContext as FragmentViewModelContext).fragment
            val component = DaggerRatesListComponent.factory().create(fragment.injector)
            return RatesListViewModel(
                state,
                component.repository
            )
        }
    }
}
