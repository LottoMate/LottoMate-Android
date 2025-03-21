package com.lottomate.lottomate.presentation.screen.winnerguide.model

import com.lottomate.lottomate.data.model.LottoType

/**
 * 당첨자 가이드 타입
 */
enum class WinnerGuideType(
    val prizeClaimList: List<String>,
    val prizeClaimSubList: Map<Int, List<String>> = emptyMap(),
    val prizeClaimListCaptions: Map<Int, List<String>>  = emptyMap(),
    val warningList: List<String>,
    val warningCaptions: Map<Int, List<String>> = emptyMap() ,
) {
    LOTTO645(
        prizeClaimList = listOf(
            "NH농협에서 당첨된 복권과 함께\n신분증으로 당첨자를 확인해요",
            "모두 확인이 되면,\n당첨금을 받게 돼요",
            "당첨금은 일시불로 지급되며 요청 시\n현금으로 받을수도 있어요",
        ),
        warningList = listOf(
            "19세 미만은 복권을\n살 수 없어요",
            "당첨 복권을 잃어버리면\n당첨금을 받을 수 없어요",
            "복권이 훼손되어도 1/2 이상 살아있고\n컴퓨터가 인식 가능하면 당첨 정보를\n확인할 수 있어요",
        ),
    ),
    LOTTO720(
        prizeClaimList = listOf(
            "동행복권 본사 방문 전 꼭 전화로\n방문예약을 하세요",
            "예약한 날짜/시간에 동행복권\n본점에 방문해요",
            "동행복권에서\n당첨된 복권, 신분증으로\n당첨자를 확인해요",
            "모두 확인이 되면, 당첨금을 받아요"
        ),
        prizeClaimSubList = mapOf(
            0 to listOf(
                "월 - 금 오전 10시 - 오후 3시",
                "고객센터 : 1566 - 5520"
            ),
        ),
        prizeClaimListCaptions = mapOf(
            3 to listOf(
                "*연금식 당첨금은 방문 후 다음달 20일부터\n받아요 (공휴일인 경우 20일 전날에 받아요)",
                "*일반 당첨금은 한 번에 받아요"
            ),
        ),
        warningList = listOf(
            "19세 미만은 복권을\n살 수 없어요",
            "당첨 복권을 잃어버리면\n당첨금을 받을 수 없어요",
            "당첨금을 받을 수 있는 권리를\n타인에게 양도할 수 없으며, 금융권\n담보로 사용할 수 없어요",
            "오염 및 훼손된 연금복권은 동행복권\n지점을 방문하거나 우편접수로 복권\n당첨 확인 검사를 요청할 수 있어요"
        ),
        warningCaptions = mapOf(
            3 to listOf(
                "*검사비는 소지자가 부담합니다"
            ),
        ),
    ),
    SPEETTO(
        prizeClaimList = listOf(
            "동행복권 본사 방문 전 꼭 전화로\n방문예약을 하세요",
            "예약한 날짜/시간에 동행복권\n본점에 방문해요",
            "동행복권에서\n당첨된 복권, 신분증으로\n당첨자를 확인해요",
            "모두 확인이 되면, 당첨금을 받아요",
            "게임을 구성한 그림, 숫자 등 기호가\n오염 또는 훼손되어 확인할 수 없으면\n당첨금을 받을 수 없어요",
        ),
        prizeClaimSubList = mapOf(
            0 to listOf(
                "월 - 금 오전 10시 - 오후 3시",
                "고객센터 : 1566 - 5520"
            ),
        ),
        prizeClaimListCaptions = mapOf(
            3 to listOf(
                "*연금식 당첨금은 방문 후 다음달 20일에\n받아요 (공휴일인 경우 20일 전날에 받아요)",
                "*일반 당첨금은 한 번에 받아요"
            )
        ),
        warningList = listOf(
            "19세 미만은 복권을\n살 수 없어요",
            "당첨 복권을 잃어버리면\n당첨금을 받을 수 없어요",
            "아래의 경우 교환, 환불이 가능해요"
        ),
        warningCaptions = mapOf(
            2 to listOf(
                "*긁는 부분이 긁히지 않는 경우",
                "*당첨 내역을 초과해서 당첨된 경우",
                "*인쇄 오류가 인정되는 경우",
            )
        )
    );

    companion object {
        fun findWinnerGuide(type: LottoType): WinnerGuideType {
            return when (type) {
                LottoType.L645 -> LOTTO645
                LottoType.L720 -> LOTTO720
                else -> SPEETTO
            }
        }
    }
}