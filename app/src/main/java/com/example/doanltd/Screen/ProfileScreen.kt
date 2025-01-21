package com.example.doanltd.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doanltd.AppDatabase
import com.example.doanltd.Navigation.Screen
import com.example.doanltd.R
import com.example.doanltd.RoomDatabase.NgDungRoom.NgDungEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    var user by remember { mutableStateOf<NgDungEntity?>(null) }
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context).ngDungDao()

    LaunchedEffect(Unit) {
        // Di chuyển việc truy vấn vào coroutine
        CoroutineScope(Dispatchers.IO).launch {
            val userList = db.getAll()
            if (userList.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    user = userList[0]
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.Home.route)
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Email, contentDescription = "Tin Nhắn") },
                    label = { Text("Tin nhắn") },
                    selected = false,
                    onClick = {navController.navigate(Screen.Message.route) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Giỏ Hàng") },
                    label = { Text("Giỏ Hàng") },
                    selected = false,
                    onClick = { navController.navigate(Screen.Cart.route)}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Thông Tin") },
                    label = { Text("Thông Tin") },
                    selected = true,
                    onClick = {

                        navController.navigate(Screen.Profile.route)
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Cài Đặt") },
                    label = { Text("Cài Đặt") },
                    selected = false,
                    onClick = { navController.navigate(Screen.Setting.route)}
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "User Profile",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Come and bring me :)",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Information Section
            ProfileSection(
                title = "Thông tin liên hệ",
                content = "${user?.Email}",
                onClick = { }
            )

            ProfileSection(
                title = "Số điện thoại",
                content = "${user?.SDT}",
                onClick = { }
            )

            ProfileSection(
                title = "Họ và tên",
                content = "${user?.TenNgD}",
                onClick = { }
            )
            Spacer(modifier = Modifier.height(24.dp))

            // New section for Order History and Orders icons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Order History Icon
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {navController.navigate(Screen.OrderHistory.route)}
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Lịch sử đơn hàng",
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        text = "Lịch sử đơn hàng",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Orders Icon
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { navController.navigate(Screen.XemDonHang.route)}
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "Đơn hàng",
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        text = "Đơn hàng",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

    }
}

@Composable
fun ProfileSection(
    title: String,
    content: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "More",
                tint = Color.Gray
            )
        }
    }
}