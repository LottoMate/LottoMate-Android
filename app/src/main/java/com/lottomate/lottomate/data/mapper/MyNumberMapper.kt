package com.lottomate.lottomate.data.mapper

import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.data.model.MyNumberEntity
import com.lottomate.lottomate.domain.model.MyNumber
import com.lottomate.lottomate.domain.model.ScanMyNumber
import com.lottomate.lottomate.presentation.screen.pocket.my.model.MyNumberDetailUiModel
import com.lottomate.lottomate.presentation.screen.pocket.my.model.MyNumberRowUiModel
import com.lottomate.lottomate.presentation.screen.pocket.my.model.MyNumberUiModel
import com.lottomate.lottomate.presentation.screen.result.model.MyLotto645Info
import com.lottomate.lottomate.presentation.screen.result.model.MyLotto720Info

fun MyNumberEntity.toDomain() = MyNumber(
    no = this.memberLottoNo,
    type = LottoType.findLottoTypeByCode(this.lottoType),
    drawNo = this.lottoDrwNo,
    numbers = this.lottoNum,
    isWinner = this.winnYn,
)

fun List<MyNumber>.toUiModel() = MyNumberUiModel(
    totalCount = this.size,
    winCount = this.count { it.isWinner },
    loseCount = this.count { !it.isWinner },
    myNumberDetails = this.groupBy { it.type }
        .flatMap { (type, item) ->
            item.groupBy { it.drawNo }
                .map { (round, numbers) ->
                    MyNumberDetailUiModel(
                        type = type,
                        round = round,
                        numberRows = numbers.map { row ->
                            MyNumberRowUiModel(
                                id = row.no,
                                numbers = row.numbers,
                                isWin = row.isWinner,
                            )
                        }
                    )
                }
        }.sortedBy { it.date }
)

fun MyLotto645Info.toScanMyNumber() = ScanMyNumber(
    type = LottoType.L645,
    round = this.round,
    numbers = this.numbers,
)

fun MyLotto720Info.toScanMyNumber() = ScanMyNumber(
    type = LottoType.L720,
    round = this.round,
    numbers = this.numbers.map { it.numbers },
)