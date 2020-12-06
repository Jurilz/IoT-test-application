package com.example.testapplication.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.testapplication.domain.Url
import com.example.testapplication.network.ApiModel
import com.example.testapplication.network.Body
import com.example.testapplication.network.Param
import com.example.testapplication.network.Services

@androidx.room.Database(
    entities = [Url::class],
    version = 4,
    exportSchema = false
)
abstract class Database: RoomDatabase() {

    abstract val urlDao: UrlDao
}

private lateinit var INSTANCE: Database

fun getDatabase(context: Context): Database {
    synchronized(Database::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    "Database"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}

@Dao
interface UrlDao {

    @Query("SELECT * FROM url ORDER BY url.timestamp DESC LIMIT 1")
    fun getLastUrl(): Url?

    @Insert(onConflict = REPLACE)
    fun setLastUrl(url: Url)
}

