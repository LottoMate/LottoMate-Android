package com.lottomate.lottomate.presentation.component

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun LottoMateBackHandler() {
    val context = LocalContext.current
    var backHandlerTime = System.currentTimeMillis()
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler {
        if (System.currentTimeMillis() - backHandlerTime <= 400L) {
            showExitDialog = true
        }

        backHandlerTime = System.currentTimeMillis()
    }

    if (showExitDialog) {
        LottoMateDialog(
            title = """
                행운 가득 로또메이트를
                종료하시겠습니까?
            """.trimIndent(),
            cancelText = "아니오",
            confirmText = "네",
            onDismiss = { showExitDialog = false },
            onConfirm = { (context as Activity).finish() },
        )
    }
}
