package com.example.week8.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week8.data.UserRepository
import com.example.week8.model.ReqresUser
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val profile: ReqresUser? = null,
    val following: List<ReqresUser> = emptyList(),
) {
    val benefitCountText: String
        get() {
            val count = profile?.let { (it.id - 1).coerceAtLeast(0) } ?: 0
            return "${count}개 사용 가능"
        }
}

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadUserPage() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            runCatching {
                val profileDeferred = async { UserRepository.getProfile(USER_ID) }
                val followingDeferred = async { UserRepository.getFollowing(page = 1, perPage = 3) }
                profileDeferred.await() to followingDeferred.await()
            }.onSuccess { (profile, following) ->
                _uiState.value = ProfileUiState(
                    isLoading = false,
                    errorMessage = null,
                    profile = profile,
                    following = following,
                )
            }.onFailure { throwable ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = throwable.message ?: "마이페이지 로드에 실패했습니다.",
                )
            }
        }
    }

    companion object {
        private const val USER_ID = 1
    }
}
