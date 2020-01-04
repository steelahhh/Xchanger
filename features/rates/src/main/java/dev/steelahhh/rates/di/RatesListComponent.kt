package dev.steelahhh.rates.di

import dagger.Component
import dev.steelahh.core.CoreModule
import dev.steelahh.core.di.AppComponent
import dev.steelahhh.rates.data.CurrencyRatesRepository

/*
 * Author: steelahhh
 * 4/1/20
 */

@RatesListScope
@Component(
    dependencies = [AppComponent::class],
    modules = [RatesListModule::class, CoreModule::class]
)
interface RatesListComponent {
    val repository: CurrencyRatesRepository

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): RatesListComponent
    }
}
