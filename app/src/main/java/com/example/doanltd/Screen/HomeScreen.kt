package com.example.doanltd.Screen

import LoaiSP
import com.example.doanltd.data.SanPham
import com.example.doanltd.View.SanPhamViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.doanltd.Navigation.Screen
import com.example.doanltd.Navigation.Screen.CategoryScreen
import com.example.doanltd.R
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: SanPhamViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var resetHome by remember { mutableStateOf(false) }

    val Orange = Color(0xFFE7A953)

    val sanPhams by remember { derivedStateOf { viewModel.posts } }
    val loaiSps by remember { derivedStateOf { viewModel.loaisanphams } }

    // Reset Home when clicked on Home Button
    if (resetHome) {
        searchQuery = ""
        isSearching = false
        resetHome = false
    }

    fun normalize(text: String): String {
        val regex = "\\p{InCombiningDiacriticalMarks}+".toRegex()
        val temp = java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFD)
        return regex.replace(temp, "").lowercase()
    }

    val filteredSanPhams = sanPhams.filter {
        normalize(it.TenSp).contains(normalize(searchQuery)) ||
                normalize(it.MoTa).contains(normalize(searchQuery))
    }

        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Orange
                    ),
                    title = {
                        Column {
                            Text("GIGACHADCAFE")
                            Text(
                                "Chao buoi sang, Phuong Vo",
                                fontSize = 14.sp
                            )
                        }
                    }
                )
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
                            navController.navigate(Screen.Cart.route )
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(painter = painterResource(id=R.drawable.history),
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
                        selected = true,
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
        ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {



            // Search Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { focusState -> isSearching = focusState.isFocused },
                    placeholder = { Text("Tìm Kiếm") },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                if (isSearching) {
                    IconButton(
                        onClick = { isSearching = false },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            }

            // Category Section
            Text(
                "Danh Mục",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)

            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                items(loaiSps) { loaiSp ->
                    CategoryItem(loaiSP = loaiSp, navController = navController)
                }
            }

            // Product Section
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(if (searchQuery.isNotEmpty()) filteredSanPhams else sanPhams) { sanPham ->
                    SanPhamItem(sanPham = sanPham, navController = navController)
                }
            }
        }
    }
}

@Composable
fun CategoryItem(loaiSP: LoaiSP, navController: NavController) {
    val maLoai = loaiSP.MaLoai ?: return

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .size(60.dp)
            .clickable {
                navController.navigate(CategoryScreen.createRoute(maLoai))
            }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)

        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = loaiSP.HinhLoai,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
        Text(
            text = loaiSP.TenLoai,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SanPhamItem(sanPham: SanPham, navController: NavController) {
    val formattedPrice = NumberFormat.getInstance(Locale("vi", "VN")).format(sanPham.DonGia)

//    Card(
//        modifier = Modifier.fillMaxWidth().padding(15.dp),
//        shape = RoundedCornerShape(12.dp),
//        onClick = { navController.navigate("${Screen.ProductDetail.route}/${sanPham.MaSp}") }
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(8.dp)
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ){
//            AsyncImage(
//                modifier = Modifier.size(40.dp),
//                model = sanPham.HinhSp,
//                contentDescription = null
//            )
//        }
//        Column(modifier = Modifier.padding(8.dp)) {
//            Text(
//                text = sanPham.TenSp,
//                style = MaterialTheme.typography.titleSmall,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(vertical = 4.dp)
//            )
//            Text(
//                text = "${sanPham.MoTa} Thơm ngon khó cưỡng",
//                style = MaterialTheme.typography.bodySmall,
//                color = Color.Gray,
//                modifier = Modifier.padding(vertical = 4.dp)
//            )
//            Text(
//                text = "$formattedPrice VND",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold
//            )
//        }
//    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = { navController.navigate("${Screen.ProductDetail.route}/${sanPham.MaSp}") }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier.size(150.dp).padding(10.dp),
                model = sanPham.HinhSp,
                contentDescription = null
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    sanPham.TenSp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "$formattedPrice VND",
                    color = Color.Gray
                )
            }
        }
    }
}

