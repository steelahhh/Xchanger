package dev.steelahh.core.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/*
 * Author: steelahhh
 * 2/1/20
 */

class SchedulerProviderImpl : SchedulerProvider {
    override val main get(): Scheduler = Schedulers.io()
    override val computation get(): Scheduler = Schedulers.computation()
    override val io get(): Scheduler = AndroidSchedulers.mainThread()
}
