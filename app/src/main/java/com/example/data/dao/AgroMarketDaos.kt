package com.example.data.dao

import androidx.room.*
import com.example.data.model.ChatMessage
import com.example.data.model.Product
import com.example.data.model.UserSession
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSessionDao {
    @Query("SELECT * FROM user_sessions WHERE id = 1 LIMIT 1")
    fun getUserSession(): Flow<UserSession?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserSession(session: UserSession)

    @Query("DELETE FROM user_sessions")
    suspend fun clearSession()
}

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY isPremium DESC, timestamp DESC")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE category = :category ORDER BY isPremium DESC, timestamp DESC")
    fun getProductsByCategory(category: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY isPremium DESC, timestamp DESC")
    fun searchProducts(query: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    fun getProductById(id: Int): Flow<Product?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)
}

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessagesForChat(chatId: String): Flow<List<ChatMessage>>

    @Query("SELECT * FROM chat_messages WHERE senderPhone = :phone OR receiverPhone = :phone ORDER BY timestamp DESC")
    fun getAllMessagesForUser(phone: String): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    @Query("UPDATE chat_messages SET isRead = 1 WHERE chatId = :chatId AND receiverPhone = :receiverPhone")
    suspend fun markMessagesAsRead(chatId: String, receiverPhone: String)
}
