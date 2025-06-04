package com.lottomate.lottomate.utils.extentions

fun String.toDistanceInMeter(): Int {
    return if (this.contains("km")) {
        this.substringBefore("km").toDoubleOrNull()?.let {
            (it * 1000).toInt()
        } ?: 0
    } else {
        this.substringBefore("m").toIntOrNull() ?: 0
    }
}