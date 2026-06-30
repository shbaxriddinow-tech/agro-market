package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AgroGold
import com.example.ui.theme.AgroGreen
import com.example.ui.theme.AgroGreenLight
import com.example.ui.theme.AgroTerracotta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (phone: String, name: String, role: String, region: String) -> Unit
) {
    var language by remember { mutableStateOf("O'zbekcha") }
    var phoneNumber by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var userRole by remember { mutableStateOf("Xaridor") }
    var region by remember { mutableStateOf("Toshkent") }
    
    var showRoleDropdown by remember { mutableStateOf(false) }
    var showRegionDropdown by remember { mutableStateOf(false) }
    
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Decorative Top Banner Background Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(AgroGreen, AgroGreen.copy(alpha = 0.7f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Language Switcher
            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "UZ",
                    fontWeight = FontWeight.Bold,
                    color = if (language == "O'zbekcha") AgroGold else Color.White,
                    modifier = Modifier
                        .clickable { language = "O'zbekcha" }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("lang_uz")
                )
                Text(
                    text = "|",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 14.sp
                )
                Text(
                    text = "RU",
                    fontWeight = FontWeight.Bold,
                    color = if (language == "Rus tili") AgroGold else Color.White,
                    modifier = Modifier
                        .clickable { language = "Rus tili" }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("lang_ru")
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Logo & Welcome Illustration
            Icon(
                imageVector = Icons.Filled.Agriculture,
                contentDescription = "AgroMarket Logo",
                tint = AgroGold,
                modifier = Modifier
                    .size(84.dp)
                    .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(24.dp))
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "AgroMarket",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 1.sp
            )

            Text(
                text = if (language == "O'zbekcha") 
                    "Dehqon va xaridorlarni birlashtiruvchi bepul platforma" 
                else "Бесплатная платформа для фермеров и покупателей",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Card Container for Inputs
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (language == "O'zbekcha") "Kirish va Ro'yxatdan o'tish" else "Вход и Регистрация",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgroGreen,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    // Phone Input
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text(if (language == "O'zbekcha") "Telefon raqamingiz" else "Номер телефона") },
                        placeholder = { Text("+998 90 123 45 67") },
                        prefix = { Text("+998 ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = AgroGreen) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("phone_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Full Name Input
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text(if (language == "O'zbekcha") "Ism-familiyangiz" else "Имя и Фамилия") },
                        placeholder = { Text("Eshmat Toshmatov") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = AgroGreen) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("name_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Role Selector Dropdown
                    ExposedDropdownMenuBox(
                        expanded = showRoleDropdown,
                        onExpandedChange = { showRoleDropdown = it }
                    ) {
                        OutlinedTextField(
                            value = userRole,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(if (language == "O'zbekcha") "Ilovadagi rolingiz" else "Ваша роль") },
                            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = AgroGreen) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showRoleDropdown) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .testTag("role_selector"),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = showRoleDropdown,
                            onDismissRequest = { showRoleDropdown = false }
                        ) {
                            roles.forEach { role ->
                                DropdownMenuItem(
                                    text = { Text(role) },
                                    onClick = {
                                        userRole = role
                                        showRoleDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    // Province Selection
                    ExposedDropdownMenuBox(
                        expanded = showRegionDropdown,
                        onExpandedChange = { showRegionDropdown = it }
                    ) {
                        OutlinedTextField(
                            value = region,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(if (language == "O'zbekcha") "Hududingiz" else "Ваш регион") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = AgroGreen) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showRegionDropdown) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .testTag("region_selector"),
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
                                        region = reg
                                        showRegionDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Error warning
                    val isFormValid = phoneNumber.length >= 7 && fullName.trim().isNotEmpty()

                    // Submit Button
                    Button(
                        onClick = {
                            if (isFormValid) {
                                val fullPhone = if (phoneNumber.startsWith("+998")) phoneNumber else "+998$phoneNumber"
                                onLoginSuccess(fullPhone, fullName, userRole, region)
                            }
                        },
                        enabled = isFormValid,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("login_submit_button"),
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
                            Icon(Icons.Default.Login, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == "O'zbekcha") "Ilovaga kirish" else "Войти в приложение",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Footer Agro Hint
            Card(
                colors = CardDefaults.cardColors(containerColor = AgroGreenLight.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = AgroGreen,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (language == "O'zbekcha")
                            "Barcha ma'lumotlar xavfsiz saqlanadi. Dehqonlarimiz mahsulotlarini vositachilarsiz soting."
                        else "Все данные надежно защищены. Продавайте продукцию без посредников.",
                        fontSize = 12.sp,
                        color = AgroGreen
                    )
                }
            }
        }
    }
}
