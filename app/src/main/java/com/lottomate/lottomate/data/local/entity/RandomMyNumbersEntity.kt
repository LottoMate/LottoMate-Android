package com.lottomate.lottomate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "RandomMyNumbers",
)
data class RandomMyNumbersEntity(
    @PrimaryKey(autoGenerate = true)
    val key: Int = 0,
    val numbers: List<Int>,
    val createAt: String,
)
