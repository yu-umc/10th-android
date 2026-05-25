package com.example.wk8.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.wk8.R

@Composable
fun AvatarImage(
    imageUrl: String?,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        placeholder = painterResource(R.drawable.ic_user),
        error = painterResource(R.drawable.ic_user),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(CircleShape)
            .border(1.dp, Color(0xFFEDEDED), CircleShape),
    )
}