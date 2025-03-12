package com.lottomate.lottomate.presentation.screen.onboarding.model

import androidx.annotation.DrawableRes
import com.lottomate.lottomate.R

enum class OnboardingType(val title: String, @DrawableRes val img: Int) {
    Onboarding01(
        title = "내 주위에 있는\n로또 명당을 알려줘요",
        img = R.drawable.img_onboarding01,
    ),
    Onboarding02(
        title = "빠르게 내 로또 당첨을\n확인해요",
        img = R.drawable.img_onboarding02,
    ),
    Onboarding03(
        title = "생생한 로또 당첨 후기를\n들어보세요",
        img = R.drawable.img_onboarding03,
    ),
    Onboarding04(
        title = "나만의 행운 번호를\n뽑아봐요",
        img = R.drawable.img_onboarding04,
    ),
    Onboarding05(
        title = "설레는 로또 당첨,\n로또메이트와 함께 해요",
        img = R.drawable.img_onboarding05,
    ),
}