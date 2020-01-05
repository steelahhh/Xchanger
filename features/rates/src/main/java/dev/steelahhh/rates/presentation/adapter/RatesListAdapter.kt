package dev.steelahhh.rates.presentation.adapter

import android.text.Editable
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.textfield.TextInputEditText
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import dev.steelahh.core.BaseAdapter
import dev.steelahh.core.EmptyTextWatcher
import dev.steelahh.core.hideSoftKeyboard
import dev.steelahh.core.moveCursorToEnd
import dev.steelahh.core.showSoftKeyboard
import dev.steelahhh.rates.R
import dev.steelahhh.rates.presentation.CurrencyRateUi
import java.math.BigDecimal

/*
 * Author: steelahhh
 * 4/1/20
 */

class RatesListAdapter(
    onRateClick: (CurrencyRateUi) -> Unit,
    onTextChanged: (BigDecimal) -> Unit
) : BaseAdapter<CurrencyRateUi>(rateDelegate(onRateClick, onTextChanged)) {
    override val diffItemCallback: DiffUtil.ItemCallback<CurrencyRateUi>? =
        object : DiffUtil.ItemCallback<CurrencyRateUi>() {
            override fun areItemsTheSame(
                oldItem: CurrencyRateUi,
                newItem: CurrencyRateUi
            ): Boolean = oldItem.key == newItem.key

            override fun areContentsTheSame(
                oldItem: CurrencyRateUi,
                newItem: CurrencyRateUi
            ): Boolean = oldItem.value == newItem.value

            override fun getChangePayload(
                oldItem: CurrencyRateUi,
                newItem: CurrencyRateUi
            ): Any? = if (oldItem.value != newItem.value) true else null
        }
}

fun rateDelegate(
    onRateClick: (CurrencyRateUi) -> Unit,
    onTextChanged: (BigDecimal) -> Unit
) = adapterDelegate<CurrencyRateUi, CurrencyRateUi>(layout = R.layout.item_currency_rate) {
    val textWatcher = object : EmptyTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            if (s.isNullOrEmpty()) onTextChanged(BigDecimal.ZERO)
            else onTextChanged(BigDecimal(s.toString()))
        }
    }

    itemView.setOnClickListener { onRateClick(item) }

    val keyTv: TextView = findViewById(R.id.currencyKey)
    val nameTv: TextView = findViewById(R.id.currencyTitle)
    val imageFlag: ImageView = findViewById(R.id.currencyFlag)
    val valueEditText: TextInputEditText = findViewById(R.id.currencyValue)

    bind { payload ->
        valueEditText.removeTextChangedListener(textWatcher)

        /**
         * Load the images straight from the resources
         * Glide or some other library might be introduced in the future
         * Also good idea would be to not store the flags locally, and get them from API
         */
        try {
            val flagRes = context.resources.getIdentifier(
                item.key.toLowerCase() + "_flag",
                "drawable",
                context.applicationContext.packageName
            )
            imageFlag.setImageDrawable(context.getDrawable(flagRes))
        } catch (e: Exception) {
            // might need to handle the case when there's no flag for the given currency
        }

        if (payload.isEmpty()) {
            keyTv.text = item.key
            nameTv.text = item.name
            valueEditText.setText(item.value)
        }

        if (payload.isNotEmpty() && !valueEditText.hasFocus())
            valueEditText.setText(item.value)

        with(valueEditText) {
            isFocusable = item.isEditable
            isFocusableInTouchMode = item.isEditable
            isClickable = item.isEditable

            if (item.isEditable) addTextChangedListener(textWatcher)

            setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    v.hideSoftKeyboard()
                } else {
                    moveCursorToEnd()
                    v.showSoftKeyboard()
                }
            }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFocus()
                    hideSoftKeyboard()
                }
                false
            }
        }
    }
}
