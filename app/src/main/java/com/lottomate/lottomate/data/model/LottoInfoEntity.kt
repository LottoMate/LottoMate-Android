package com.lottomate.lottomate.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface LottoInfoEntity

@Serializable
@SerialName("645")
data class Lotto645InfoEntity(
    val lottoRndNum: Int,
    val drwtDate: String,
    val prizeMoney: List<Long>,
    val drwtWinNum: List<Int>,
    val drwtMoney: List<Long>,
    val drwtNum: List<Int>,
    val drwtBonusNum: List<Int>,
    val drwtSaleMoney: Long = 0L,
): LottoInfoEntity

@Serializable
@SerialName("720")
data class Lotto720InfoEntity(
    val lottoRndNum: Int,
    val drwtDate: String,
    val drwtWinNum: List<Int> = emptyList(),
    val drwtNum: List<Int>,
    val drwtBonusNum: List<Int>,
): LottoInfoEntity