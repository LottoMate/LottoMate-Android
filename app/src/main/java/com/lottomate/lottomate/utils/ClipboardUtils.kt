package com.lottomate.lottomate.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build

/**
 * Text를 복사할 때 사용되는 ClipboardUtils입니다.
 */
class ClipboardUtils {
    companion object {
        fun copyToClipboard(context: Context, copyText: String, onSuccess: () -> Unit,) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("Copied Text", copyText))

            // API 33 이하일 경우, 스낵바 표시
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) onSuccess()
        }
    }
}