package com.lottomate.lottomate.data.mapper

import com.lottomate.lottomate.data.remote.model.StoreInfoEntity
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.naver.maps.geometry.LatLng

object StoreMapper {
    fun toModel(storeInfoEntity: StoreInfoEntity): StoreInfo {
        return StoreInfo(
            key = storeInfoEntity.storeNo,
            storeName = storeInfoEntity.storeNm,
            hasLottoType = storeInfoEntity.lottoTypeList,
            distance = 1400,
            latLng = LatLng(storeInfoEntity.addrLat, storeInfoEntity.addrLot),
            address = storeInfoEntity.storeAddr,
            phone = storeInfoEntity.storeTel,
            winCountOfLottoType = emptyList(),
            isLike = false,
            countLike = 0,
        )
    }
}