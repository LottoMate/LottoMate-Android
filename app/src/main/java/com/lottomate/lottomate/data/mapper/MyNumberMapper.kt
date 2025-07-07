package com.lottomate.lottomate.data.mapper

import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.data.model.MyNumberEntity
import com.lottomate.lottomate.domain.model.MyNumber
import com.lottomate.lottomate.presentation.screen.pocket.my.model.MyNumberDetailUiModel
import com.lottomate.lottomate.presentation.screen.pocket.my.model.MyNumberRowUiModel
import com.lottomate.lottomate.presentation.screen.pocket.my.model.MyNumberUiModel

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