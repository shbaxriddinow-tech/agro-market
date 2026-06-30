package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiClient
import com.example.data.db.AgroMarketDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.Product
import com.example.data.model.UserSession
import com.example.data.repository.AgroMarketRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatThread(
    val chatId: String,
    val productId: Int,
    val productTitle: String,
    val otherPartyName: String,
    val otherPartyPhone: String,
    val lastMessage: String,
    val lastMessageTime: Long,
    val unreadCount: Int,
    val isOnline: Boolean = true
)

class AgroMarketViewModel(
    application: Application,
    private val repository: AgroMarketRepository
) : AndroidViewModel(application) {

    // Central state declarations
    val userSession: StateFlow<UserSession?> = repository.userSession
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allProducts: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedCategory = MutableStateFlow("Barchasi")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _locationFilter = MutableStateFlow("Barchasi")
    val locationFilter: StateFlow<String> = _locationFilter.asStateFlow()

    // Dynamic product filtering in Kotlin code to ensure speed and reactivity
    val filteredProducts: StateFlow<List<Product>> = combine(
        allProducts,
        _selectedCategory,
        _searchQuery,
        _locationFilter
    ) { products, category, query, loc ->
        products.filter { p ->
            val matchesCategory = if (category == "Barchasi") true else p.category == category
            val matchesQuery = if (query.isEmpty()) true else {
                p.title.contains(query, ignoreCase = true) || 
                p.description.contains(query, ignoreCase = true) ||
                p.sellerName.contains(query, ignoreCase = true)
            }
            val matchesLocation = if (loc == "Barchasi") true else p.location.contains(loc, ignoreCase = true)
            matchesCategory && matchesQuery && matchesLocation
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active chat management
    private val _activeChatId = MutableStateFlow<String?>(null)
    val activeChatId: StateFlow<String?> = _activeChatId.asStateFlow()

    val activeChatMessages: StateFlow<List<ChatMessage>> = _activeChatId
        .flatMapLatest { chatId ->
            if (chatId != null) {
                repository.getMessagesForChat(chatId)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Derive chat threads reactively for the logged-in user
    val chatThreads: StateFlow<List<ChatThread>> = combine(
        userSession,
        allProducts
    ) { session, products ->
        session to products
    }.flatMapLatest { (session, products) ->
        if (session == null || !session.isLoggedIn || session.phoneNumber.isEmpty()) {
            flowOf(emptyList())
        } else {
            repository.getAllMessagesForUser(session.phoneNumber).map { messages ->
                // Group messages by chatId
                messages.groupBy { it.chatId }.mapNotNull { (chatId, chatMsgs) ->
                    val lastMsg = chatMsgs.firstOrNull() ?: return@mapNotNull null
                    
                    // Parse chatId: buyerPhone_sellerPhone_productId
                    val parts = chatId.split("_")
                    if (parts.size < 3) return@mapNotNull null
                    
                    val buyerPhone = parts[0]
                    val sellerPhone = parts[1]
                    val productId = parts[2].toIntOrNull() ?: 0
                    
                    val otherPhone = if (session.phoneNumber == buyerPhone) sellerPhone else buyerPhone
                    
                    // Retrieve associated product
                    val product = products.find { it.id == productId }
                    val productTitle = product?.title ?: "Agro Mahsulot"
                    
                    val otherName = if (otherPhone == sellerPhone && product != null) {
                        product.sellerName
                    } else {
                        // Look for any message to infer name, or use default
                        if (otherPhone == "+998901234567") "Rustam Dehqon"
                        else if (otherPhone == "+998912223344") "Dilshodbek Agro"
                        else if (otherPhone == "+998935556677") "Hikmatillo"
                        else if (otherPhone == "+998941112233") "Sardor Mashinasoz"
                        else if (otherPhone == "+998998889900") "Farhod Agro-Kimyo"
                        else if (otherPhone == "+998974445566") "Abdumalik Cho'pon"
                        else "Agro Hamkor"
                    }
                    
                    val unreadCount = chatMsgs.count { 
                        it.receiverPhone == session.phoneNumber && !it.isRead 
                    }

                    ChatThread(
                        chatId = chatId,
                        productId = productId,
                        productTitle = productTitle,
                        otherPartyName = otherName,
                        otherPartyPhone = otherPhone,
                        lastMessage = lastMsg.messageText,
                        lastMessageTime = lastMsg.timestamp,
                        unreadCount = unreadCount,
                        isOnline = true
                    )
                }.sortedByDescending { it.lastMessageTime }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.initializeDatabase()
        }
    }

    // Filter controls
    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setLocationFilter(loc: String) {
        _locationFilter.value = loc
    }

    // User authentication
    fun login(phoneNumber: String, fullName: String, userType: String, location: String) {
        viewModelScope.launch {
            val session = UserSession(
                id = 1,
                phoneNumber = phoneNumber,
                fullName = fullName,
                userType = userType,
                location = location,
                isLoggedIn = true,
                language = "O'zbekcha"
            )
            repository.saveUserSession(session)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.clearSession()
        }
    }

    fun updateProfile(fullName: String, userType: String, location: String, paymentMethods: String) {
        viewModelScope.launch {
            userSession.value?.let { current ->
                val updated = current.copy(
                    fullName = fullName,
                    userType = userType,
                    location = location,
                    paymentMethods = paymentMethods
                )
                repository.saveUserSession(updated)
            }
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            userSession.value?.let { current ->
                repository.saveUserSession(current.copy(language = lang))
            }
        }
    }

    // Product operations (CRUD)
    fun addProduct(
        title: String,
        category: String,
        description: String,
        price: Double,
        priceType: String,
        quantity: String,
        location: String,
        imageUrl: String = ""
    ) {
        viewModelScope.launch {
            val session = userSession.value
            val sellerName = session?.fullName ?: "Nomalum Dehqon"
            val sellerPhone = session?.phoneNumber ?: "+998900000000"
            
            val newProduct = Product(
                title = title,
                category = category,
                description = description,
                price = price,
                priceType = priceType,
                quantity = quantity,
                location = location,
                sellerName = sellerName,
                sellerPhone = sellerPhone,
                imageUrl = imageUrl,
                sellerRating = 5.0f,
                isPremium = false
            )
            repository.insertProduct(newProduct)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }

    fun toggleProductPremium(productId: Int) {
        viewModelScope.launch {
            val product = allProducts.value.find { it.id == productId }
            if (product != null) {
                repository.updateProduct(product.copy(isPremium = !product.isPremium))
            }
        }
    }

    // Chat operations
    fun setActiveChat(chatId: String?) {
        _activeChatId.value = chatId
        if (chatId != null) {
            viewModelScope.launch {
                userSession.value?.let { session ->
                    repository.markMessagesAsRead(chatId, session.phoneNumber)
                }
            }
        }
    }

    fun sendMessage(chatId: String, text: String) {
        val session = userSession.value ?: return
        if (!session.isLoggedIn || session.phoneNumber.isEmpty()) return

        val parts = chatId.split("_")
        if (parts.size < 3) return
        val buyerPhone = parts[0]
        val sellerPhone = parts[1]
        val productId = parts[2].toIntOrNull() ?: 0

        val receiverPhone = if (session.phoneNumber == buyerPhone) sellerPhone else buyerPhone

        viewModelScope.launch {
            val userMsg = ChatMessage(
                chatId = chatId,
                senderPhone = session.phoneNumber,
                receiverPhone = receiverPhone,
                messageText = text,
                isRead = false
            )
            repository.insertMessage(userMsg)

            // Trigger simulated real-time response from seller
            simulateSellerResponse(chatId, receiverPhone, text, productId)
        }
    }

    private fun simulateSellerResponse(chatId: String, sellerPhone: String, userMessage: String, productId: Int) {
        viewModelScope.launch {
            // Delay to simulate human typing and network latency
            delay(1500)

            val product = allProducts.value.find { it.id == productId }
            val productTitle = product?.title ?: "Mahsulot"
            val sellerName = product?.sellerName ?: "Sotuvchi"

            // System instructions for Gemini AI call
            val systemInstruction = """
                Siz O'zbekistondagi dehqon/sotuvchisiz. Ismingiz: $sellerName. 
                Sizning e'loningiz: '$productTitle' (Kategoriya: ${product?.category ?: ""}, Narxi: ${product?.price ?: ""} so'm, Manzili: ${product?.location ?: ""}).
                Xaridor sizga chatda shunday deb yozdi: '$userMessage'.
                Siz unga o'zbek tilida, nihoyatda samimiy, dehqoncha ohangda javob qaytaring (maksimal 1-2 gap). 
                Javobingizda uni aka, uka deb gapirishingiz, kelib ko'rishini yoki telefon qilishini taklif qilishingiz mumkin.
                Faqat va faqat javob matnini qaytaring. Hech qanday qo'shimcha tushuntirish yozmang.
            """.trimIndent()

            val prompt = "Xaridor xabari: $userMessage"

            // Attempt to fetch AI response using GeminiClient
            var replyText = GeminiClient.generateResponse(prompt, systemInstruction)

            // Dynamic rule-based fallback if Gemini is offline/unconfigured
            if (replyText == null || replyText.trim().isEmpty()) {
                val cleanedMsg = userMessage.lowercase()
                replyText = when {
                    cleanedMsg.contains("salom") || cleanedMsg.contains("assalom") -> {
                        "Va alaykum assalom! Baraka toping aka, mahsulotimiz hozir bor. Kelib bemalol ko'rishingiz mumkin."
                    }
                    cleanedMsg.contains("narx") || cleanedMsg.contains("necha") || cleanedMsg.contains("qancha") || cleanedMsg.contains("so'm") -> {
                        "E'londagi narxini kelishadigan joyi bor uka. Real xaridor bo'lsangiz, telefon qiling kelishamiz."
                    }
                    cleanedMsg.contains("joy") || cleanedMsg.contains("manzil") || cleanedMsg.contains("qayer") -> {
                        "Manzilimiz: ${product?.location ?: "Toshkent"}. Kelishdan oldin telefon qilsangiz, kutib olaman."
                    }
                    cleanedMsg.contains("sifat") || cleanedMsg.contains("yaxshimi") || cleanedMsg.contains("yangi") -> {
                        "Sifatiga 100% javob beraman, o'zimiz halol yetishtirganmiz. Hali hech kim shikoyat qilmagan."
                    }
                    cleanedMsg.contains("optom") || cleanedMsg.contains("ko'p") || cleanedMsg.contains("tonna") -> {
                        "Ha, optomga olsangiz, narxidan yaxshigina o'tib beramiz. Kelib tarozida o'lchab olib ketasiz."
                    }
                    else -> {
                        "Rahmat qiziqishingiz uchun! E'lon bo'yicha batafsil gaplashish uchun qo'ng'iroq qilsangiz, ayni muddao bo'lar edi. Telefonim: $sellerPhone."
                    }
                }
            }

            val sellerMsg = ChatMessage(
                chatId = chatId,
                senderPhone = sellerPhone,
                receiverPhone = userSession.value?.phoneNumber ?: "",
                messageText = replyText,
                isRead = false
            )
            repository.insertMessage(sellerMsg)
        }
    }
}

class AgroMarketViewModelFactory(
    private val application: Application,
    private val repository: AgroMarketRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AgroMarketViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AgroMarketViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
