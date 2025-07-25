package com.lottomate.lottomate.data.mapper

import com.lottomate.lottomate.data.local.entity.RandomMyNumbersEntity
import com.lottomate.lottomate.domain.model.RandomMyNumbers
import com.lottomate.lottomate.domain.model.RandomMyNumbersGroup
import com.lottomate.lottomate.presentation.screen.pocket.random.model.RandomMyNumbersGroupUiModel
import com.lottomate.lottomate.presentation.screen.pocket.random.model.RandomMyNumbersUiModel
import com.lottomate.lottomate.utils.DateUtils

fun RandomMyNumbersEntity.toDomain() = RandomMyNumbers(
    key = this.key,
    numbers = this.numbers,
    date = this.createAt,
)

fun RandomMyNumbers.toEntity() = RandomMyNumbersEntity(
    numbers = this.numbers,
    createAt = this.date,
)

fun List<RandomMyNumbersGroup>.toUiModel() = this.map { it.toUiModel() }

fun RandomMyNumbersGroup.toUiModel() = RandomMyNumbersGroupUiModel(
    date = this.date.replace("-", "."),
    numbers = this.numbers.map { it.toUiModel() },
)
fun RandomMyNumbers.toUiModel() = RandomMyNumbersUiModel(
    id = this.key,
    numbers = this.numbers,
)

fun RandomMyNumbersUiModel.toDomain() = RandomMyNumbers(
    key = this.id,
    numbers = this.numbers,
    date = DateUtils.getCurrentDate(),
)