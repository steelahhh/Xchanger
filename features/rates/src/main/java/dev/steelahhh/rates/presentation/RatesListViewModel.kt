package dev.steelahhh.rates.presentation

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.ViewModelContext
import dev.steelahh.core.injector
import dev.steelahhh.rates.data.CurrencyRatesRepository
import dev.steelahhh.rates.di.DaggerRatesListComponent
import dev.steelahhh.rates.domain.CurrencyRate
import dev.steelahhh.rates.domain.toDomain
import dev.steelahhh.rates.domain.toUi
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import java.math.BigDecimal
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

    init {
        logStateChanges()
    }

    fun startObserving() = withState { state ->
        if (state.convertValue != BigDecimal.ZERO) {
            disposables += currencyRatesRepository.get(state.currency)
                .map { it.getOrThrow().toDomain() }
                .retry { _ ->
                    /**
                     * Error handling could be split into different case, i.e. Network, API, etc
                     */
                    setState { copy(isError = true, isLoading = false) }
                    true
                }
                .repeatWhen { it.delay(currencyRatesRepository.pollingRate, TimeUnit.MILLISECONDS) }
                .toObservable()
                .execute {
                    copy(
                        isError = it is Error,
                        isLoading = it is Loading,
                        rates = if (it is Success) createList(it()) else state.rates
                    )
                }
                .disposeOnClear()
        } else {
            setState { copy(rates = recreateList()) }
        }
    }

    fun stopObserving() = disposables.clear()

    fun valueChanged(value: BigDecimal) = resubscribe {
        setState {
            copy(
                convertValue = value,
                rates = if (value == BigDecimal.ZERO) rates.map { it.copy(value = "") } else rates
            )
        }
    }

    fun changeCurrency(newCurrency: String, value: String) = resubscribe {
        setState {
            copy(
                currency = newCurrency,
                convertValue = if (value.isEmpty()) BigDecimal.ZERO else BigDecimal(value),
                rates = rates.map { rate -> rate.copy(isEditable = rate.key == newCurrency) }
            )
        }
    }

    private fun resubscribe(action: () -> Unit) {
        stopObserving()
        action()
        startObserving()
    }

    private fun RatesListState.createList(items: List<CurrencyRate>?): List<CurrencyRateUi> {
        if (items.isNullOrEmpty()) return emptyList()

        val baseCurrency = items.find { it.key == currency } ?: return emptyList()

        return (listOf(baseCurrency) + (items - baseCurrency).sortedBy { it.key }).map { rate ->
            rate.toUi(convertValue, rate.key == currency)
        }
    }

    private fun RatesListState.recreateList(): List<CurrencyRateUi> {
        val baseCurrency = rates.find { it.key == currency } ?: return emptyList()
        return (listOf(baseCurrency) + (rates - baseCurrency).sortedBy { it.key })
    }

    companion object : MvRxViewModelFactory<RatesListViewModel, RatesListState> {
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
