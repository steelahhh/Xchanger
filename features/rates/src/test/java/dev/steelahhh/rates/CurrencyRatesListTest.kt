package dev.steelahhh.rates

import com.airbnb.mvrx.test.MvRxTestRule
import com.airbnb.mvrx.withState
import dev.steelahhh.rates.data.CurrencyRatesRepository
import dev.steelahhh.rates.data.CurrencyRatesResponse
import dev.steelahhh.rates.data.PollingRate
import dev.steelahhh.rates.domain.DEFAULT_MATH_CONTEXT
import dev.steelahhh.rates.domain.formatRate
import dev.steelahhh.rates.presentation.RatesListState
import dev.steelahhh.rates.presentation.RatesListViewModel
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single.just
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import kotlin.Result.Companion.success
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test

class CurrencyRatesListTest {
    private val repository = mockk<CurrencyRatesRepository>()

    lateinit var vm: RatesListViewModel

    @Before
    fun setup() {
        every { repository.pollingRate }.answers { PollingRate(100L) }
    }

    @Test
    fun `Should start with default state`() {
        setupCurrencyResult(EUR_CURRENCY)
        vm = RatesListViewModel(RatesListState(), repository)
        withState(vm) { state ->
            assert(state.isLoading)
            assert(state.convertValue == BigDecimal(100))
            assert(state.currency == EUR_CURRENCY)
        }
    }

    @Test
    fun `startObserving fetches the rates`() {
        setupCurrencyResult(EUR_CURRENCY)
        val ts = setupTestScheduler()
        vm = RatesListViewModel(RatesListState(), repository)

        vm.startObserving()
        ts.advanceTimeBy(repository.pollingRate.time, TimeUnit.MILLISECONDS)
        vm.stopObserving()

        withState(vm) { state ->
            assert(state.rates.isNotEmpty())
            assert(state.isLoading.not())
        }
    }

    @Test
    fun `Rates are calculated properly`() {
        val ts = setupTestScheduler()
        setupCurrencyResult(EUR_CURRENCY)
        vm = RatesListViewModel(RatesListState(), repository)

        vm.startObserving()
        ts.advanceTimeBy(repository.pollingRate.time, TimeUnit.MILLISECONDS)
        vm.stopObserving()

        withState(vm) { state ->
            val manuallyCalculatedRates = staticRatesForEuro.values.map {
                (it * state.convertValue).formatRate(DEFAULT_MATH_CONTEXT)
            }

            val stateRatesWithoutBase = state.rates.filter { !it.isEditable }.map { it.value }

            assert(stateRatesWithoutBase == manuallyCalculatedRates)
        }
    }

    @Test
    fun `Currency change changes the currency in VM state`() {
        val ts = setupTestScheduler()
        setupCurrencyResult(EUR_CURRENCY)
        setupCurrencyResult(CAD_CURRENCY)
        vm = RatesListViewModel(RatesListState(), repository)

        // Load the initial rates
        vm.startObserving()
        ts.advanceTimeBy(repository.pollingRate.time, TimeUnit.MILLISECONDS)

        // Change the currency
        val newCurrency = CAD_CURRENCY
        withState(vm) { state ->
            vm.changeCurrency(newCurrency, state.rates.find { it.key == newCurrency }!!.value)
        }

        vm.stopObserving()

        withState(vm) { state ->
            assert(state.currency == newCurrency)
            assert(state.rates.first().key == newCurrency)
        }
    }

    @Test
    fun `When value changes, rates in state update`() {
        val ts = setupTestScheduler()
        setupCurrencyResult(EUR_CURRENCY)
        vm = RatesListViewModel(RatesListState(), repository)

        // Load the initial rates
        vm.startObserving()
        ts.advanceTimeBy(repository.pollingRate.time, TimeUnit.MILLISECONDS)
        vm.stopObserving()

        var previous: List<String> = emptyList()

        // Change the value
        val newValue = BigDecimal("200")
        withState(vm) { state ->
            previous = state.rates.map { it.value }
            vm.valueChanged(newValue)
        }

        vm.stopObserving()

        // Check that the value is changed and rates are different
        withState(vm) { state ->
            val newRates = state.rates.map { it.value }
            assert(previous.isNotEmpty())
            assert(state.convertValue == newValue)
            assert(newRates != previous)
        }
    }

    @Test
    fun `Check that all the rates are empty if the new value is zero`() {
        val ts = setupTestScheduler()
        setupCurrencyResult(EUR_CURRENCY)
        vm = RatesListViewModel(RatesListState(), repository)

        // Load the initial rates
        vm.startObserving()
        ts.advanceTimeBy(repository.pollingRate.time, TimeUnit.MILLISECONDS)
        vm.stopObserving()

        // Change the value
        val newValue = BigDecimal("0")
        withState(vm) { vm.valueChanged(newValue) }

        vm.stopObserving()

        withState(vm) { state ->
            assert(state.convertValue == newValue)
            assert(state.rates.all { it.value.isEmpty() })
        }
    }

    private fun setupTestScheduler(): TestScheduler {
        val testScheduler = TestScheduler()
        RxJavaPlugins.reset()
        RxJavaPlugins.setNewThreadSchedulerHandler { testScheduler }
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
        RxJavaPlugins.setSingleSchedulerHandler { testScheduler }
        return testScheduler
    }

    private fun setupCurrencyResult(currency: String) {
        every { repository.get(currency) }.answers {
            just(
                success(
                    CurrencyRatesResponse(
                        base = currency,
                        rates = when (currency) {
                            CAD_CURRENCY -> staticRatesForCad
                            else -> staticRatesForEuro
                        }
                    )
                )
            )
        }
    }

    companion object {
        @JvmField
        @ClassRule
        val rule = MvRxTestRule()

        private const val EUR_CURRENCY = "EUR"
        private const val CAD_CURRENCY = "CAD"

        val staticRatesForEuro: LinkedHashMap<String, BigDecimal> = linkedMapOf(
            "AUD" to BigDecimal("1.6139"),
            "BGN" to BigDecimal("1.9528"),
            "BRL" to BigDecimal("4.7845"),
            "CAD" to BigDecimal("1.5315"),
            "CHF" to BigDecimal("1.1258"),
            "CNY" to BigDecimal("7.9329"),
            "CZK" to BigDecimal("25.676"),
            "DKK" to BigDecimal("7.4453")
        )

        val staticRatesForCad: LinkedHashMap<String, BigDecimal> = linkedMapOf(
            "AUD" to BigDecimal("1.0545"),
            "BGN" to BigDecimal("1.2758"),
            "BRL" to BigDecimal("3.1258"),
            "CHF" to BigDecimal("0.7355"),
            "CNY" to BigDecimal("5.1828"),
            "CZK" to BigDecimal("16.775"),
            "DKK" to BigDecimal("4.8642"),
            "GBP" to BigDecimal("0.58595")
        )
    }
}
