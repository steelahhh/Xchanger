package dev.steelahhh.rates.presentation

import androidx.core.view.isGone
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import dev.steelahhh.rates.R
import kotlinx.android.synthetic.main.fragment_rates.*

/*
 * Author: steelahhh
 * 2/1/20
 */

class RatesListFragment : BaseMvRxFragment(R.layout.fragment_rates) {

    private val viewModel: RatesListViewModel by fragmentViewModel()

    override fun invalidate() = withState(viewModel) { state ->
        ratesList.isGone = state.isLoading
        if (state.isLoading) ratesListProgress.show()
        else ratesListProgress.hide()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onStop() {
        viewModel.onStop()
        super.onStop()
    }

    companion object {
        fun newInstance() = RatesListFragment()
    }
}
