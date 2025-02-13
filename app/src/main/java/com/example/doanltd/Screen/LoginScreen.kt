package com.example.doanltd.Screen

import NgDung
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doanltd.AppDatabase
import com.example.doanltd.Navigation.Screen
import com.example.doanltd.R
import com.example.doanltd.View.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController,viewModel: AuthViewModel= androidx.lifecycle.viewmodel.compose.viewModel()) {
    var TKNgD by remember { mutableStateOf("") }
    var MatKhauNgD by remember { mutableStateOf("") }

    val Orange = Color(0xFFE7A953)

    val dangNhapThanhCong by viewModel.dangNhapThanhCong.collectAsState()
    val dulieunguoidung by viewModel.duLieuNguoiDung.collectAsState()

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context).ngDungDao()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.cake),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 30.dp),
            text = "ĐĂNG NHẬP",
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                modifier = Modifier.padding(horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors(Orange),
                onClick = {
                    navController.navigate("login")
                },
            ) {
                Text(text = "Quản lý", color = Color.Black)
            }
            Button(
                colors = ButtonDefaults.buttonColors(Color.LightGray),
                onClick = {
                    navController.navigate(Screen.LoginCustomer.route)
                }
            ) {
                Text(text = "Khách hàng", color = Color.Black)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            OutlinedTextField(
                value = TKNgD,
                onValueChange = { TKNgD = it },
                label = { Text("Tên đăng nhập") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(20.dp),
            )

            OutlinedTextField(
                value = MatKhauNgD,
                onValueChange = { MatKhauNgD = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(20.dp),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    onClick = {
                        navController.navigate(Screen.Admin.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE7A953)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("ĐĂNG NHẬP", color = Color.White)
                }
                Button(
                    onClick = {
                        navController.navigate("register")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE7A953)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(text = "Đăng ký", color = Color.Black)
                }
            }
        }
    }
    }
    // ⬇️ Đặt LaunchedEffect bên ngoài Column ⬇️
    LaunchedEffect(dangNhapThanhCong) {
        dangNhapThanhCong?.let {
            if (it) {
                dulieunguoidung?.let { user ->
                    db.insertUserByFields(
                        user.MaNgD,
                        user.TenNgD,
                        user.Email,
                        user.SDT,
                        user.TKNgD,
                        user.TrangThai,
                        user.ChucVu
                    )
                    if(user.ChucVu.equals("NguoiDung") && user.TrangThai == 1)
                    {
                        navController.navigate("home")
                    }
                    else
                    {
                        navController.navigate("admin")
                    }
                }

                Toast.makeText( context,"Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context,"Đăng nhập thất bại!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

