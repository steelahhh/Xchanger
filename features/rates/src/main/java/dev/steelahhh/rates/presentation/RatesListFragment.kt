package dev.steelahhh.rates.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.snackbar.Snackbar
import dev.steelahh.core.setTextMaxLines
import dev.steelahhh.rates.R
import kotlinx.android.synthetic.main.fragment_rates.*

/*
 * Author: steelahhh
 * 2/1/20
 */

class RatesListFragment : BaseMvRxFragment(R.layout.fragment_rates) {
    private val viewModel: RatesListViewModel by fragmentViewModel()

    private val ratesAdapter by lazy {
        RatesListAdapter(::onCurrencySelected, viewModel::valueChanged)
    }

    /**
     * Kind of a hacky solution, since you shouldn't really keep that in the screen state
     */
    private var shouldScrollToTop = false
    private var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ratesList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ratesAdapter
        }
    }

    override fun invalidate() = withState(viewModel) { state ->
        if (shouldScrollToTop) {
            ratesList.smoothScrollToPosition(0)
            shouldScrollToTop = false
        }

        ratesList.isVisible = state.rates.isNotEmpty()
        ratesListProgress.isVisible = state.rates.isEmpty() && state.isLoading && !state.isError
        ratesListStub.isGone = !(state.isError && state.rates.isEmpty())

        handleProgress(state)
        handleContent(state)
        handleError(state)
    }

    override fun onStart() {
        super.onStart()
        viewModel.startObserving()
    }

    override fun onStop() {
        viewModel.stopObserving()
        super.onStop()
    }

    private fun onCurrencySelected(currency: CurrencyRateUi) {
        viewModel.changeCurrency(currency.key, currency.value)
        shouldScrollToTop = true
    }

    private fun handleError(state: RatesListState) {
        if (state.isError && state.rates.isNotEmpty()) {
            if (snackbar == null) {
                snackbar = Snackbar.make(
                    requireView(),
                    R.string.rates_error_message,
                    Snackbar.LENGTH_INDEFINITE
                )
                snackbar?.setTextMaxLines(4)
                snackbar?.show()
            }
        } else {
            snackbar?.dismiss()
            snackbar = null
        }
    }

    private fun handleContent(state: RatesListState) {
        if (state.rates.isNotEmpty()) {
            ratesAdapter.replaceItems(
                items = state.rates,
                withDiff = true,
                async = true
            )
        }
    }

    private fun handleProgress(state: RatesListState) {
        if (state.isLoading && state.rates.isEmpty()) {
            ratesList.isGone = true
            ratesListProgress.isVisible = true
            ratesListProgress.show()
        } else {
            ratesListProgress.hide()
        }
    }

    companion object {
        fun newInstance() = RatesListFragment()
    }
}
