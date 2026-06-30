package com.example.data.repository

import com.example.data.dao.ChatMessageDao
import com.example.data.dao.ProductDao
import com.example.data.dao.UserSessionDao
import com.example.data.model.ChatMessage
import com.example.data.model.Product
import com.example.data.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class AgroMarketRepository(
    private val userSessionDao: UserSessionDao,
    private val productDao: ProductDao,
    private val chatMessageDao: ChatMessageDao
) {
    val userSession: Flow<UserSession?> = userSessionDao.getUserSession()
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    fun getProductsByCategory(category: String): Flow<List<Product>> {
        return productDao.getProductsByCategory(category)
    }

    fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query)
    }

    fun getProductById(id: Int): Flow<Product?> {
        return productDao.getProductById(id)
    }

    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    suspend fun saveUserSession(session: UserSession) {
        userSessionDao.insertUserSession(session)
    }

    suspend fun clearSession() {
        userSessionDao.clearSession()
        // Save an empty logged out session
        userSessionDao.insertUserSession(UserSession(id = 1, isLoggedIn = false))
    }

    fun getMessagesForChat(chatId: String): Flow<List<ChatMessage>> {
        return chatMessageDao.getMessagesForChat(chatId)
    }

    fun getAllMessagesForUser(phone: String): Flow<List<ChatMessage>> {
        return chatMessageDao.getAllMessagesForUser(phone)
    }

    suspend fun insertMessage(message: ChatMessage) {
        chatMessageDao.insertMessage(message)
    }

    suspend fun markMessagesAsRead(chatId: String, receiverPhone: String) {
        chatMessageDao.markMessagesAsRead(chatId, receiverPhone)
    }

    // Prepopulate data if database is empty
    suspend fun initializeDatabase() {
        val currentSession = userSession.firstOrNull()
        if (currentSession == null) {
            userSessionDao.insertUserSession(UserSession(id = 1, isLoggedIn = false))
        }

        val existingProducts = allProducts.first()
        if (existingProducts.isEmpty()) {
            val prepopulated = listOf(
                Product(
                    title = "Lazer Guruchi (Xorazm Premium)",
                    category = "Ekinlar 🌾",
                    price = 18000.0,
                    priceType = "Chakana",
                    quantity = "1000 kg",
                    location = "Xorazm, Shovot",
                    description = "Xorazmning eng sara Lazer guruchi. Tosh va jallardan tozalangan, birinchi nav. Guruch o'ta mazzali va palov uchun ayni muddao! 50 kg lik qoplarda. Minimal xarid - 50 kg.",
                    sellerName = "Rustam Dehqon",
                    sellerPhone = "+998901234567",
                    sellerRating = 4.9f,
                    isPremium = true
                ),
                Product(
                    title = "Namangan Qizil Olmasi (Eksportbop)",
                    category = "Ekinlar 🌾",
                    price = 8500.0,
                    priceType = "Optom",
                    quantity = "5 tonna",
                    location = "Namangan, Kosonsoy",
                    description = "Mazzali, sersuv va shirin qizil olmalar. Eksport uchun maxsus saralangan. Chiroyli plastik qutilarga solingan. Sovuqxonada saqlangan, sifati kafolatlanadi.",
                    sellerName = "Dilshodbek Agro",
                    sellerPhone = "+998912223344",
                    sellerRating = 4.7f,
                    isPremium = false
                ),
                Product(
                    title = "Golshtin Zotli Sog'in Sigir",
                    category = "Chorva 🐄",
                    price = 22000000.0,
                    priceType = "Kelishilgan",
                    quantity = "1 bosh",
                    location = "Toshkent viloyati, Zangiota",
                    description = "Golshtin-Friz zotli sog'lom va yuvosh sigir. Kunlik sut miqdori 26-28 litr. Ikkinchi marta tug'ishi. Emlash va tibbiy daftarchasi bor.",
                    sellerName = "Hikmatullo",
                    sellerPhone = "+998935556677",
                    sellerRating = 4.8f,
                    isPremium = true
                ),
                Product(
                    title = "Traktor TTZ-80.11 plugi bilan",
                    category = "Texnika 🚜",
                    price = 95000000.0,
                    priceType = "Kelishilgan",
                    quantity = "1 dona",
                    location = "Samarqand, Jomboy",
                    description = "TTZ-80.11 traktori. Ishchi holatda, motor va karobka kapital ta'mirdan chiqqan. Balonlari yangi. Plugi sovg'a sifatida qo'shib beriladi. Hujjatlari 100% joyida.",
                    sellerName = "Sardor Mashinasoz",
                    sellerPhone = "+998941112233",
                    sellerRating = 4.4f,
                    isPremium = false
                ),
                Product(
                    title = "Karbamid azotli o'g'it (Farg'onaazot)",
                    category = "Agrokimyo 🧪",
                    price = 4500.0,
                    priceType = "Optom",
                    quantity = "10 tonna",
                    location = "Farg'ona",
                    description = "Farg'onaazot AJ tomonidan ishlab chiqarilgan 46% azotli yuqori sifatli Karbamid o'g'iti. 50 kg lik qoplarda. Bahorgi va kuzgi oziqlantirish uchun mukammal.",
                    sellerName = "Farhod Agro-Kimyo",
                    sellerPhone = "+998998889900",
                    sellerRating = 4.6f,
                    isPremium = true
                ),
                Product(
                    title = "Hisor Zotli Qo'chqorlar",
                    category = "Chorva 🐄",
                    price = 4200000.0,
                    priceType = "Chakana",
                    quantity = "15 bosh",
                    location = "Surxondaryo, Boysun",
                    description = "Haqiqiy tog' sharoitida boqilgan zotdor Hisor qo'chqorlari. Semiz va sog'lom. Bo'yi baland, dumi katta. Qurbonlik va marosimlar uchun juda munosib.",
                    sellerName = "Abdumalik Cho'pon",
                    sellerPhone = "+998974445566",
                    sellerRating = 4.9f,
                    isPremium = false
                )
            )
            for (p in prepopulated) {
                productDao.insertProduct(p)
            }
        }
    }
}
