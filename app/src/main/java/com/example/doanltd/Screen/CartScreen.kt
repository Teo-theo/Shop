package com.example.doanltd.Screen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.doanltd.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.doanltd.CartDao

import com.example.doanltd.CartManager
import com.example.doanltd.Navigation.Screen
import com.example.doanltd.R
import com.example.doanltd.RoomDatabase.CartRoom.CartItemEntity
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavHostController) {
    val context = LocalContext.current
    val Orange = Color(0xFFE7A953)
    val db = remember { AppDatabase.getDatabase(context) }
    val cartDao = remember { db.cartDao() }
    val cartItems = remember { mutableStateOf<List<CartItemEntity>>(emptyList()) }
    val totalAmount = remember { mutableStateOf(0.0) }

    // Lấy danh sách sản phẩm từ cơ sở dữ liệu
    LaunchedEffect(Unit) {
        val items = cartDao.getAllCartItems()
        cartItems.value = items
        totalAmount.value = items.sumOf { it.price * it.quantity }
    }

    fun updateCartItem(cartItem: CartItemEntity, newQuantity: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            if (newQuantity > 0) {
                if(newQuantity > cartItem.SoLuongSP)
                {
                    cartDao.updateCartItem(cartItem.copy(quantity = cartItem.SoLuongSP))
                }
                cartDao.updateCartItem(cartItem.copy(quantity = newQuantity))
            } else {
                cartDao.deleteCartItem(cartItem)
            }
            val items = cartDao.getAllCartItems()
            cartItems.value = items
            totalAmount.value = items.sumOf { it.price * it.quantity }
        }
    }

    fun deleteCartItem(cartItem: CartItemEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            cartDao.deleteCartItem(cartItem)
            val items = cartDao.getAllCartItems()
            cartItems.value = items
            totalAmount.value = items.sumOf { it.price * it.quantity }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Orange
                ),
                title = { Text(
                    text = "Giỏ hàng",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(16.dp)
                ) },
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
                    navController.navigate(Screen.OrderDetails.route)
                },
                containerColor = Orange ,
                contentColor = Color.White,
                shape = CircleShape
            ){
                Icon(Icons.Default.Paid,contentDescription = null)
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Orange
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
                    label = { Text("Cart") },
                    selected = true,
                    onClick = {
                        navController.navigate(Screen.Cart.route )
                    }
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id= R.drawable.history),
                        modifier = Modifier.size(30.dp), contentDescription = "History") },
                    label = { Text("History") },
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.OrderHistory.route)
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.Home.route)
                    }
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {


            if (cartItems.value.isEmpty()) {
                Text(
                    text = "Giỏ hàng của bạn trống",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartItems.value) { cartItem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = cartItem.imageUrl,
                                contentDescription = "",
                                modifier = Modifier.size(80.dp),
                                contentScale = ContentScale.Crop
                            )
                            Column(
                                modifier = Modifier.weight(1f).padding(10.dp)
                            ) {
                                Text(
                                    cartItem.name,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "${cartItem.price} VND",
                                    color = Color.Gray
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {
                                deleteCartItem(cartItem)
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "", tint = Color.Black)
                            }
                                Button(
                                    onClick = { if (cartItem.quantity > 0) updateCartItem(cartItem, cartItem.quantity - 1) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Orange)
                                ) {
                                    Text("-")
                                }
                                Text(
                                    text = cartItem.quantity.toString(),
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                Button(
                                    onClick = { updateCartItem(cartItem, cartItem.quantity + 1) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Orange)
                                ) {
                                    Text("+")
                                }
                            }
//
                        }
                    }
                }

                    Text(
                        text = "Tổng tiền: ${totalAmount.value} VND",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )



            }
        }
    }
}

