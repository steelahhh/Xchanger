package dev.steelahhh.rates

import androidx.core.view.isGone
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
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

    companion object {
        fun newInstance() = RatesListFragment()
    }
}
