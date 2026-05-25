package com.example.wk8.repository

import com.example.wk8.data.RetrofitClient
import com.example.wk8.data.UserService
import com.example.wk8.model.UserListResponse
import com.example.wk8.model.UserResponse

class ProfileRepositoryImpl(
    private val userService: UserService = RetrofitClient.userService,
) : ProfileRepository {
    override suspend fun getUserInfo(): UserResponse = userService.getUserById(1)

    override suspend fun getFollowingList(page: Int): UserListResponse = userService.getUsers(page)
}
