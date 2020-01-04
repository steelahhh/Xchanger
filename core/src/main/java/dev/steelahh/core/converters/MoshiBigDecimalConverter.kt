package dev.steelahh.core.converters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.math.BigDecimal

/*
 * Author: steelahhh
 * 4/1/20
 */

object MoshiBigDecimalConverter {
    @FromJson
    fun fromJson(string: String) = BigDecimal(string)

    @ToJson
    fun toJson(value: BigDecimal) = value.toString()
}
