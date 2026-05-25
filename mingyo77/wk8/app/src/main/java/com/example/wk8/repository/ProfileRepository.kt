package com.example.wk8.repository

import com.example.wk8.model.UserListResponse
import com.example.wk8.model.UserResponse

interface ProfileRepository {
    suspend fun getUserInfo(): UserResponse
    suspend fun getFollowingList(page: Int): UserListResponse
}
