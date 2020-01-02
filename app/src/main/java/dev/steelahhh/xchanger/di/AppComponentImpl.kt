package dev.steelahhh.xchanger.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dev.steelahh.core.di.AppComponent
import javax.inject.Singleton

/*
 * Author: steelahhh
 * 2/1/20
 */

@Singleton
@Component
interface AppComponentImpl : AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponentImpl
    }
}
