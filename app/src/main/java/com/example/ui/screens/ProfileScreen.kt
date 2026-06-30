package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserSession
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userSession: UserSession,
    activeAdsCount: Int,
    onUpdateProfile: (name: String, role: String, region: String, paymentMethods: String) -> Unit,
    onLanguageChange: (String) -> Unit,
    onLogout: () -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showPaymentToast by remember { mutableStateOf<String?>(null) }
    
    var tempName by remember { mutableStateOf(userSession.fullName) }
    var tempRole by remember { mutableStateOf(userSession.userType) }
    var tempRegion by remember { mutableStateOf(userSession.location) }
    var tempPayment by remember { mutableStateOf(userSession.paymentMethods) }

    val roles = listOf(
        "Dehqon (sotuvchi)",
        "Ishlab chiqaruvchi (texnika/agrokimyo)",
        "Texnik xizmat ko'rsatuvchi",
        "Xaridor"
    )

    val regions = listOf(
        "Andijon", "Buxoro", "Farg'ona", "Jizzax", 
        "Xorazm", "Namangan", "Navoiy", "Qashqadaryo", 
        "Qoraqalpog'iston Res.", "Samarqand", "Sirdaryo", 
        "Surxondaryo", "Toshkent shahri", "Toshkent viloyati"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Top App Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 4.dp,
            color = AgroGreen
        ) {
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Mening profilim",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Profile Avatar Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .shadow(2.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Large Avatar Circle
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(AgroGreenLight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (userSession.fullName.isNotEmpty()) userSession.fullName.substring(0, 1) else "A",
                        color = AgroGreen,
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = userSession.fullName.ifEmpty { "Agro Foydalanuvchi" },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgroGreenDark
                )

                Text(
                    text = userSession.phoneNumber,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Custom badge for Role
                Surface(
                    color = AgroGreen.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = userSession.userType,
                        color = AgroGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$activeAdsCount",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = AgroTerracotta
                        )
                        Text(
                            text = "E'lonlarim",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(Color.LightGray)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${userSession.rating}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = AgroGreenDark
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Icon(Icons.Default.Star, contentDescription = null, tint = AgroGold, modifier = Modifier.size(18.dp))
                        }
                        Text(
                            text = "Reyting",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        // Section: Settings & Info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Payment Methods Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "To'lov tizimlari (Premium uchun)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgroGreenDark
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Payme Card Integration
                        Card(
                            onClick = { showPaymentToast = "Payme to'lov tizimi muvaffaqiyatli ulandi." },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (userSession.paymentMethods.contains("Payme")) Color(0xFFE0F7FA) else Color(0xFFF5F5F5)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("payment_payme")
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Payment, contentDescription = null, tint = Color(0xFF00BCD4))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Payme", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.DarkGray)
                                Text(
                                    text = if (userSession.paymentMethods.contains("Payme")) "Ulandi" else "Ulanmagan",
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        // Click Card Integration
                        Card(
                            onClick = { showPaymentToast = "Click to'lov tizimi muvaffaqiyatli ulandi." },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (userSession.paymentMethods.contains("Click")) Color(0xFFE8EAF6) else Color(0xFFF5F5F5)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("payment_click")
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.AccountBalance, contentDescription = null, tint = Color(0xFF3F51B5))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Click", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.DarkGray)
                                Text(
                                    text = if (userSession.paymentMethods.contains("Click")) "Ulandi" else "Ulanmagan",
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    // Notification alert showing linked status
                    AnimatedVisibility(visible = showPaymentToast != null) {
                        Surface(
                            color = AgroGreenLight,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            Text(
                                text = showPaymentToast ?: "",
                                color = AgroGreen,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }

            // Language Settings Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ilova tili / Язык",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgroGreenDark
                    )

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFECEFF1))
                            .padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "O'zbekcha",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (userSession.language == "O'zbekcha") Color.White else Color.DarkGray,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (userSession.language == "O'zbekcha") AgroGreen else Color.Transparent)
                                .clickable { onLanguageChange("O'zbekcha") }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("profile_lang_uz")
                        )
                        Text(
                            text = "Русский",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (userSession.language == "Rus tili") Color.White else Color.DarkGray,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (userSession.language == "Rus tili") AgroGreen else Color.Transparent)
                                .clickable { onLanguageChange("Rus tili") }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("profile_lang_ru")
                        )
                    }
                }
            }

            // Profile action triggers
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    ListItem(
                        headlineContent = { Text("Profil ma'lumotlarini tahrirlash", fontSize = 14.sp, fontWeight = FontWeight.Bold) },
                        leadingContent = { Icon(Icons.Default.Edit, contentDescription = null, tint = AgroGreen) },
                        modifier = Modifier
                            .clickable { showEditDialog = true }
                            .testTag("profile_edit_row")
                    )
                    Divider(color = Color.LightGray.copy(alpha = 0.5f))
                    ListItem(
                        headlineContent = { Text("Ilovadan chiqish", fontSize = 14.sp, color = Color.Red, fontWeight = FontWeight.Bold) },
                        leadingContent = { Icon(Icons.Default.Logout, contentDescription = null, tint = Color.Red) },
                        modifier = Modifier
                            .clickable { onLogout() }
                            .testTag("profile_logout_row")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(120.dp)) // padding for bottom bar
    }

    // Edit profile dialog
    if (showEditDialog) {
        var showRoleDrop by remember { mutableStateOf(false) }
        var showRegionDrop by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Profilni tahrirlash", fontWeight = FontWeight.Bold, color = AgroGreen) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("To'liq ismingiz") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Role Selector Dropdown
                    ExposedDropdownMenuBox(
                        expanded = showRoleDrop,
                        onExpandedChange = { showRoleDrop = it }
                    ) {
                        OutlinedTextField(
                            value = tempRole,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Roli") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showRoleDrop) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = showRoleDrop,
                            onDismissRequest = { showRoleDrop = false }
                        ) {
                            roles.forEach { role ->
                                DropdownMenuItem(
                                    text = { Text(role) },
                                    onClick = {
                                        tempRole = role
                                        showRoleDrop = false
                                    }
                                )
                            }
                        }
                    }

                    // Region Dropdown
                    ExposedDropdownMenuBox(
                        expanded = showRegionDrop,
                        onExpandedChange = { showRegionDrop = it }
                    ) {
                        OutlinedTextField(
                            value = tempRegion,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Viloyat") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showRegionDrop) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = showRegionDrop,
                            onDismissRequest = { showRegionDrop = false }
                        ) {
                            regions.forEach { reg ->
                                DropdownMenuItem(
                                    text = { Text(reg) },
                                    onClick = {
                                        tempRegion = reg
                                        showRegionDrop = false
                                    }
                                )
                            }
                        }
                    }

                    // Payments String Input
                    OutlinedTextField(
                        value = tempPayment,
                        onValueChange = { tempPayment = it },
                        label = { Text("To'lov usullari (M: Payme, Click)") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateProfile(tempName, tempRole, tempRegion, tempPayment)
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AgroGreen)
                ) {
                    Text("Saqlash")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Bekor qilish", color = Color.Gray)
                }
            }
        )
    }
}
