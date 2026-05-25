package com.example.wk8.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wk8.R
import com.example.wk8.model.UserData
import com.example.wk8.ui.components.AvatarImage
import com.example.wk8.ui.components.FollowingItem
import com.example.wk8.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(),
) {
    val userData by viewModel.userData.collectAsState()
    val followingList by viewModel.followingList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
    ) {
        ProfileHeader(
            user = userData?.data,
        )
        ProfileShortcutRow()
        SectionDivider()
        MembershipSection()
        SectionDivider()
        FollowingSection(followingList = followingList)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "회원가입일: 2025년",
            color = Color(0xFF9B9A9A),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x66F6D1D1))
                .padding(vertical = 14.dp),
        )
    }
}

@Composable
private fun ProfileHeader(
    user: UserData?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 36.dp, bottom = 20.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
    ) {
        AvatarImage(
            imageUrl = user?.avatar,
            contentDescription = "Profile image",
            modifier = Modifier.size(100.dp),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = when {
                user != null -> "${user.firstName} ${user.lastName}"
                else -> "프로필"
            },
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Button(
            onClick = {},
            modifier = Modifier
                .padding(top = 24.dp)
                .size(width = 180.dp, height = 48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black,
            ),
            border = BorderStroke(1.dp, Color(0xFFF6D1D1)),
        ) {
            Text(text = "프로필 수정")
        }
    }
}

@Composable
private fun ProfileShortcutRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ProfileShortcut(title = "주문", modifier = Modifier.weight(1f))
        VerticalDivider()
        ProfileShortcut(title = "쿠폰", modifier = Modifier.weight(1f))
        VerticalDivider()
        ProfileShortcut(title = "리뷰", modifier = Modifier.weight(1f))
        VerticalDivider()
        ProfileShortcut(title = "설정", modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ProfileShortcut(
    title: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_heartstraight),
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = Color.Black,
        )
        Text(
            text = title,
            color = Color.Black,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 10.dp),
        )
    }
}

@Composable
private fun VerticalDivider() {
    Spacer(
        modifier = Modifier
            .height(30.dp)
            .size(width = 1.dp, height = 30.dp)
            .background(Color(0xFFF6D1D1)),
    )
}

@Composable
private fun MembershipSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
    ) {
        Text(
            text = "나이키 멤버 혜택",
            color = Color.Black,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = "0개 사용가능",
            color = Color(0xFFCABCBC),
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 6.dp),
        )
    }
}

@Composable
private fun FollowingSection(followingList: List<UserData>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 20.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "팔로잉",
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(text = "편집", color = Color(0xFF8A8888), fontSize = 14.sp)
        }
        LazyRow(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(
                items = followingList,
                key = { user -> user.id },
            ) { user ->
                FollowingItem(user = user)
            }
        }
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(
        thickness = 7.dp,
        color = Color(0x66F6D1D1),
        modifier = Modifier.padding(top = 16.dp),
    )
}
