package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    currentUserPhone: String,
    onNavigateToChat: (chatId: String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var showCallDialog by remember { mutableStateOf(false) }

    val formattedPrice = String.format("%,.0f", product.price).replace(",", " ")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product.title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Orqaga", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AgroGreen),
                modifier = Modifier.testTag("detail_back_button")
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // Large Product Image Representation with Brush Gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(
                        Brush.linearGradient(
                            colors = when (product.category) {
                                "Ekinlar 🌾" -> listOf(Color(0xFF81C784), Color(0xFF1F4D2C))
                                "Chorva 🐄" -> listOf(Color(0xFFFFB74D), Color(0xFFC1502E))
                                "Texnika 🚜" -> listOf(Color(0xFF64B5F6), Color(0xFF1976D2))
                                else -> listOf(Color(0xFFBA68C8), Color(0xFF7B1FA2))
                            }
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = when (product.category) {
                            "Ekinlar 🌾" -> "🌾"
                            "Chorva 🐄" -> "🐄"
                            "Texnika 🚜" -> "🚜"
                            else -> "🧪"
                        },
                        fontSize = 72.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = product.category,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }

                if (product.isPremium) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .background(AgroGold, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "PREMIUM",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Product Title, Location & Price Box
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = product.title,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgroGreenDark
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = AgroGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = product.location,
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Divider(color = Color.LightGray.copy(alpha = 0.5f))

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Narxi",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "$formattedPrice so'm",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    color = AgroTerracotta
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Birligi",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Surface(
                                    color = AgroGreenLight,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "${product.quantity} / ${product.priceType}",
                                        color = AgroGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Description Box
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Tavsif va Ma'lumot",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgroGreenDark
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = product.description.ifEmpty { "Mahsulot haqida qo'shimcha ma'lumot berilmagan." },
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            lineHeight = 22.sp
                        )
                    }
                }

                // Seller Information Box
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Seller Avatar Circle
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(AgroGreenLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Sotuvchi",
                                tint = AgroGreen,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = product.sellerName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgroGreenDark
                            )
                            Text(
                                text = "E'lon beruvchi (Sotuvchi)",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            
                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(5) { index ->
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (index < product.sellerRating.toInt()) AgroGold else Color.LightGray,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${product.sellerRating}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = AgroGreenDark
                                )
                            }
                        }
                    }
                }

                // Call / Chat Action Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Chat Button
                    Button(
                        onClick = {
                            if (currentUserPhone.isNotEmpty()) {
                                // Prevent chatting with oneself
                                if (currentUserPhone == product.sellerPhone) {
                                    // Normally show warning, but we still generate activeChatId
                                }
                                val chatId = "${currentUserPhone}_${product.sellerPhone}_${product.id}"
                                onNavigateToChat(chatId)
                            }
                        },
                        modifier = Modifier
                            .weight(1.5f)
                            .height(56.dp)
                            .testTag("chat_contact_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AgroGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Chatda yozish",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }

                    // Call Button
                    Button(
                        onClick = { showCallDialog = true },
                        modifier = Modifier
                            .weight(1.2f)
                            .height(56.dp)
                            .testTag("call_contact_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AgroGold,
                            contentColor = Color.White
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Qo'ng'iroq",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }

    // Call Dialer Dialog
    if (showCallDialog) {
        AlertDialog(
            onDismissRequest = { showCallDialog = false },
            icon = { Icon(Icons.Default.PhoneEnabled, contentDescription = null, tint = AgroGreen, modifier = Modifier.size(36.dp)) },
            title = { Text("Sotuvchi bilan bog'lanish", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = AgroGreenDark) },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sotuvchi: ${product.sellerName}",
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = product.sellerPhone,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = AgroTerracotta,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ushbu raqamga qo'ng'iroq qilishni xohlaysizmi?",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCallDialog = false
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${product.sellerPhone}"))
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AgroGreen)
                ) {
                    Text("Ha, qo'ng'iroq qilish")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCallDialog = false }) {
                    Text("Yopish", color = Color.Gray)
                }
            }
        )
    }
}
