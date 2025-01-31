package com.lottomate.lottomate.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface LottoInfoEntity

@Serializable
@SerialName("645")
data class Lotto645InfoEntity(
    val lottoDrwNo: Int,
    val lottoType: String,
    val lottoNum: List<Int>,
    val lottoBonusNum: List<Int>,
    val drwDate: String,
    val drwNum: Int,
    val totalSalesPrice: Long,
    val p1Jackpot: Long,
    val p1WinnrCnt: Int,
    val p1PerJackpot:Long,
    val p2Jackpot: Long,
    val p2WinnrCnt: Int,
    val p2PerJackpot: Long,
    val p3Jackpot: Long,
    val p3WinnrCnt: Int,
    val p3PerJackpot: Long,
    val p4Jackpot: Long,
    val p4WinnrCnt: Int,
    val p4PerJackpot: Long,
    val p5Jackpot: Long,
    val p5WinnrCnt: Int,
    val p5PerJackpot: Long,
): LottoInfoEntity

@Serializable
@SerialName("720")
data class Lotto720InfoEntity(
    val lottoDrwNo: Int,
    val lottoType: String,
    val lottoNum: List<Int>,
    val lottoBonusNum: List<Int>,
    val drwDate: String,
    val drwNum: Int,
    val p1WinnrCnt: Int,
    val p2WinnrCnt: Int,
    val p3WinnrCnt: Int,
    val p4WinnrCnt: Int,
    val p5WinnrCnt: Int,
    val p6WinnrCnt: Int,
    val p7WinnrCnt: Int,
): LottoInfoEntity