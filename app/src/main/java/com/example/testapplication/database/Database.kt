package com.example.testapplication.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.testapplication.domain.*
import kotlinx.coroutines.flow.Flow

@androidx.room.Database(
    entities = [
        Url::class,
        SingleResponse::class,
        FlagResponse::class,
        ApiModel::class,
        DomainMeasure::class,
        Service::class],
    version = 21,
    exportSchema = false
)
abstract class Database: RoomDatabase() {

    abstract val urlDao: UrlDao

    abstract val singleResponseDao: SingleResponseDao

    abstract val flagResponseDao: FlagResponseDao

    abstract val timeseriesResponseDao: TimeseriesResponseDao

    abstract val apiModelDao: ApiModelDao

    abstract val serviceDao: ServiceDao
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

@Dao
interface SingleResponseDao {

    @Query("SELECT * FROM singleresponse WHERE apiBase = :apiBase AND endpoint = :endpoint")
    fun getByApiBaseAndEndpoint(apiBase: String, endpoint: String): SingleResponse?

    @Insert(onConflict = REPLACE)
    fun setSingle(singleResponse: SingleResponse)
}

@Dao
interface FlagResponseDao {

    @Query("SELECT * FROM flagresponse WHERE apiBase = :apiBase AND endpoint = :endpoint")
    fun getByApiBaseAndEndpoint(apiBase: String, endpoint: String): FlagResponse?

    @Insert(onConflict = REPLACE)
    fun setFlag(flagResponse: FlagResponse)
}

@Dao
interface TimeseriesResponseDao {

    @Insert(onConflict = REPLACE)
    fun insertTimeseries(timeseries: List<DomainMeasure>)

    @Query("SELECT * FROM (SELECT * FROM domainmeasure WHERE apiBase = :apiBase ORDER BY domainmeasure.timestamp DESC LIMIT :limit) ORDER BY timestamp ASC")
    fun getByApiBaseLimited(apiBase: String, limit: Int): List<DomainMeasure>?

    @Query("SELECT * FROM (SELECT * FROM domainmeasure WHERE apiBase = :apiBase ORDER BY domainmeasure.timestamp DESC LIMIT :limit) ORDER BY timestamp ASC")
    fun getSomeByApiBaseLimited(apiBase: String, limit: Int): Flow<List<DomainMeasure>?>

}

@Dao
interface ApiModelDao {

    @Query("SELECT * FROM apimodel WHERE name = :name AND apiBase = :apiBase")
    fun getApiModelByNameAndApiBase(name: String, apiBase: String): LiveData<ApiModel>

    @Query("SELECT * FROM apimodel ORDER BY apimodel.timestamp DESC LIMIT 1")
    fun getLatestApiModel(): LiveData<ApiModel>

    @Query("SELECT * FROM apimodel ORDER BY apimodel.timestamp DESC LIMIT 1")
    fun getLastApiModel(): Flow<ApiModel?>

    @Insert(onConflict = REPLACE)
    fun setApiModel(apiModel: ApiModel)
}

@Dao
interface ServiceDao {

    @Query("SELECT * FROM service WHERE apiBase = :apiBase")
    fun getServicesByApiBase(apiBase: String): LiveData<List<Service>>

    @Query("SELECT * FROM service WHERE apiBase = :apiBase AND method = \"GET\" AND name NOT LIKE :filter")
    fun getGETServicesByApiBase(apiBase: String, filter: String): LiveData<List<Service>>

    @Query("SELECT * FROM service WHERE apiBase = :apiBase AND method = \"GET\" AND name NOT LIKE :filter")
    fun testGETServicesByApiBase(apiBase: String, filter: String): List<Service>

    @Query("SELECT * FROM service WHERE apiBase = :apiBase AND method = \"GET\" AND name NOT LIKE :filter")
    fun getGETSomeServicesByApiBase(apiBase: String, filter: String): Flow<List<Service>?>

    @Query("SELECT * FROM service ORDER BY service.timestamp DESC LIMIT 8")
    fun getLatestServices(): LiveData<List<Service>>

    @Insert(onConflict = REPLACE)
    fun insertService(service: Service)
}

