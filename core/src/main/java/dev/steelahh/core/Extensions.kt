package dev.steelahh.core

import android.app.Activity
import androidx.fragment.app.Fragment
import dev.steelahh.core.di.InjectorProvider

/*
 * Author: steelahhh
 * 2/1/20
 */

val Activity.injector get() = (application as InjectorProvider).component

val Fragment.injector get() = requireActivity().injector
