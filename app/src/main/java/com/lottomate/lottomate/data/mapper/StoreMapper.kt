package com.lottomate.lottomate.data.mapper

import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.data.remote.model.LottoInfoEntity
import com.lottomate.lottomate.data.remote.model.StoreDetailEntity
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.screen.map.model.StoreWinCount
import com.lottomate.lottomate.presentation.screen.map.model.WinningDetail
import com.lottomate.lottomate.utils.extentions.toDistanceInMeter
import com.naver.maps.geometry.LatLng

fun StoreDetailEntity.toUiModel() = StoreInfo(
    key = this.storeNo,
    storeName = this.storeNm,
    hasLottoType = this.lottoTypeList.map {
        val type = LottoType.findLottoTypeByName(it)
        LottoType.getLottoNameByType(type)
    }.distinctBy { it },
    distance = this.distance.toDistanceInMeter(),
    latLng = LatLng(this.addrLat, this.addrLot),
    address = this.storeAddr,
    phone = this.storeTel,
    winCountOfLottoType = this.lottoInfos
        .map { info ->
            val lottoType = LottoType.fromCode(info.lottoType)
            val group = lottoType.group

            group to WinningDetail(
                lottoType = lottoType,
                rank = info.place.toString().plus("등"),
                prize = info.lottoJackpot?.formatToEokDecimal() ?: "",
                round = "${info.drwNum}회"
            )
        }
        .groupBy({ it.first }, { it.second })
        .map { (group, details) ->
            StoreWinCount(
                lottoType = group,
                count = details.size,
                winningDetails = details
            )
        },
    isLike = false,
    countLike = 0,
    winningDetails = this.lottoInfos.map { it.toUiModel() },
)

fun LottoInfoEntity.toUiModel() = WinningDetail(
    lottoType = LottoType.fromCode(this.lottoType),
    rank = this.place.toString().plus("등"),
    prize = this.lottoJackpot?.formatToEokDecimal() ?: "",
    round = this.drwNum.toString().plus("회")
)

fun Long.formatToEokDecimal(): String {
    val eok = this / 100_000_000.0
    return String.format("%.1f억원", eok) // 예: "16.8억"
}
