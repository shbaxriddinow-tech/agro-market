package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAdScreen(
    onAdSubmitted: (
        title: String,
        category: String,
        description: String,
        price: Double,
        priceType: String,
        quantity: String,
        location: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Ekinlar 🌾") }
    var priceStr by remember { mutableStateOf("") }
    var priceType by remember { mutableStateOf("Chakana") } // Chakana, Optom, Kelishilgan
    var quantity by remember { mutableStateOf("") }
    var selectedRegion by remember { mutableStateOf("Toshkent viloyati") }
    var description by remember { mutableStateOf("") }
    
    var showCategoryDropdown by remember { mutableStateOf(false) }
    var showRegionDropdown by remember { mutableStateOf(false) }
    var showPriceTypeDropdown by remember { mutableStateOf(false) }
    
    var showSuccessOverlay by remember { mutableStateOf(false) }

    val categories = listOf("Ekinlar 🌾", "Chorva 🐄", "Texnika 🚜", "Agrokimyo 🧪")
    val priceTypes = listOf("Chakana", "Optom", "Kelishilgan")
    
    val regions = listOf(
        "Andijon", "Buxoro", "Farg'ona", "Jizzax", 
        "Xorazm", "Namangan", "Navoiy", "Qashqadaryo", 
        "Qoraqalpog'iston Res.", "Samarqand", "Sirdaryo", 
        "Surxondaryo", "Toshkent shahri", "Toshkent viloyati"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            // App Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 4.dp,
                color = AgroGreen
            ) {
                Box(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Yangi e'lon berish",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Scrollable Form
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Image Uploader Block (Simulated)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .testTag("add_ad_image_block")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = null,
                            tint = AgroGreen,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Mahsulot rasmini yuklash",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = AgroGreenDark
                        )
                        Text(
                            text = "Rasm e'loningiz xaridorlarga tezroq sotilishiga yordam beradi",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Input fields
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Title
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("E'lon sarlavhasi (nomi)") },
                            placeholder = { Text("Masalan: Toza saralangan guruch lazer") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ad_title_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Category Selector Dropdown
                        ExposedDropdownMenuBox(
                            expanded = showCategoryDropdown,
                            onExpandedChange = { showCategoryDropdown = it }
                        ) {
                            OutlinedTextField(
                                value = selectedCategory,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Kategoriya tanlang") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryDropdown) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .testTag("ad_category_selector"),
                                shape = RoundedCornerShape(12.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = showCategoryDropdown,
                                onDismissRequest = { showCategoryDropdown = false }
                            ) {
                                categories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat) },
                                        onClick = {
                                            selectedCategory = cat
                                            showCategoryDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        // Price and Price Type Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = priceStr,
                                onValueChange = { priceStr = it },
                                label = { Text("Narxi (so'm)") },
                                placeholder = { Text("M: 15000") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier
                                    .weight(1.2f)
                                    .testTag("ad_price_input"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            ExposedDropdownMenuBox(
                                expanded = showPriceTypeDropdown,
                                onExpandedChange = { showPriceTypeDropdown = it },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = priceType,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Sotish turi") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showPriceTypeDropdown) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                        .testTag("ad_price_type_selector"),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = showPriceTypeDropdown,
                                    onDismissRequest = { showPriceTypeDropdown = false }
                                ) {
                                    priceTypes.forEach { pt ->
                                        DropdownMenuItem(
                                            text = { Text(pt) },
                                            onClick = {
                                                priceType = pt
                                                showPriceTypeDropdown = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // Quantity / Measure Unit
                        OutlinedTextField(
                            value = quantity,
                            onValueChange = { quantity = it },
                            label = { Text("Miqdori yoki o'lchov birligi") },
                            placeholder = { Text("Masalan: 500 kg, 2 tonna, 5 bosh") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ad_quantity_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Location Selector Dropdown
                        ExposedDropdownMenuBox(
                            expanded = showRegionDropdown,
                            onExpandedChange = { showRegionDropdown = it }
                        ) {
                            OutlinedTextField(
                                value = selectedRegion,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Joylashuv (Viloyat)") },
                                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = AgroGreen) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showRegionDropdown) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .testTag("ad_region_selector"),
                                shape = RoundedCornerShape(12.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = showRegionDropdown,
                                onDismissRequest = { showRegionDropdown = false }
                            ) {
                                regions.forEach { reg ->
                                    DropdownMenuItem(
                                        text = { Text(reg) },
                                        onClick = {
                                            selectedRegion = reg
                                            showRegionDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        // Description
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Mahsulot tavsifi") },
                            placeholder = { Text("Mahsulot sifati, yetkazib berish shartlari va batafsil ma'lumotlarni yozing...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .testTag("ad_desc_input"),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                val isFormValid = title.trim().isNotEmpty() && 
                                  priceStr.trim().isNotEmpty() && 
                                  quantity.trim().isNotEmpty() &&
                                  description.trim().isNotEmpty()

                // Submit Button
                Button(
                    onClick = {
                        if (isFormValid) {
                            val priceVal = priceStr.toDoubleOrNull() ?: 0.0
                            onAdSubmitted(
                                title,
                                selectedCategory,
                                description,
                                priceVal,
                                priceType,
                                quantity,
                                selectedRegion
                            )
                            showSuccessOverlay = true
                            
                            // Reset state fields
                            title = ""
                            priceStr = ""
                            quantity = ""
                            description = ""
                        }
                    },
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("ad_submit_button"),
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
                        Icon(Icons.Default.AddCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "E'lonni joylash",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

        // Success Pop-up Overlay
        if (showSuccessOverlay) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable { showSuccessOverlay = false },
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = AgroGreen,
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "E'lon qabul qilindi!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = AgroGreenDark
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Sizning e'loningiz muvaffaqiyatli saqlandi va katalogga qo'shildi.",
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { showSuccessOverlay = false },
                            colors = ButtonDefaults.buttonColors(containerColor = AgroGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Rahmat")
                        }
                    }
                }
            }
        }
    }
}
