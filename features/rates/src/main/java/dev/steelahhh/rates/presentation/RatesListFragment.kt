package dev.steelahhh.rates.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import dev.steelahhh.rates.R
import dev.steelahhh.rates.presentation.adapter.RatesListAdapter
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

        if (state.isLoading && state.rates.isEmpty()) ratesListProgress.show()
        else ratesListProgress.hide()

        if (state.rates.isNotEmpty()) {
            ratesList.isVisible = true
            ratesAdapter.replaceItems(
                items = state.rates,
                withDiff = true,
                async = true
            )
        } else {
            ratesList.isGone = state.isLoading
        }
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

    companion object {
        fun newInstance() = RatesListFragment()
    }
}
