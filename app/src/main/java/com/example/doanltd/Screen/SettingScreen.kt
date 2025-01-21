package com.example.doanltd.Screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doanltd.AppDatabase
import com.example.doanltd.Navigation.Screen
import com.example.doanltd.NgDungDao
import com.example.doanltd.RoomDatabase.NgDungRoom.NgDungEntity
import com.example.doanltd.View.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navController: NavController) {
    var isDarkMode by remember { mutableStateOf(false) } // Quản lý trạng thái giao diện
    val context = LocalContext.current
    var message by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }
    var showChangePasswordPopup by remember { mutableStateOf(false) }
    var user by remember { mutableStateOf<NgDungEntity?>(null) }
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

    // Cập nhật MaterialTheme với chế độ sáng/tối
    MaterialTheme(
        colorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Cài Đặt") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = false,
                        onClick = { navController.navigate(Screen.Home.route) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Email, contentDescription = "Tin Nhắn") },
                        label = { Text("Tin nhắn") },
                        selected = false,
                        onClick = { navController.navigate(Screen.Message.route) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Giỏ Hàng") },
                        label = { Text("Giỏ Hàng") },
                        selected = false,
                        onClick = { navController.navigate(Screen.Cart.route) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "Thông Tin") },
                        label = { Text("Thông Tin") },
                        selected = false,
                        onClick = { navController.navigate(Screen.Profile.route) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Cài Đặt") },
                        label = { Text("Cài Đặt") },
                        selected = true,
                        onClick = { navController.navigate(Screen.Setting.route) }
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Dark Mode Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Dark Mode")
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = {
                            isDarkMode = it // Bật chế độ tối
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { showChangePasswordPopup = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = "Change Password")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Đổi Mật Khẩu")
                    }
                }
                if (showChangePasswordPopup) {
                    user?.let {
                        ChangePasswordDialog(
                            it,
                            onDismiss = { showChangePasswordPopup = false },
                            onMessageChange = { msg, success ->
                                message = msg
                                isSuccess = success
                            },
                            authViewModel = AuthViewModel(),
                            navController = navController,
                            db = db
                        )
                    }
                }
                // Display Message
                if (message.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                            contentDescription = null,
                            tint = if (isSuccess) Color.Green else Color.Red
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = message,
                            color = if (isSuccess) Color.Green else Color.Red
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))

                // Logout Button
                Button(
                    onClick = {
                        navController.navigate(Screen.Login.route) {
                            CoroutineScope(Dispatchers.IO).launch { user?.let { db.delete(it) } }
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4B12))
                ) {
                    Text("Đăng Xuất")
                }
            }
        }
    }
}
@Composable
fun ChangePasswordDialog(
    user: NgDungEntity,
    navController: NavController,
    db:NgDungDao,
    onDismiss: () -> Unit,
    onMessageChange: (String, Boolean) -> Unit,
    authViewModel: AuthViewModel
) {
    var MatKhauNgD by remember { mutableStateOf("") }
    var MatKhauMoi by remember { mutableStateOf("") }
    var NhapLaiMatKhau by remember { mutableStateOf("") }

    val capNhatMatKhauThanhCong by authViewModel.capNhatMatKhauThanhCong.collectAsState()
    val thongBaoCapNhatMatKhau by authViewModel.thongbaocapnhatmatkhau.collectAsState()

    // Lắng nghe trạng thái từ ViewModel
    LaunchedEffect(capNhatMatKhauThanhCong, thongBaoCapNhatMatKhau) {
        val isSuccessful = capNhatMatKhauThanhCong
        if (isSuccessful != null) {
            onMessageChange(thongBaoCapNhatMatKhau ?: "", isSuccessful)
            if (isSuccessful) {
                onDismiss() // Đóng hộp thoại trước khi chuyển trang
                CoroutineScope(Dispatchers.IO).launch {
                    user.let { db.delete(it) }
                }
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
            authViewModel.resetPasswordChangeState() // Reset trạng thái
        }
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Đổi Mật Khẩu") },
        text = {
            Column {
                OutlinedTextField(
                    value = MatKhauNgD,
                    onValueChange = { MatKhauNgD = it },
                    label = { Text("Mật khẩu cũ") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = MatKhauMoi,
                    onValueChange = { MatKhauMoi = it },
                    label = { Text("Mật khẩu mới") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = NhapLaiMatKhau,
                    onValueChange = { NhapLaiMatKhau = it },
                    label = { Text("Nhập lại mật khẩu mới") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            val context = LocalContext.current
            TextButton(onClick = {
                when {
                    MatKhauNgD.isEmpty() || MatKhauMoi.isEmpty() || NhapLaiMatKhau.isEmpty() -> {
                        Toast.makeText(context, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show()
                    }
                    MatKhauMoi != NhapLaiMatKhau -> {
                        Toast.makeText(context, "Mật khẩu mới không khớp.", Toast.LENGTH_SHORT).show()
                    }
                    MatKhauNgD == MatKhauMoi -> {
                        Toast.makeText(context, "Mật khẩu cũ và mới không được giống nhau.", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            authViewModel.capNhatMatKhau(
                                MaNgD = user.MaNgD,
                                MatKhauCu = MatKhauNgD,
                                MatKhauMoi = MatKhauMoi
                            )
                        }
                    }
                }
            }) {
                Text("Xác Nhận")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Hủy")
            }
        }
    )
}
