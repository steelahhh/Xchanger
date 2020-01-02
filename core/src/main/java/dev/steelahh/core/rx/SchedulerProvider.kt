package dev.steelahh.core.rx

import io.reactivex.Scheduler

/*
 * Author: steelahhh
 * 2/1/20
 */

interface SchedulerProvider {
    val io: Scheduler
    val main: Scheduler
    val computation: Scheduler
}
