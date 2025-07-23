package com.lottomate.lottomate.data.mapper

import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.data.remote.request.RegisterLottoNumberEntity
import com.lottomate.lottomate.domain.model.RegisterLottoNumber
import com.lottomate.lottomate.presentation.screen.pocket.register.model.RegisterLottoNumberUiModel

fun RegisterLottoNumberUiModel.toDomain(type: LottoType, round: Int) = RegisterLottoNumber(
    type = type,
    numbers = if (type == LottoType.L645) this.lottoNumbers.chunked(2) else this.lottoNumbers.chunked(1),
    round = round,
)

fun RegisterLottoNumber.toEntity() = RegisterLottoNumberEntity(
    type = this.type.code,
    round = this.round,
    lottoNumbers = this.numbers,
)