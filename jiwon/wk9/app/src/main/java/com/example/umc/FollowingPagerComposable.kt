package com.example.umc

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FollowingHorizontalPager(
    users: List<ReqResUser>,
    modifier: Modifier = Modifier
) {
    MaterialTheme {
        if (users.isEmpty()) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "팔로잉 데이터를 불러오는 중입니다",
                    color = Color(0xFF777777),
                    fontSize = 13.sp
                )
            }
        } else {
            val pagerState = rememberPagerState(pageCount = { users.size })

            Column(
                modifier = modifier.fillMaxWidth()
            ) {
                HorizontalPager(
                    state = pagerState,
                    pageSize = PageSize.Fixed(160.dp),
                    contentPadding = PaddingValues(horizontal = 0.dp),
                    pageSpacing = 12.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(178.dp)
                ) { page ->
                    val user = users[page]

                    Card(
                        modifier = Modifier.fillMaxHeight(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            AsyncImage(
                                model = user.avatar,
                                contentDescription = "${user.nickname} 프로필 이미지",
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                            )

                            Text(
                                text = user.nickname,
                                modifier = Modifier.padding(top = 14.dp),
                                color = Color(0xFF111111),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = user.email,
                                modifier = Modifier.padding(top = 6.dp),
                                color = Color(0xFF777777),
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(users.size) { index ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(width = if (pagerState.currentPage == index) 20.dp else 14.dp, height = 6.dp)
                                .clip(RoundedCornerShape(99.dp))
                                .background(
                                    if (pagerState.currentPage == index) Color(0xFF111111)
                                    else Color(0xFFD9D9D9)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
