package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.ChatMessageDao
import com.example.data.dao.ProductDao
import com.example.data.dao.UserSessionDao
import com.example.data.model.ChatMessage
import com.example.data.model.Product
import com.example.data.model.UserSession

@Database(
    entities = [UserSession::class, Product::class, ChatMessage::class],
    version = 1,
    exportSchema = false
)
abstract class AgroMarketDatabase : RoomDatabase() {
    abstract fun userSessionDao(): UserSessionDao
    abstract fun productDao(): ProductDao
    abstract fun chatMessageDao(): ChatMessageDao

    companion object {
        @Volatile
        private var INSTANCE: AgroMarketDatabase? = null

        fun getDatabase(context: Context): AgroMarketDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AgroMarketDatabase::class.java,
                    "agromarket_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
