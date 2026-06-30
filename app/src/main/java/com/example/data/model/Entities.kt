package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_sessions")
data class UserSession(
    @PrimaryKey val id: Int = 1,
    val phoneNumber: String = "",
    val fullName: String = "",
    val userType: String = "Xaridor", // Dehqon (sotuvchi), Ishlab chiqaruvchi (texnika/agrokimyo), Texnik xizmat ko'rsatuvchi, Xaridor
    val location: String = "Toshkent",
    val rating: Float = 5.0f,
    val paymentMethods: String = "Payme, Click",
    val language: String = "O'zbekcha",
    val isLoggedIn: Boolean = false
) : Serializable

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // Ekinlar 🌾, Chorva 🐄, Texnika 🚜, Agrokimyo 🧪
    val imageUrl: String = "",
    val description: String,
    val price: Double,
    val priceType: String = "Chakana", // Chakana / Optom / Kelishilgan
    val quantity: String, // Masalan: "500 kg", "10 ta"
    val location: String, // Viloyat/Tuman (Masalan: "Andijon", "Farg'ona")
    val sellerName: String,
    val sellerPhone: String,
    val sellerRating: Float = 4.5f,
    val isPremium: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
) : Serializable

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val chatId: String, // chat_roomId (buyerPhone + "_" + sellerPhone + "_" + productId)
    val senderPhone: String,
    val receiverPhone: String,
    val messageText: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
) : Serializable
