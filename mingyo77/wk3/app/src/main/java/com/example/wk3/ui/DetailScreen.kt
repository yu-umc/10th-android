package com.example.wk3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wk3.data.Product

@Composable
fun ProductDetailScreen(
    productName: String,
    products: List<Product>,
    onBackClick: () -> Unit
) {
    val product = remember(productName, products) {
        products.firstOrNull { it.name == productName }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Button(onClick = onBackClick) {
            Text(text = "뒤로가기")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (product == null) {
            Text(text = "상품 정보를 찾을 수 없습니다.")
            return
        }

        Image(
            painter = painterResource(id = product.image),
            contentDescription = product.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = product.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = product.price,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = if (product.explain.isBlank()) "상품 설명이 없습니다." else product.explain,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}
