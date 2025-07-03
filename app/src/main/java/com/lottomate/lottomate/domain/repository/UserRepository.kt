package com.lottomate.lottomate.domain.repository

import com.lottomate.lottomate.domain.model.LoginType
import com.lottomate.lottomate.domain.model.UserProfile
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val userProfile: StateFlow<UserProfile?>

    suspend fun setLatestLoginInfo(type: LoginType): Result<Unit>
    suspend fun setUserProfile(profile: UserProfile?): Result<Unit>
    suspend fun updateUserNickName(nickName: String): Result<Unit>
}