package dev.steelahh.core

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import dev.steelahh.core.di.InjectorProvider

/*
 * Author: steelahhh
 * 2/1/20
 */

val Activity.injector get() = (application as InjectorProvider).component

val Fragment.injector get() = requireActivity().injector

fun <T> MutableList<T>.putAll(obj: Collection<T>) {
    obj.forEach { put(it) }
}

fun <T> MutableList<T>.put(obj: T) {
    val iOf = indexOf(obj)
    if (iOf != -1) set(iOf, obj)
    else add(obj)
}

fun View.showSoftKeyboard() {
    if (requestFocus()) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun View.hideSoftKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

open class EmptyTextWatcher : TextWatcher {
    override fun afterTextChanged(s: Editable?) = Unit
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
}

fun EditText.moveCursorToEnd() {
    if (length() > 0) setSelection(length())
}

fun Snackbar.setTextMaxLines(lines: Int) {
    val textView: TextView = view.findViewById(com.google.android.material.R.id.snackbar_text)
    textView.maxLines = lines
}
