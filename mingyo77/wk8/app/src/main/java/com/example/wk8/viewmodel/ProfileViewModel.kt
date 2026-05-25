package com.example.wk8.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wk8.model.UserData
import com.example.wk8.model.UserResponse
import com.example.wk8.repository.ProfileRepository
import com.example.wk8.repository.ProfileRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository = ProfileRepositoryImpl(),
) : ViewModel() {
    private val _userData = MutableStateFlow<UserResponse?>(null)
    val userData: StateFlow<UserResponse?> = _userData.asStateFlow()

    private val _followingList = MutableStateFlow<List<UserData>>(emptyList())
    val followingList: StateFlow<List<UserData>> = _followingList.asStateFlow()

    fun loadProfile() {
        if (_userData.value != null) return

        viewModelScope.launch {
            _userData.value = repository.getUserInfo()
            _followingList.value = repository.getFollowingList(page = 1).data
        }
    }
}
