package com.example.wk3.ui

import com.example.wk3.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wk3.data.Product

@Composable
fun HomeScreen(
    products: List<Product>,
    onProductClick: (Product) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item {
        Text(
            text = "Discover",
            modifier = Modifier
                .padding(start = 40.dp, top = 40.dp),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )
        Text(
            text = "9월 4일 목요일",
            modifier = Modifier
                .padding(start = 40.dp, top = 10.dp, bottom = 20.dp),
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        )
        }
        item {
        Image(
            painter = painterResource(id=R.drawable.ic_homelogo_background),
            contentDescription = "Main Banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
        )
        }
        item {
            Text(
                text = "What's New",
                modifier = Modifier
                    .padding(start = 24.dp, top = 16.dp)
            )
            Text(
                text = "나이키 최신 상품",
                modifier = Modifier
                    .padding(start = 24.dp, top = 8.dp),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = products,
                    key = { product -> product.name }
                ) { product ->
                    ProductCard(
                        product = product,
                        onClick = { onProductClick(product) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp)
    ) {
        Image(
            painter = painterResource(id = product.image),
            contentDescription = product.name,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(0.55f)
        )
        Text(
            text = product.name,
            modifier = Modifier.padding(top = 8.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )
        Text(
            text = product.price,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            color = Color.Gray
        )
    }
}
