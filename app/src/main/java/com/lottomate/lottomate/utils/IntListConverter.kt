package com.lottomate.lottomate.utils

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class IntListConverter {
    @TypeConverter
    fun fromList(value: List<Int>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toList(value: String): List<Int> {
        return Json.decodeFromString(value)
    }
}