package com.lottomate.lottomate.data.mapper

import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.data.remote.model.LottoInfoEntity
import com.lottomate.lottomate.data.remote.model.StoreDetailEntity
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.screen.map.model.WinningDetail
import com.naver.maps.geometry.LatLng

fun StoreDetailEntity.toUiModel() = StoreInfo(
    key = this.storeNo,
    storeName = this.storeNm,
    hasLottoType = this.lottoTypeList.map {
        val type = LottoType.findLottoTypeByName(it)
        LottoType.getLottoNameByType(type)
    }.distinctBy { it },
    distance = this.distance.substringBefore("km").toDouble().run {
        (this * 1000).toInt()
    },
    latLng = LatLng(this.addrLat, this.addrLot),
    address = this.storeAddr,
    phone = this.storeTel,
    winCountOfLottoType = emptyList(),
    isLike = false,
    countLike = 0,
    winningDetails = this.lottoInfos.map { it.toUiModel() },
)

fun LottoInfoEntity.toUiModel() = WinningDetail(
    lottoType = LottoType.fromCode(this.lottoType),
    rank = this.place.toString().plus("등"),
    prize = (this.lottoJacpot / 100_000_000).toString().plus("억원"),
    round = this.drwNum.toString().plus("회")
)