package com.lottomate.lottomate.presentation.res

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import com.lottomate.lottomate.R

object Strings {

}

object StringArrays {
    val Lotto645WinConditions: Array<String>
        @Composable get() = stringArrayResource(id = R.array.lotto645_win_condition)
    val Lotto720WinConditions: Array<String>
        @Composable get() = stringArrayResource(id = R.array.lotto720_win_condition)
    val Lotto720WinPrizes: Array<String>
        @Composable get() = stringArrayResource(id = R.array.lotto720_win_prize)
}