package dev.steelahh.core

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dev.steelahh.core.rx.SchedulerProvider
import dev.steelahh.core.rx.SchedulerProviderImpl

/*
 * Author: steelahhh
 * 2/1/20
 */

@Module
object CoreModule {
    @Provides
    @JvmStatic
    @Reusable
    fun provideSchedulers(): SchedulerProvider = SchedulerProviderImpl()
}
