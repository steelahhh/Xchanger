package dev.steelahhh.rates.data

import java.util.concurrent.TimeUnit

data class PollingRate(
    val time: Long,
    val timeUnit: TimeUnit = TimeUnit.MILLISECONDS
)
