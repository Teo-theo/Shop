package com.example.doanltd.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doanltd.Navigation.Screen
import com.example.doanltd.R

data class Order(
    val orderId: String,
    val status: String,
    val value: Int,
    val dateRecorded: String,
    val lastUpdate: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(navController: NavController) {
    val Orange = Color(0xFFE7A953)
    val orders = remember {
        mutableStateOf(
            listOf(
                Order("HD001", "Hoàn thành", 180000, "27/11/2019 11:52", "01/12/2019 11:06"),
                Order("HD002", "Đang giao dịch", 180000, "24/11/2019 19:14", "24/11/2019 19:14"),
                Order("HD002", "Đang giao dịch", 180000, "23/11/2019 14:59", "23/11/2019 14:59")
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Orange
                ),
                title = { Text("Lịch sử mua hàng") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton (
                onClick = {
                    navController.navigate(Screen.Review.route.replace("{productId}", "default"))
                },
                containerColor = Orange ,
                contentColor = Color.White,
                shape = CircleShape
            ){
                Icon(Icons.Default.Star,contentDescription = null)
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Orange
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
                    label = { Text("Cart") },
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.Cart.route)
                    }
                )
                //123
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.history),
                        modifier = Modifier.size(30.dp), contentDescription = "History") },
                    label = { Text("History") },
                    selected = true,
                    onClick = {
                        navController.navigate(Screen.OrderHistory.route)
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { navController.navigate(Screen.Home.route) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.Profile.route)
                    }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(orders.value) { order ->
                OrderRow(order)
                Divider(thickness = 0.5.dp, color = Color.LightGray)
            }
        }
    }
}

//@Composable
//fun OrderTableHeader() {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(
//            "Mã đơn hàng",
//            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
//            modifier = Modifier.weight(1f)
//        )
//        Text(
//            "Trạng thái",
//            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
//            modifier = Modifier.weight(1f)
//        )
//        Text(
//            "Thanh toán",
//            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
//            modifier = Modifier.weight(1f)
//        )
//        Text(
//            "Ngày ghi nhận",
//            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
//            modifier = Modifier.weight(1.5f)
//        )
//        Text(
//            "Cập nhật cuối",
//            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
//            modifier = Modifier.weight(1.5f)
//        )
//    }
//}

@Composable
fun OrderRow(order: Order) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(order.orderId, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
//        Text(
//            order.status,
//            style = MaterialTheme.typography.bodySmall.copy(
//                color = if (order.status == "Hoàn thành") Color.Green else Color.Blue
//            ),
//            modifier = Modifier.weight(1f)
//        )
//        Text("${order.value}₫", style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
//        Text(order.dateRecorded, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1.5f))
//        Text(order.lastUpdate, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1.5f))
//    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF8DC)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "#${order.orderId}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.status,
                    color = Color(0xFF4CAF50),
                    fontSize = 14.sp
                )
                Text(
                    text = "${order.value} đ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Text(
                text = "Ngày: ${order.dateRecorded}",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
