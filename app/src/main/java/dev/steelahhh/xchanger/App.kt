package dev.steelahhh.xchanger

import android.app.Application
import dev.steelahh.core.di.AppComponent
import dev.steelahh.core.di.InjectorProvider
import dev.steelahhh.xchanger.di.DaggerAppComponentImpl

/*
 * Author: steelahhh
 * 2/1/20
 */

open class App : Application(), InjectorProvider {
    override val component: AppComponent by lazy {
        DaggerAppComponentImpl.factory().create(applicationContext)
    }
}
