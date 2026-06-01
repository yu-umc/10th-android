package com.example.umc

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ReqResApiService {
    @GET("api/users/{userId}")
    suspend fun getUser(
        @Path("userId") userId: Int,
        @Header("x-api-key") apiKey: String
    ): Response<ReqResSingleUserResponse>

    @GET("api/users")
    suspend fun getUsers(
        @Query("page") page: Int = 1,
        @Header("x-api-key") apiKey: String
    ): Response<ReqResUserListResponse>
}

object RetrofitClient {
    private const val BASE_URL = "https://reqres.in/"

    // jiwon/wk6에서 실제로 성공했던 ReqRes API Key를 우선 사용하고,
    // ReqRes 무료 예제 키도 예비로 둬서 401/403 상황에 한 번 더 시도합니다.
    val apiKeys = listOf(
        "reqres_0fdb12297a944572a572183840092802",
        "reqres-free-v1"
    )

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ReqResApiService by lazy {
        retrofit.create(ReqResApiService::class.java)
    }
}
