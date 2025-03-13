package com.lottomate.lottomate.presentation.screen.onboarding

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.lottomate.lottomate.data.datastore.LottoMateDataStore
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.onboarding.model.PermissionType
import com.lottomate.lottomate.presentation.ui.LottoMateGray10
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import kotlinx.coroutines.launch

@Composable
fun PermissionNoticeRoute(
    moveToHome: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        coroutineScope.launch {
            LottoMateDataStore.changeOnBoardingState()
        }.invokeOnCompletion { moveToHome() }
    }

    PermissionNoticeScreen(
        onClickRequestPermission = {
            permissionLauncher.launch(
                input = when {
                    Build.VERSION_CODES.TIRAMISU >= Build.VERSION.SDK_INT -> {
                        com.lottomate.lottomate.utils.PermissionType.AllPermissionAPI33.permissions.toTypedArray()
                    }
                    else -> com.lottomate.lottomate.utils.PermissionType.AllPermission.permissions.toTypedArray()
                }
            )
        },
    )
}
@Composable
private fun PermissionNoticeScreen(
    onClickRequestPermission: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(
                top = WindowInsets.statusBars
                    .asPaddingValues()
                    .calculateTopPadding(),
                bottom = WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateBottomPadding(),
            )
            .fillMaxSize()
            .background(LottoMateWhite)
            .padding(horizontal = Dimens.DefaultPadding20),
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 84.dp)
                .align(Alignment.Center),
        ) {
            LottoMateText(
                text = "더 편리한 사용을 위해\n앱 접근 권한을 확인해주세요",
                style = LottoMateTheme.typography.title3,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 44.dp)
                    .background(LottoMateGray10, RoundedCornerShape(10.dp))
                    .padding(horizontal = 28.dp),
            ) {
                PermissionNoticeItem(
                    type = PermissionType.CAMERA,
                )

                PermissionNoticeItem(
                    type = PermissionType.CALL,
                )

                PermissionNoticeItem(
                    type = PermissionType.LOCATION
                )

                LottoMateText(
                    text = "*선택사항으로 접근 허용하지 않아도 로또메이트를 이용할 수 있어요",
                    style = LottoMateTheme.typography.caption2
                        .copy(color = LottoMateGray100),
                    modifier = Modifier.padding(vertical = 24.dp),
                )
            }
        }

        LottoMateSolidButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 36.dp),
            text = "확인",
            buttonSize = LottoMateButtonProperty.Size.LARGE,
            onClick = onClickRequestPermission,
        )
    }
}

@Composable
private fun PermissionNoticeItem(
    type: PermissionType,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            bitmap = ImageBitmap.imageResource(id = type.img),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp),
        )

        Column(
            modifier = Modifier
                .padding(start = 20.dp)
                .fillMaxWidth(),
        ) {
            LottoMateText(
                text = type.title,
                style = LottoMateTheme.typography.headline2,
                modifier = Modifier.fillMaxWidth(),
            )

            LottoMateText(
                text = type.subTitle,
                style = LottoMateTheme.typography.body2
                    .copy(color = LottoMateGray100),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
            )
        }
    }
}