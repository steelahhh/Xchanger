package dev.steelahhh.xchanger

import android.app.Application
import android.os.Looper
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.MvRxViewModelConfigFactory
import dev.steelahh.core.di.AppComponent
import dev.steelahh.core.di.InjectorProvider
import dev.steelahhh.xchanger.di.DaggerAppComponentImpl
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers

/*
 * Author: steelahhh
 * 2/1/20
 */

open class App : Application(), InjectorProvider {
    override val component: AppComponent by lazy {
        DaggerAppComponentImpl.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        initializeMvRx()
        initializeRxPlugin()
    }

    private fun initializeMvRx() {
        MvRx.viewModelConfigFactory = MvRxViewModelConfigFactory(false)
    }

    private fun initializeRxPlugin() {
        val asyncMainThreadScheduler = AndroidSchedulers.from(
            Looper.getMainLooper(),
            true
        )
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { asyncMainThreadScheduler }
    }
}
