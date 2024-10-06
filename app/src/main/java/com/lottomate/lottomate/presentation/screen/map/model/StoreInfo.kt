package com.lottomate.lottomate.presentation.screen.map.model

import android.icu.text.DecimalFormat
import com.lottomate.lottomate.data.model.LottoType
import com.naver.maps.geometry.LatLng

// TODO : 전달되는 데이터 형식이 아직 정해지지 않아서 추후 수정할 예정

data class StoreInfo(
    val key: Int,
    val storeName: String,
    val hasLottoType: List<String>,
    val distance: Int,
    val latLng: LatLng,
    val address: String,
    val phone: String = "-",
    val winCountOfLottoType: List<StoreWinCount>,
    val isLike: Boolean = false,
    val countLike: Int = 0,
) {
    val hasWinLotto645 = getCountLotto645() != 0
    val hasWinLotto720 = getCountLotto720() != 0
    val hasWinSpeetto = getCountSpeetto() != 0

    fun getCountLotto645(): Int = winCountOfLottoType.count { it.lottoType == LottoType.L645 }
    fun getCountLotto720(): Int = winCountOfLottoType.count { it.lottoType == LottoType.L720 }
    fun getCountSpeetto(): Int =
        winCountOfLottoType.count { it.lottoType == LottoType.S2000 || it.lottoType == LottoType.S1000 || it.lottoType == LottoType.S500 }
    fun getCountLike(): String {
        val formatter = DecimalFormat("#,###")
        return buildString {
            if (countLike > MAX_COUNT_LIKE) {
                val formatted = formatter.format(MAX_COUNT_LIKE)
                append(formatted.plus("+"))
            } else {
                append(formatter.format(countLike))
            }
        }
    }

    companion object {
        private const val MAX_COUNT_LIKE = 9_999
    }
}

data class StoreWinCount(
    val lottoType: LottoType,
    val count: Int,
    val winningDetails: List<WinningDetail>,
)

data class WinningDetail(
    val lottoType: String,
    val rank: String = "1등",
    val prize: String,
    val round: String,
)
val StoreInfoMock = StoreInfo(
    key = 1,
    storeName = "도곡 행운로또방",
    hasLottoType = listOf("로또", "연금복권", "스피또"),
    distance = 1600,
    address = "서울 강남구 도곡로4길 11 102호",
    latLng = LatLng(37.4893124651714, 127.033694208578),
    phone = "02-123-4567",
    winCountOfLottoType = listOf(
        StoreWinCount(
            lottoType = LottoType.L645,
            count = 11,
            winningDetails = List(11) {
                WinningDetail(
                    lottoType = "로또",
                    prize = "25억원",
                    round = "6102회"
                )
            },
        ),
        StoreWinCount(
            lottoType = LottoType.L720,
            count = 9,
            winningDetails = List(9) {
                WinningDetail(
                    lottoType = "연금복권",
                    prize = "25억원",
                    round = "6102회"
                )
            },
        ),
        StoreWinCount(
            lottoType = LottoType.S2000,
            count = 4,
            winningDetails = List(4) {
                WinningDetail(
                    lottoType = "스피또 2000",
                    prize = "25억원",
                    round = "6102회"
                )
            },
        ),
    ).shuffled()
)

val StoreInfoMocks = listOf(
    StoreInfoMock,
    StoreInfoMock.copy(
        key = 2,
        storeName = "신사우리가판",
        address = "서울 강남구 강남대로 620",
        latLng = LatLng(37.5171459798091, 127.01969555607),
        isLike = true,
        countLike = 123,
        distance = 1100,
    ),
    StoreInfoMock.copy(
        key = 3,
        storeName = "버스표판매소",
        address = "서울 강남구 압구정로 151-2 신현대9차 상가1동 출입구 앞",
        latLng = LatLng(37.5252805267207, 127.02440030268),
        winCountOfLottoType = emptyList(),
        phone = "-",
        distance = 100,
    ),
    StoreInfoMock.copy(
        key = 4,
        storeName = "선릉가판점",
        address = "서울 강남구 선릉로 519",
        latLng = LatLng(37.5058724287544, 127.048007701663),
        winCountOfLottoType = emptyList(),
        isLike = true,
        countLike = 1234,
        distance = 9900,
    ),
    StoreInfoMock.copy(
        key = 5,
        storeName = "버스표판매소",
        address = "서울 강남구 논현로 837",
        latLng = LatLng(37.5233390681827,127.027961858387),
        winCountOfLottoType = emptyList(),
        distance = 130,
    ),
    StoreInfoMock.copy(
        key = 6,
        storeName = "가로판매대(구)-3",
        address = "서울 강남구 압구정로 201",
        latLng = LatLng(37.5315341736395, 127.029273123095),
        isLike = true,
        countLike = 1,
        distance = 90,
    ),
    StoreInfoMock.copy(
        key = 7,
        storeName = "삼성대박복권방",
        address = "서울 강남구 봉은사로74길 13 101호",
        latLng = LatLng(37.5116699286081, 127.051878663467),
        distance = 700,
    ),
    StoreInfoMock.copy(
        key = 8,
        storeName = "포이로또방",
        address = "서울 강남구 개포로22길 19 1층",
        latLng = LatLng(37.477752673752, 127.048542099489),
        isLike = true,
        countLike = 99999,
        distance = 10,
    ),
    StoreInfoMock.copy(
        key = 9,
        storeName = "버스카드충전소",
        address = "서울 강남구 테헤란로 625 삼성동 덕명빌딩 앞",
        latLng = LatLng(37.5101503390692, 127.066048979299),
        distance = 400,
    ),
    StoreInfoMock.copy(
        key = 10,
        storeName = "교통카드충전소",
        address = "서울 강남구 선릉로 309",
        latLng = LatLng(37.4971857834581, 127.052159610697),
        winCountOfLottoType = emptyList(),
        countLike = 23,
        distance = 2300,
    ),
    StoreInfoMock.copy(
        key = 11,
        storeName = "LOTTO복권",
        address = "서울 강남구 언주로70길 10",
        latLng = LatLng(37.4969537535198, 127.046376168482),
        phone = "-",
        distance = 3500,
    ),
    StoreInfoMock.copy(
        key = 12,
        storeName = "새날",
        address = "서울 강남구 강남대로84길 23 115호",
        latLng = LatLng(37.4971298698887, 127.0303844347),
        countLike = 19,
        distance = 35000,
    ),
    StoreInfoMock.copy(
        key = 13,
        storeName = "로또",
        address = "서울 강남구 일원로3길 66 1층",
        latLng = LatLng(37.4926942568508, 127.085063146495),
        phone = "-",
        distance = 9950,
    )
)