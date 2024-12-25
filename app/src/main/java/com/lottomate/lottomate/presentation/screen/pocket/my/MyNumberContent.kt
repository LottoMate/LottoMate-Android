package com.lottomate.lottomate.presentation.screen.pocket.my

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.component.BannerCard
import com.lottomate.lottomate.presentation.component.LottoMateAssistiveButton
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateCard
import com.lottomate.lottomate.presentation.component.LottoMateOutLineButton
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTextButton
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoBall645
import com.lottomate.lottomate.presentation.screen.lottoinfo.component.LottoBall720
import com.lottomate.lottomate.presentation.screen.pocket.model.LottoCondition
import com.lottomate.lottomate.presentation.screen.pocket.model.LottoDetail
import com.lottomate.lottomate.presentation.screen.pocket.model.mockLottoDetails
import com.lottomate.lottomate.presentation.ui.LottoMateError
import com.lottomate.lottomate.presentation.ui.LottoMateGray10
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray60
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMatePositive
import com.lottomate.lottomate.presentation.ui.LottoMateRed100
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.utils.noInteractionClickable
import dagger.Lazy

@Composable
fun MyNumberContent(
    modifier: Modifier = Modifier,
    onClickQRScan: () -> Unit,
    onClickSaveNumbers: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            LottoMateCard(
                modifier = Modifier.weight(1f),
                onClick = {},
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LottoMateText(
                        text = stringResource(id = R.string.pocket_title_my_number_left),
                        textAlign = TextAlign.Center,
                        style = LottoMateTheme.typography.headline2,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.img_rounge_cony2),
                        contentDescription = stringResource(id = R.string.pocket_desc_my_number_top_left_image),
                        modifier = Modifier.size(76.dp),
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LottoMateAssistiveButton(
                        text = stringResource(id = R.string.pocket_text_my_number_scan),
                        buttonSize = LottoMateButtonProperty.Size.SMALL,
                        onClick = onClickQRScan,
                    )
                }
            }

            Spacer(modifier = Modifier.width(15.dp))

            LottoMateCard(
                modifier = Modifier.weight(1f),
                onClick = {},
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LottoMateText(
                        text = stringResource(id = R.string.pocket_title_my_number_right),
                        textAlign = TextAlign.Center,
                        style = LottoMateTheme.typography.headline2,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.img_rounge_cony),
                        contentDescription = stringResource(id = R.string.pocket_desc_my_number_top_right_image),
                        modifier = Modifier.size(76.dp),
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LottoMateAssistiveButton(
                        text = stringResource(id = R.string.pocket_text_my_number_save_number),
                        buttonSize = LottoMateButtonProperty.Size.SMALL,
                        onClick = onClickSaveNumbers,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        MyLottoSituation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.DefaultPadding20),
        )

        Spacer(modifier = Modifier.height(48.dp))

        MyLottoHistory(
            modifier = Modifier.padding(horizontal = Dimens.DefaultPadding20),
            myLottoHistories = mockLottoDetails,
            onClickEdit = {},
            onClickCheckWin = {},
        )
    }
}

@Composable
private fun MyLottoEmpty(
    modifier: Modifier = Modifier,
    onClickDrawNumber: () -> Unit,
) {
    Column {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color = LottoMateGray10)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp, bottom = 49.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pocket_venus),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                LottoMateText(
                    text = "아직 저장한 내 로또가 없어요.",
                    style = LottoMateTheme.typography.body2
                        .copy(color = LottoMateGray100),
                )

                Spacer(modifier = Modifier.height(16.dp))

                LottoMateSolidButton(
                    text = "번호 뽑으러 가기",
                    buttonSize = LottoMateButtonProperty.Size.SMALL,
                    buttonShape = LottoMateButtonProperty.Shape.ROUND,
                    onClick = onClickDrawNumber,
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))

        BannerCard(
            modifier = Modifier
                .padding(Dimens.DefaultPadding20)
                .padding(bottom = 28.dp),
            onClickBanner = {}
        )
    }

}

@Composable
private fun MyLottoSituation(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        LottoMateText(
            text = "내 로또 현황",
            style = LottoMateTheme.typography.headline1,
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        LottoMateCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = {}
        ) {
            Row(
                modifier = Modifier.padding(vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LottoMateText(
                        text = stringResource(id = R.string.pocket_title_my_number_possible_check),
                        style = LottoMateTheme.typography.label2,
                    )

                    Row {
                        LottoMateText(
                            text = "내 로또",
                            style = LottoMateTheme.typography.headline1,
                        )

                        Spacer(modifier = Modifier.width(2.dp))

                        LottoMateText(
                            text = "NN",
                            style = LottoMateTheme.typography.headline1
                                .copy(color = LottoMateRed50),
                        )

                        Spacer(modifier = Modifier.width(2.dp))

                        LottoMateText(
                            text = "개",
                            style = LottoMateTheme.typography.headline1,
                        )
                    }
                }

                Divider(
                    modifier = Modifier.size(width = 1.dp, height = 40.dp),
                    color = Color(0xFFD9D9D9),
                )

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LottoMateText(
                        text = stringResource(id = R.string.pocket_title_my_number_possible_check),
                        style = LottoMateTheme.typography.label2,
                    )

                    Row {
                        LottoMateText(
                            text = "내 로또",
                            style = LottoMateTheme.typography.headline1,
                        )

                        Spacer(modifier = Modifier.width(2.dp))

                        LottoMateText(
                            text = "NN",
                            style = LottoMateTheme.typography.headline1
                                .copy(color = LottoMateRed50),
                        )

                        Spacer(modifier = Modifier.width(2.dp))

                        LottoMateText(
                            text = "개",
                            style = LottoMateTheme.typography.headline1,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = LottoMateGray10, shape = RoundedCornerShape(Dimens.RadiusLarge))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_lotto645_rank_first),
                        contentDescription = null,
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    LottoMateText(
                        text = "로또 당첨 발표까지",
                        style = LottoMateTheme.typography.body1,
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    LottoMateText(
                        text = "N일",
                        style = LottoMateTheme.typography.headline2
                            .copy(color = LottoMateRed50),
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    LottoMateText(
                        text = "남았어요.",
                        style = LottoMateTheme.typography.body1,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_lotto720_rank_first),
                        contentDescription = null,
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    LottoMateText(
                        text = "연금복권 당첨 발표까지",
                        style = LottoMateTheme.typography.body1,
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    LottoMateText(
                        text = "N일",
                        style = LottoMateTheme.typography.headline2
                            .copy(color = LottoMateRed50),
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    LottoMateText(
                        text = "남았어요.",
                        style = LottoMateTheme.typography.body1,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LottoMateText(
                text = "역대 당첨금 확인하기",
                style = LottoMateTheme.typography.caption1
                    .copy(color = LottoMateGray100),
            )
            
            Spacer(modifier = Modifier.width(4.dp))
            
            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_right),
                contentDescription = null,
                tint = LottoMateGray100,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Composable
internal fun MyLottoHistory(
    modifier: Modifier = Modifier,
    myLottoHistories: List<LottoDetail> = emptyList(),
    onClickEdit: () -> Unit,
    onClickCheckWin: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        LottoMateText(
            text = "내 로또 내역",
            style = LottoMateTheme.typography.headline1,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = LottoMateGray10, shape = RoundedCornerShape(Dimens.RadiusLarge))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            ) {
                Row {
                    LottoMateText(
                        text = "2024년 8월부터 로또를 총",
                        style = LottoMateTheme.typography.body1,
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    LottoMateText(
                        text = "NN개",
                        style = LottoMateTheme.typography.body2,
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    LottoMateText(
                        text = "구매했어요.",
                        style = LottoMateTheme.typography.body1,
                    )
                }

                Row {
                    LottoMateText(
                        text = "당첨 N개 미당첨 N개로 내 당첨률은",
                        style = LottoMateTheme.typography.body1,
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    LottoMateText(
                        text = "NN%",
                        style = LottoMateTheme.typography.body2,
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    LottoMateText(
                        text = "이에요.",
                        style = LottoMateTheme.typography.body1,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        val lottoHistoriesByRound = myLottoHistories.groupBy { it.round }

        Column {
            LottoMateText(
                text = "편집",
                textAlign = TextAlign.End,
                style = LottoMateTheme.typography.label2
                    .copy(color = LottoMateGray80),
                modifier = Modifier
                    .fillMaxWidth()
                    .noInteractionClickable { onClickEdit() },
            )

            lottoHistoriesByRound.forEach { (round, lottoDetails) ->
                Column(
                    modifier = Modifier.padding(bottom = 16.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row(
                            verticalAlignment = Alignment.Bottom,
                        ) {
                            LottoMateText(
                                text = round.toString().plus("회차"),
                                style = LottoMateTheme.typography.label1,
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            LottoMateText(
                                text = lottoDetails.first().date,
                                style = LottoMateTheme.typography.caption1
                                    .copy(color = LottoMateGray80),
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    lottoDetails.forEach { detail ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Image(
                                    painter = painterResource(
                                        id = when (detail.type) {
                                            LottoType.L645 -> R.drawable.icon_lotto645_rank_first
                                            LottoType.L720 -> R.drawable.icon_lotto720_rank_first
                                            else -> R.drawable.icon_speetto_rank_first
                                        }
                                    ),
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                detail.numbers.forEachIndexed { index, number ->
                                    when (detail.type) {
                                        LottoType.L645 -> {
                                            LottoBall645(
                                                number = number,
                                            )

                                            Spacer(modifier = Modifier.width(8.dp))
                                        }

                                        LottoType.L720 -> {
                                            LottoBall720(
                                                index = index,
                                                number = number,
                                                isBonusNumber = false,
                                            )

                                            if (index == 0) {
                                                Spacer(modifier = Modifier.width(4.dp))

                                                LottoMateText(
                                                    text = "조",
                                                    style = LottoMateTheme.typography.label2,
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(6.dp))
                                        }
                                        else -> {}
                                    }
                                }
                            }

                            when (detail.condition) {
                                LottoCondition.NOT_WON -> {
                                    LottoMateTextButton(
                                        buttonText = "미당첨",
                                        buttonSize = LottoMateButtonProperty.Size.SMALL,
                                        textColor = LottoMateError,
                                        onClick = {},
                                    )
                                }
                                LottoCondition.CHECKED_WIN -> {
                                    LottoMateTextButton(
                                        buttonText = "당첨",
                                        buttonSize = LottoMateButtonProperty.Size.SMALL,
                                        textColor = LottoMatePositive,
                                        onClick = {},
                                    )
                                }
                                LottoCondition.NOT_CHECKED -> {
                                    LottoMateOutLineButton(
                                        text = "확인",
                                        buttonSize = LottoMateButtonProperty.Size.XSMALL,
                                        onClick = { onClickCheckWin() },
                                    )
                                }
                                LottoCondition.NOT_CHECKED_END -> {
                                    LottoMateOutLineButton(
                                        text = "확인",
                                        buttonSize = LottoMateButtonProperty.Size.XSMALL,
                                        buttonBorderColor = LottoMateGray60,
                                        textColor = LottoMateGray60,
                                        onClick = {}
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyNumberContentPreview() {
    LottoMateTheme {
        MyLottoHistory(
            modifier = Modifier.background(Color.White),
            myLottoHistories = mockLottoDetails,
            onClickEdit = {},
            onClickCheckWin = {}
        )
    }
}