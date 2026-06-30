package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
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
fun CatalogScreen(
    products: List<Product>,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    locationFilter: String,
    onLocationFilterChange: (String) -> Unit,
    onProductSelect: (Product) -> Unit
) {
    var showLocationDropdown by remember { mutableStateOf(false) }

    val categories = listOf(
        "Barchasi" to "📌",
        "Ekinlar 🌾" to "🌾",
        "Chorva 🐄" to "🐄",
        "Texnika 🚜" to "🚜",
        "Agrokimyo 🧪" to "🧪"
    )

    val locations = listOf(
        "Barchasi", "Andijon", "Buxoro", "Farg'ona", "Jizzax", 
        "Xorazm", "Namangan", "Navoiy", "Qashqadaryo", 
        "Qoraqalpog'iston Res.", "Samarqand", "Sirdaryo", 
        "Surxondaryo", "Toshkent shahri", "Toshkent viloyati"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App bar top header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 4.dp,
            color = AgroGreen
        ) {
            Row(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Agro Katalog",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Location selector trigger
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .clickable { showLocationDropdown = true }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("catalog_location_dropdown"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (locationFilter == "Barchasi") "Hudud: Barchasi" else locationFilter,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color.White
                    )

                    DropdownMenu(
                        expanded = showLocationDropdown,
                        onDismissRequest = { showLocationDropdown = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        locations.forEach { loc ->
                            DropdownMenuItem(
                                text = { Text(loc, color = AgroGreenDark) },
                                onClick = {
                                    onLocationFilterChange(loc)
                                    showLocationDropdown = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Horizontal Category Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { (cat, emoji) ->
                val isSelected = cat == selectedCategory
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelect(cat) },
                    label = { 
                        Text(
                            text = if (cat.contains(" ")) cat.substringBefore(" ") else cat,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    leadingIcon = { Text(emoji) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AgroGreen,
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White,
                        containerColor = Color.White,
                        labelColor = AgroGreenDark
                    ),
                    modifier = Modifier.testTag("catalog_chip_$cat")
                )
            }
        }

        // Grid Content
        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Ushbu hudud yoki kategoriyada e'lon topilmadi.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 14.dp, end = 14.dp, bottom = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(products) { product ->
                    CatalogGridItem(product = product, onClick = { onProductSelect(product) })
                }
            }
        }
    }
}

@Composable
fun CatalogGridItem(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(16.dp))
            .testTag("catalog_item_${product.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // Visual Header Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(105.dp)
                    .background(
                        Brush.linearGradient(
                            colors = when (product.category) {
                                "Ekinlar 🌾" -> listOf(Color(0xFF81C784), Color(0xFF388E3C))
                                "Chorva 🐄" -> listOf(Color(0xFFFFB74D), Color(0xFFF57C00))
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
                        fontSize = 28.sp
                    )
                    Text(
                        text = product.quantity,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }

                // Premium Badge Overlay
                if (product.isPremium) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = "Premium",
                        tint = AgroGold,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = product.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
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
                        text = product.location.substringBefore(","),
                        color = Color.Gray,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                val formattedPrice = String.format("%,.0f", product.price).replace(",", " ")
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$formattedPrice so'm",
                        color = AgroTerracotta,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp
                    )

                    Surface(
                        color = when (product.priceType) {
                            "Optom" -> AgroGold.copy(alpha = 0.15f)
                            else -> AgroGreen.copy(alpha = 0.15f)
                        },
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = product.priceType,
                            color = when (product.priceType) {
                                "Optom" -> AgroGold
                                else -> AgroGreen
                            },
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
            }
        }
    }
}
