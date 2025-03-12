package com.lottomate.lottomate.presentation.screen.onboarding.model

import androidx.annotation.DrawableRes
import com.lottomate.lottomate.R

enum class PermissionType(
    val title: String,
    val subTitle: String,
    @DrawableRes val img: Int,
) {
    CAMERA(
        title = "카메라(선택)",
        subTitle = "복권의 QR코드로\n당첨을 확인할 수 있어요",
        img = R.drawable.img_permission_camera,
    ),
    CALL(
        title = "전화(선택)",
        subTitle = "로또 판매점 및 당첨금 지급 장소에\n전화할 수 있어요",
        img = R.drawable.img_permission_call,
    ),
    LOCATION(
        title = "사용자 위치 정보(선택)",
        subTitle = "내 주변의 로또 판매점을\n확인할 수 있어요",
        img = R.drawable.img_permission_location,
    ),
}