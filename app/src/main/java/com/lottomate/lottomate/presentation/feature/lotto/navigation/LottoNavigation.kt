package com.lottomate.lottomate.presentation.feature.lotto.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lottomate.lottomate.presentation.feature.lottoinfo.LottoInfoScreen
import com.lottomate.lottomate.presentation.navigation.LottoRoute

fun NavGraphBuilder.lottoNavGraph(
    padding: PaddingValues,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable(LottoRoute.INFO.name) {
        LottoInfoScreen(
            onBackPressed = {}
        )
    }
}