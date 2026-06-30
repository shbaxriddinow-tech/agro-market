package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    products: List<Product>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onProductSelect: (Product) -> Unit,
    userName: String
) {
    val premiumProducts = products.filter { it.isPremium }
    val regularProducts = products.filter { !it.isPremium }

    val categories = listOf(
        "Barchasi" to "📌",
        "Ekinlar 🌾" to "🌾",
        "Chorva 🐄" to "🐄",
        "Texnika 🚜" to "🚜",
        "Agrokimyo 🧪" to "🧪"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Hero Header Block
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(AgroGreen, AgroGreen.copy(alpha = 0.85f))
                        ),
                        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                    )
                    .padding(horizontal = 20.dp)
                    .padding(top = 28.dp, bottom = 24.dp)
            ) {
                Column {
                    // Header Greeting
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Xush kelibsiz! 👋",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )
                            Text(
                                text = userName.ifEmpty { "Agro Hamkor" },
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        // Verification/Trust badge
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(AgroGold.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = AgroGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Ishonchli",
                                color = AgroGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Title
                    Text(
                        text = "Yer barakasi — rizq-ro'zimiz!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        lineHeight = 30.sp
                    )
                    Text(
                        text = "Bugun qanday agro mahsulot qidiryapsiz?",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Search TextField in Header
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(14.dp))
                            .background(Color.White, RoundedCornerShape(14.dp))
                            .testTag("home_search_input"),
                        placeholder = { Text("Qidiruv: guruch, traktor, sigir...", color = Color.Gray, fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AgroGreen) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchQueryChange("") }) {
                                    Icon(Icons.Default.Close, contentDescription = "Tozalash", tint = Color.Gray)
                                }
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            }
        }

        // Category Selection
        item {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Text(
                    text = "Kategoriyalar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgroGreenDark,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    categories.forEach { (cat, emoji) ->
                        Card(
                            onClick = { onCategorySelect(cat) },
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .testTag("cat_tab_$cat"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (cat == "Barchasi") AgroGreenLight else Color.White
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = emoji, fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (cat.contains(" ")) cat.substringBefore(" ") else cat,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (cat == "Barchasi") AgroGreen else AgroGreenDark
                                )
                            }
                        }
                    }
                }
            }
        }

        // Premium Ads Section
        item {
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = AgroGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Premium e'lonlar",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgroGreenDark
                        )
                    }
                    Text(
                        text = "Hammasi",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgroTerracotta,
                        modifier = Modifier.clickable { onCategorySelect("Barchasi") }
                    )
                }

                if (premiumProducts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Premium e'lonlar mavjud emas.",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(premiumProducts) { product ->
                            PremiumProductCard(product = product, onClick = { onProductSelect(product) })
                        }
                    }
                }
            }
        }

        // Regular/Recent Listings Section
        item {
            Text(
                text = "Yangi e'lonlar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AgroGreenDark,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
        }

        val displayRegular = if (searchQuery.isNotEmpty()) {
            products // show all matching search query
        } else {
            regularProducts
        }

        if (displayRegular.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Hech qanday e'lon topilmadi.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            items(displayRegular) { product ->
                RegularProductRow(product = product, onClick = { onProductSelect(product) })
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp)) // padding for bottom bar
        }
    }
}

@Composable
fun PremiumProductCard(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(220.dp)
            .shadow(2.dp, RoundedCornerShape(18.dp))
            .testTag("premium_card_${product.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // Visual Placeholder instead of web image for 100% reliability
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .background(
                        Brush.linearGradient(
                            colors = when (product.category) {
                                "Ekinlar 🌾" -> listOf(Color(0xFF81C784), Color(0xFF388E3C))
                                "Chorva 🐄" -> listOf(Color(0xFFFFB74D), Color(0xFFF57C00))
                                "Texnika 🚜" -> listOf(Color(0xFF64B5F6), Color(0xFF1976D2))
                                else -> listOf(Color(0xFFBA68C8), Color(0xFF7B1FA2)) // Agrokimyo
                            }
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Category Emoji & Icon Overlay
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = when (product.category) {
                            "Ekinlar 🌾" -> "🌾"
                            "Chorva 🐄" -> "🐄"
                            "Texnika 🚜" -> "🚜"
                            else -> "🧪"
                        },
                        fontSize = 32.sp
                    )
                    Text(
                        text = product.quantity,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Premium gold badge
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(AgroGold, RoundedCornerShape(8.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "PREMIUM",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = AgroGreenDark
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = product.location,
                        color = Color.Gray,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Price display with beautiful styling
                val formattedPrice = String.format("%,.0f", product.price).replace(",", " ")
                Text(
                    text = "$formattedPrice so'm",
                    color = AgroTerracotta,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )

                // Seller rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = AgroGold, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${product.sellerRating}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = AgroGreenDark
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "• ${product.sellerName.substringBefore(" ")}",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun RegularProductRow(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .shadow(0.5.dp, RoundedCornerShape(14.dp))
            .testTag("regular_card_${product.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Visual Box
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(
                            colors = when (product.category) {
                                "Ekinlar 🌾" -> listOf(Color(0xFFC8E6C9), Color(0xFF81C784))
                                "Chorva 🐄" -> listOf(Color(0xFFFFE0B2), Color(0xFFFFB74D))
                                "Texnika 🚜" -> listOf(Color(0xFFBBDEFB), Color(0xFF64B5F6))
                                else -> listOf(Color(0xFFE1BEE7), Color(0xFFBA68C8))
                            }
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (product.category) {
                        "Ekinlar 🌾" -> "🌾"
                        "Chorva 🐄" -> "🐄"
                        "Texnika 🚜" -> "🚜"
                        else -> "🧪"
                    },
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Right Product Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = AgroGreenDark
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(11.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = product.location,
                        color = Color.Gray,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Price & Type (Optom / Chakana)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val formattedPrice = String.format("%,.0f", product.price).replace(",", " ")
                    Text(
                        text = "$formattedPrice so'm",
                        color = AgroTerracotta,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp
                    )

                    Surface(
                        color = when (product.priceType) {
                            "Optom" -> AgroGold.copy(alpha = 0.15f)
                            "Chakana" -> AgroGreen.copy(alpha = 0.15f)
                            else -> Color.LightGray.copy(alpha = 0.3f)
                        },
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text(
                            text = product.priceType,
                            color = when (product.priceType) {
                                "Optom" -> AgroGold
                                "Chakana" -> AgroGreen
                                else -> Color.DarkGray
                            },
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
