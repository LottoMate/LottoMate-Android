package com.lottomate.lottomate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RandomLotto(
    @PrimaryKey(autoGenerate = true)
    val key: Int = 0,
    val randomNumbers: List<Int>,
    val createAt: String,
)