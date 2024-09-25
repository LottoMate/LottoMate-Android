package com.lottomate.lottomate.presentation.screen.map.model

import com.lottomate.lottomate.data.model.LottoType
import com.naver.maps.geometry.LatLng

// TODO : 전달되는 데이터 형식이 아직 정해지지 않아서 추후 수정할 예정

data class StoreInfo(
    val storeName: String,
    val hasLottoType: List<String>,
    val distance: Double,
    val latLng: LatLng,
    val address: String,
    val phone: String? = null,
    val winCountOfLottoType: List<StoreWinCount>,
)

data class StoreWinCount(
    val lottoType: LottoType,
    val count: Int,
    val winningDetails: List<WinningDetail>,
)

data class WinningDetail(
    val content: String,
    val round: String,
)
val StoreInfoMock = StoreInfo(
    storeName = "도곡 행운로또방",
    hasLottoType = listOf("로또", "연금복권", "스피또"),
    distance = 1.6,
    address = "서울 강남구 도곡로4길 11 102호",
    latLng = LatLng(37.4893124651714, 127.033694208578),
    phone = "02-123-4567",
    winCountOfLottoType = listOf(
        StoreWinCount(
            lottoType = LottoType.L645,
            count = 111,
            winningDetails = listOf(
                WinningDetail(
                    content = "로또 1등 25억원 당첨",
                    round = "5016회"
                ),
                WinningDetail(
                    content = "로또 1등 25억원 당첨",
                    round = "5016회"
                ),
                WinningDetail(
                    content = "로또 1등 25억원 당첨",
                    round = "5016회"
                ),
                WinningDetail(
                    content = "로또 1등 25억원 당첨",
                    round = "5016회"
                ),
            )
        ),
        StoreWinCount(
            lottoType = LottoType.L720,
            count = 9,
            winningDetails = listOf(
                WinningDetail(
                    content = "연금복권 1등 25억원 당첨",
                    round = "6102회"
                ),
                WinningDetail(
                    content = "연금복권 1등 25억원 당첨",
                    round = "6102"
                ),
                WinningDetail(
                    content = "연금복권 1등 25억원 당첨",
                    round = "6102"
                ),
            )
        ),
        StoreWinCount(
            lottoType = LottoType.S2000,
            count = 4,
            winningDetails = listOf(
                WinningDetail(
                    content = "스피또 2000 25억원 당첨",
                    round = "52회"
                ),
                WinningDetail(
                    content = "스피또 2000 25억원 당첨",
                    round = "52회"
                ),
            )
        ),
    )
)

val StoreInfoMocks = listOf(
    StoreInfoMock,
    StoreInfoMock.copy(
        storeName = "신사우리가판",
        address = "서울 강남구 강남대로 620",
        latLng = LatLng(37.5171459798091, 127.01969555607)
    ),
    StoreInfoMock.copy(
        storeName = "버스표판매소",
        address = "서울 강남구 압구정로 151-2 신현대9차 상가1동 출입구 앞",
        latLng = LatLng(37.5252805267207, 127.02440030268),
        winCountOfLottoType = emptyList(),
    ),
    StoreInfoMock.copy(
        storeName = "선릉가판점",
        address = "서울 강남구 선릉로 519",
        latLng = LatLng(37.5058724287544, 127.048007701663),
        winCountOfLottoType = emptyList(),
    ),
    StoreInfoMock.copy(
        storeName = "버스표판매소",
        address = "서울 강남구 논현로 837",
        latLng = LatLng(37.5233390681827,127.027961858387),
        winCountOfLottoType = emptyList(),
    ),
    StoreInfoMock.copy(
        storeName = "가로판매대(구)-3",
        address = "서울 강남구 압구정로 201",
        latLng = LatLng(37.5315341736395, 127.029273123095)
    ),
    StoreInfoMock.copy(
        storeName = "삼성대박복권방",
        address = "서울 강남구 봉은사로74길 13 101호",
        latLng = LatLng(37.5116699286081, 127.051878663467)
    ),
    StoreInfoMock.copy(
        storeName = "포이로또방",
        address = "서울 강남구 개포로22길 19 1층",
        latLng = LatLng(37.477752673752, 127.048542099489)
    ),
    StoreInfoMock.copy(
        storeName = "버스카드충전소",
        address = "서울 강남구 테헤란로 625 삼성동 덕명빌딩 앞",
        latLng = LatLng(37.5101503390692, 127.066048979299)
    ),
    StoreInfoMock.copy(
        storeName = "교통카드충전소",
        address = "서울 강남구 선릉로 309",
        latLng = LatLng(37.4971857834581, 127.052159610697),
        winCountOfLottoType = emptyList(),
    ),
    StoreInfoMock.copy(
        storeName = "LOTTO복권",
        address = "서울 강남구 언주로70길 10",
        latLng = LatLng(37.4969537535198, 127.046376168482)
    ),
    StoreInfoMock.copy(
        storeName = "새날",
        address = "서울 강남구 강남대로84길 23 115호",
        latLng = LatLng(37.4971298698887, 127.0303844347)
    ),
    StoreInfoMock.copy(
        storeName = "로또",
        address = "서울 강남구 일원로3길 66 1층",
        latLng = LatLng(37.4926942568508, 127.085063146495)
    )
)