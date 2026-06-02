package com.example.nikeappcompose.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.nikeappcompose.ApiClient
import com.example.nikeappcompose.R
import com.example.nikeappcompose.UserData
import kotlinx.coroutines.async
import kotlin.math.absoluteValue

private const val API_KEY = "reqres_df73b9dd436f45ee8ae8925f9dd0f2aa"

@Composable
fun ProfileScreen() {

    var user by remember { mutableStateOf<UserData?>(null) }
    var userList by remember { mutableStateOf<List<UserData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    // Recomposition 확인용 — DEBUG 빌드에서만 로그 출력
    SideEffect {
        Log.d("ProfileScreen", "Recomposition 발생")
    }
    // 두 API 호출을 async로 병렬 실행
    LaunchedEffect(Unit) {
        try {
            val userDeferred = async { ApiClient.userService.getUser(API_KEY) }
            val listDeferred = async { ApiClient.userService.getUserList(API_KEY) }

            val userResponse = userDeferred.await()
            val listResponse = listDeferred.await()

            Log.d("API_TEST", "userResponse 성공: ${userResponse.isSuccessful}")
            Log.d("API_TEST", "listResponse 성공: ${listResponse.isSuccessful}")
            Log.d("API_TEST", "listResponse body: ${listResponse.body()}")

            if (userResponse.isSuccessful) user = userResponse.body()?.data
            if (listResponse.isSuccessful) userList = listResponse.body()?.data ?: emptyList()

            Log.d("USER_COUNT", "유저 수: ${userList.size}")
        } catch (e: Exception) {
            e.printStackTrace()
            hasError = true
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        // 메인 프로필 이미지 — 공통 컴포저블 재사용
        ProfileAvatar(
            url = user?.avatar,
            contentDescription = "프로필",
            modifier = Modifier.size(80.dp)
        )

        Text(
            text = "${user?.firstName ?: ""} ${user?.lastName ?: ""}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = user?.email ?: "",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = "팔로잉 목록",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
        )

        // 로딩 / 에러 / 빈 목록 / 정상 상태를 when으로 명확히 분기
        when {
            isLoading -> CircularProgressIndicator()
            hasError -> Text(text = "데이터를 불러오는데 실패했습니다.", color = Color.Red)
            userList.isEmpty() -> Text(text = "유저 데이터가 없습니다.", color = Color.Red)
            else -> {
                val pagerState = rememberPagerState(pageCount = { userList.size })

                HorizontalPager(
                    state = pagerState,
                    // 양옆 페이지를 살짝 노출해 스와이프 힌트 제공
                    contentPadding = PaddingValues(horizontal = 32.dp),
                    pageSpacing = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(Color.LightGray)
                ) { page ->
                    val followUser = userList[page]

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        ProfileAvatar(
                            url = followUser.avatar,
                            contentDescription = followUser.firstName,
                            modifier = Modifier.size(80.dp)
                        )

                        Text(
                            text = "${followUser.firstName} ${followUser.lastName}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Text(
                            text = followUser.email,
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }

                // 페이지 인디케이터 — currentPageOffsetFraction으로 부드러운 전환
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    horㅇizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(userList.size) { index ->
                        val pageOffset by remember {
                            derivedStateOf {
                                ((pagerState.currentPage - index) + pagerState.currentPageOffsetFraction).absoluteValue
                            }
                        }
                        val fraction = (1f - pageOffset.coerceIn(0f, 1f))

                        Box(
                            modifier = Modifier
                                .size(lerp(8.dp, 10.dp, fraction))
                                .clip(CircleShape)
                                .background(lerp(Color.Gray, Color.Black, fraction))
                        )
                    }
                }
            }
        }
    }
}

// AsyncImage 중복을 제거한 공통 컴포저블
@Composable
private fun ProfileAvatar(
    url: String?,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // url이 바뀔 때만 ImageRequest를 재생성
    val request = remember(url) {
        ImageRequest.Builder(context)
            .data(url)
            .crossfade(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .build()
    }
    AsyncImage(
        model = request,
        contentDescription = contentDescription,
        placeholder = painterResource(R.drawable.ic_profile),
        error = painterResource(R.drawable.ic_profile),
        fallback = painterResource(R.drawable.ic_profile),
        modifier = modifier.clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}
