package com.example.week8.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.week8.R
import com.example.week8.ui.components.FollowingItem
import com.example.week8.viewmodel.ProfileViewModel

/**
 * wk5 [fragment_user.xml] + [UserFragment] UI/API 마이그레이션.
 * ReqRes userId=1, 팔로잉은 HorizontalPager.
 */
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserPage()
    }

    val nickname = uiState.profile?.displayName ?: "홍길동"
    val avatarUrl = uiState.profile?.avatar
    val benefitCount = uiState.benefitCountText
    val following = uiState.following
    val pagerState = rememberPagerState(pageCount = { following.size.coerceAtLeast(1) })

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.white)),
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ProfileAvatar(avatarUrl = avatarUrl)
                Text(
                    text = nickname,
                    color = colorResource(R.color.black),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 14.dp),
                )
                Surface(
                    onClick = { },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .width(180.dp)
                        .height(51.dp),
                    shape = RoundedCornerShape(25.dp),
                    color = colorResource(R.color.white),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        colorResource(R.color.gray_300),
                    ),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "프로필 수정",
                            color = colorResource(R.color.black),
                            fontSize = 18.sp,
                        )
                    }
                }
            }
        }

        item {
            ProfileMenuRow()
        }

        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(10.dp)
                    .background(colorResource(R.color.gray_100)),
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(101.dp)
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "나이키 멤버 혜택",
                        color = colorResource(R.color.black),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = benefitCount,
                        color = colorResource(R.color.gray_500),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
                Image(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                        .rotate(180f),
                )
            }
        }

        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(colorResource(R.color.gray_100)),
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 18.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "팔로잉 (${following.size})",
                    color = colorResource(R.color.black),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = "편집",
                    color = colorResource(R.color.gray_700),
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { },
                )
            }
        }

        item {
            if (following.isNotEmpty()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(88.dp)
                        .padding(horizontal = 16.dp),
                    pageSpacing = 8.dp,
                ) { page ->
                    FollowingItem(avatarUrl = following[page].avatar)
                }
            } else if (!uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(88.dp)
                        .padding(horizontal = 16.dp),
                )
            }
        }

        if (uiState.isLoading) {
            item {
                Text(
                    text = "불러오는 중...",
                    color = colorResource(R.color.gray_500),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
            }
        }

        if (!uiState.errorMessage.isNullOrBlank()) {
            item {
                Text(
                    text = uiState.errorMessage.orEmpty(),
                    color = colorResource(R.color.gray_700),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
            }
        }

        item {
            Spacer(modifier = Modifier.fillParentMaxHeight())
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .background(colorResource(R.color.gray_100)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "회원 가입일: 2025년 9월",
                    color = colorResource(R.color.gray_500),
                    fontSize = 14.sp,
                )
            }
        }
    }
}

@Composable
private fun ProfileAvatar(avatarUrl: String?) {
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(colorResource(R.color.gray_300)),
    ) {
        if (!avatarUrl.isNullOrBlank()) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "프로필 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun ProfileMenuRow() {
    val menus = listOf(
        R.drawable.archive to "주문",
        R.drawable.identificationcard to "패스",
        R.drawable.calendarblank to "이벤트",
        R.drawable.gear to "설정",
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .height(72.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        menus.forEachIndexed { index, (iconRes, label) ->
            if (index > 0) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(20.dp)
                        .background(colorResource(R.color.gray_300)),
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = label,
                    modifier = Modifier.size(22.dp),
                )
                Text(
                    text = label,
                    color = colorResource(R.color.black),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
        }
    }
}
