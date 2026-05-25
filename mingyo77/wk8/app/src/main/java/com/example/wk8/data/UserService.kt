package com.example.wk8.data

import com.example.wk8.model.UserListResponse
import com.example.wk8.model.UserResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import java.net.HttpURLConnection
import java.net.URL

interface UserService {
    @Headers("x-api-key: reqres_2f04e79a468f4cfe8ac30e593c43ee12")
    @GET("api/users/{id}")
    suspend fun getUserById(
        @Path("id") id: Int
    ): UserResponse

    @Headers("x-api-key: reqres_2f04e79a468f4cfe8ac30e593c43ee12")
    @GET("api/users")
    suspend fun getUsers(
        @Query("page") page: Int
    ): UserListResponse
}