package com.example.nikeappcompose.screen

import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nikeappcompose.R
import kotlinx.coroutines.launch

data class Product(
    val name: String,
    val price: String,
    val imageRes: Int = R.drawable.home_bg
)

@Composable
fun ShopScreen(
    wishlist: List<Product> = emptyList(),
    onToggleWishlist: (Product) -> Unit = {}
) {

    //TabRow + HorizontalPager 연동
    val tabs = listOf("전체", "Tops & T-Shirts", "Sale")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    val products = listOf(
        Product("Nike Everyday Plus", "₩10,000"),
        Product("Nike Elite Crew", "₩16,000"),
        Product("Nike Air Force 1", "₩115,000"),
        Product("Jordan ENike", "₩115,000")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            contentColor = Color.Black,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = Color.Black
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            color = if (pagerState.currentPage == index) Color.Black else Color.Gray
                        )
                    }
                )
            }
        }

        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        items = products,
                        key = { product -> product.name }
                    ) { product ->
                        ProductItem(
                            product = product,
                            isLiked = wishlist.contains(product),
                            onToggleWishlist = { onToggleWishlist(product) }
                        )
                    }
                }
                1 -> Box(modifier = Modifier.fillMaxSize())
                2 -> Box(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(
    product: Product,
    isLiked: Boolean = false,
    onToggleWishlist: () -> Unit = {}
) {
    var showSheet by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(4.dp)
            .clickable { showSheet = true }
    ) {
        Box {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = { openDialog = true },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "하트",
                    tint = if (isLiked) Color.Red else Color.White
                )
            }
        }
        Text(
            text = product.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = product.price,
            fontSize = 11.sp,
            color = Color.Gray
        )
    }

    // AlertDialog
    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = { Text("위시리스트에 추가") },
            text = { Text("이 상품을 위시리스트에 추가하시겠습니까?") },
            confirmButton = {
                TextButton(onClick = {
                    onToggleWishlist()
                    openDialog = false
                }) {
                    Text("추가")
                }
            },
            dismissButton = {
                TextButton(onClick = { openDialog = false }) {
                    Text("취소")
                }
            }
        )
    }

    // ModalBottomSheet
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "사이즈 선택",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
                listOf("S", "M", "L", "XL").forEach { size ->
                    Button(
                        onClick = {
                            scope.launch { sheetState.hide() }
                                .invokeOnCompletion { showSheet = false }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text(size, color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}