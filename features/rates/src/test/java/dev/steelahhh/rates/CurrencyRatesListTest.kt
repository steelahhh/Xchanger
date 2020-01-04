package dev.steelahhh.rates

import com.airbnb.mvrx.test.MvRxTestRule
import dev.steelahhh.rates.data.CurrencyRatesApi
import dev.steelahhh.rates.data.CurrencyRatesRepository
import dev.steelahhh.rates.data.CurrencyRatesResponse
import dev.steelahhh.rates.presentation.RatesListState
import dev.steelahhh.rates.presentation.RatesListViewModel
import io.reactivex.Single
import org.junit.ClassRule
import org.junit.Test

class CurrencyRatesListTest {
    private val repository = CurrencyRatesRepository(object : CurrencyRatesApi {
        override fun currencyRates(baseCurrency: String): Single<CurrencyRatesResponse> {
            return Single.just(CurrencyRatesResponse(baseCurrency, hashMapOf()))
        }
    })

    val vm = RatesListViewModel(
        RatesListState(),
        repository
    )

    @Test
    fun `This should pass`() {
        assert(true)
    }

    companion object {
        @JvmField
        @ClassRule
        val rule = MvRxTestRule() }
}
