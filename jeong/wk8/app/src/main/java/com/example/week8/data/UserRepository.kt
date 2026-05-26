package com.example.week8.data

import com.example.week8.model.ReqresUser

/**
 * wk5 [UserRemoteRepositoryImpl] — Reqres API 호출을 object로 캡슐화.
 */
object UserRepository {

    suspend fun getProfile(userId: Int): ReqresUser {
        val response = ReqresClient.authService.getUserById(userId)
        if (!response.isSuccessful) error("유저 정보 조회 실패: ${response.code()}")
        return response.body()?.data ?: error("유저 데이터가 비어 있습니다.")
    }

    suspend fun getFollowing(page: Int = 1, perPage: Int = 3): List<ReqresUser> {
        val response = ReqresClient.authService.getUsers(page = page, perPage = perPage)
        if (!response.isSuccessful) error("팔로잉 목록 조회 실패: ${response.code()}")
        return response.body()?.data ?: error("팔로잉 데이터가 비어 있습니다.")
    }
}
