package com.lottomate.lottomate.data.remote.repository

import com.lottomate.lottomate.data.datastore.LottoMateDataStore
import com.lottomate.lottomate.domain.model.LatestLoginInfo
import com.lottomate.lottomate.domain.model.LoginType
import com.lottomate.lottomate.domain.model.UserProfile
import com.lottomate.lottomate.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

val testUserProfile = UserProfile(nickname = "nickname")

class UserRepositoryImpl @Inject constructor(

) : UserRepository {
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    override val userProfile: StateFlow<UserProfile?>
        get() = _userProfile.asStateFlow()

    override suspend fun setLatestLoginInfo(type: LoginType): Result<Unit> {
        return try {
            val loginInfo = LatestLoginInfo(type = type, date = System.currentTimeMillis())

            LottoMateDataStore.saveLatestLoginInfo(loginInfo)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun setUserProfile(profile: UserProfile?): Result<Unit> {
        return try {
            _userProfile.update { profile }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserNickName(nickName: String): Result<Unit> {
        return try {
            _userProfile.update { it?.copy(nickname = nickName) }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}