package com.lottomate.lottomate.data.mapper

import com.lottomate.lottomate.data.local.entity.RandomMyNumbersEntity
import com.lottomate.lottomate.domain.model.RandomMyNumbers
import com.lottomate.lottomate.utils.DateUtils

fun RandomMyNumbersEntity.toDomain() = RandomMyNumbers(
    key = this.key,
    numbers = this.numbers,
    date = this.createAt,
)

fun RandomMyNumbers.toEntity() = RandomMyNumbersEntity(
    numbers = this.numbers,
    createAt = DateUtils.getCurrentDate(),
)