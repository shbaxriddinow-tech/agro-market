package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.db.AgroMarketDatabase
import com.example.data.model.Product
import com.example.data.repository.AgroMarketRepository
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.AgroGreen
import com.example.ui.viewmodel.AgroMarketViewModel
import com.example.ui.viewmodel.AgroMarketViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize Room Database, DAO and Repository
        val database = AgroMarketDatabase.getDatabase(applicationContext)
        val repository = AgroMarketRepository(
            database.userSessionDao(),
            database.productDao(),
            database.chatMessageDao()
        )
        
        // Initialize ViewModel via our Custom Factory
        val factory = AgroMarketViewModelFactory(application, repository)
        val viewModel = ViewModelProvider(this, factory)[AgroMarketViewModel::class.java]

        setContent {
            MyApplicationTheme {
                val userSessionState by viewModel.userSession.collectAsStateWithLifecycle()
                
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val session = userSessionState
                    if (session == null || !session.isLoggedIn) {
                        // Show Login/Onboarding screen first
                        LoginScreen(
                            onLoginSuccess = { phone, name, role, region ->
                                viewModel.login(phone, name, role, region)
                            }
                        )
                    } else {
                        // Main App scaffold
                        MainAppContent(viewModel = viewModel, currentUserPhone = session.phoneNumber)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(
    viewModel: AgroMarketViewModel,
    currentUserPhone: String
) {
    var currentTab by remember { mutableStateOf("home") }
    var selectedProductForDetail by remember { mutableStateOf<Product?>(null) }
    
    // State collection from the ViewModel
    val products by viewModel.filteredProducts.collectAsStateWithLifecycle()
    val allProductsRaw by viewModel.allProducts.collectAsStateWithLifecycle()
    val userSession by viewModel.userSession.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val locationFilter by viewModel.locationFilter.collectAsStateWithLifecycle()
    val activeChatId by viewModel.activeChatId.collectAsStateWithLifecycle()
    val activeChatMessages by viewModel.activeChatMessages.collectAsStateWithLifecycle()
    val chatThreads by viewModel.chatThreads.collectAsStateWithLifecycle()

    // Calculate dynamic stats
    val userAdsCount = remember(allProductsRaw, currentUserPhone) {
        allProductsRaw.count { it.sellerPhone == currentUserPhone }
    }
    
    val totalUnreadChats = remember(chatThreads) {
        chatThreads.sumOf { it.unreadCount }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Hide bottom bar when viewing product details or active chat to maximize screen space
            if (selectedProductForDetail == null && activeChatId == null) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    modifier = Modifier.testTag("main_bottom_navigation")
                ) {
                    // Home Tab
                    NavigationBarItem(
                        selected = currentTab == "home",
                        onClick = { currentTab = "home" },
                        icon = { Icon(if (currentTab == "home") Icons.Default.Home else Icons.Outlined.Home, contentDescription = "Bosh sahifa") },
                        label = { Text("Bosh sahifa", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("nav_tab_home")
                    )

                    // Catalog Tab
                    NavigationBarItem(
                        selected = currentTab == "catalog",
                        onClick = { currentTab = "catalog" },
                        icon = { Icon(if (currentTab == "catalog") Icons.Default.GridView else Icons.Outlined.GridView, contentDescription = "Katalog") },
                        label = { Text("Katalog", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("nav_tab_catalog")
                    )

                    // Add Ad Tab
                    NavigationBarItem(
                        selected = currentTab == "add_ad",
                        onClick = { currentTab = "add_ad" },
                        icon = { Icon(if (currentTab == "add_ad") Icons.Default.AddCircle else Icons.Outlined.AddCircle, contentDescription = "E'lon berish", tint = AgroGreen) },
                        label = { Text("E'lon berish", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("nav_tab_add")
                    )

                    // Chat Tab (with notifications badge)
                    NavigationBarItem(
                        selected = currentTab == "chat",
                        onClick = { currentTab = "chat" },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (totalUnreadChats > 0) {
                                        Badge { Text("$totalUnreadChats") }
                                    }
                                }
                            ) {
                                Icon(if (currentTab == "chat") Icons.Default.ChatBubble else Icons.Outlined.ChatBubble, contentDescription = "Chat")
                            }
                        },
                        label = { Text("Chat", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("nav_tab_chat")
                    )

                    // Profile Tab
                    NavigationBarItem(
                        selected = currentTab == "profile",
                        onClick = { currentTab = "profile" },
                        icon = { Icon(if (currentTab == "profile") Icons.Default.Person else Icons.Outlined.Person, contentDescription = "Profil") },
                        label = { Text("Profil", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("nav_tab_profile")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main views switcher
            when (currentTab) {
                "home" -> HomeScreen(
                    products = products,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                    onCategorySelect = { category ->
                        viewModel.setSelectedCategory(category)
                        currentTab = "catalog"
                    },
                    onProductSelect = { product -> selectedProductForDetail = product },
                    userName = userSession?.fullName ?: ""
                )
                "catalog" -> CatalogScreen(
                    products = products,
                    selectedCategory = selectedCategory,
                    onCategorySelect = { viewModel.setSelectedCategory(it) },
                    locationFilter = locationFilter,
                    onLocationFilterChange = { viewModel.setLocationFilter(it) },
                    onProductSelect = { product -> selectedProductForDetail = product }
                )
                "add_ad" -> AddAdScreen(
                    onAdSubmitted = { t, c, d, p, pt, q, l ->
                        viewModel.addProduct(t, c, d, p, pt, q, l)
                        currentTab = "home"
                    }
                )
                "chat" -> ChatListScreen(
                    threads = chatThreads,
                    activeChatId = activeChatId,
                    activeMessages = activeChatMessages,
                    currentUserPhone = currentUserPhone,
                    onThreadSelect = { id -> viewModel.setActiveChat(id) },
                    onSendMessage = { cid, text -> viewModel.sendMessage(cid, text) }
                )
                "profile" -> userSession?.let { session ->
                    ProfileScreen(
                        userSession = session,
                        activeAdsCount = userAdsCount,
                        onUpdateProfile = { name, role, reg, pay ->
                            viewModel.updateProfile(name, role, reg, pay)
                        },
                        onLanguageChange = { lang ->
                            viewModel.setLanguage(lang)
                        },
                        onLogout = {
                            viewModel.logout()
                        }
                    )
                }
            }

            // Overlay Detail Screen
            AnimatedVisibility(
                visible = selectedProductForDetail != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                selectedProductForDetail?.let { product ->
                    ProductDetailScreen(
                        product = product,
                        currentUserPhone = currentUserPhone,
                        onNavigateToChat = { chatId ->
                            viewModel.setActiveChat(chatId)
                            selectedProductForDetail = null
                            currentTab = "chat"
                        },
                        onBack = { selectedProductForDetail = null }
                    )
                }
            }
        }
    }
}
