package com.example.testapplication.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.testapplication.database.Database
import com.example.testapplication.database.getDatabase
import com.example.testapplication.domain.*
import com.example.testapplication.network.*
import com.example.testapplication.utility.asDomainFlag
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception
import java.time.ZonedDateTime
import java.util.*

private const val MEASUREMENT_LIMIT: Int = 50

class QrRepository(private val database: Database) {

    constructor(context: Context) : this(getDatabase(context))

    private val _loadingStatus = MutableLiveData(false)
    val loadingStatus: LiveData<Boolean> = _loadingStatus

    val currentApiModel by lazy {
        database.apiModelDao.getLatestApiModel()
    }

    val apiModel: Flow<ApiModel> = database.apiModelDao.getLastApiModel()

    val currentServices: Flow<List<Service>?> = apiModel.flatMapLatest {
        database.serviceDao.getGETSomeServicesByApiBase(it.apiBase, "Get one value")
    }

    val currentMeasurements: Flow<List<DomainMeasure>?> = apiModel.flatMapLatest {
        database.timeseriesResponseDao.getSomeByApiBaseLimited(it.apiBase, MEASUREMENT_LIMIT)
    }

    fun getCurrentService(apiModel: ApiModel): Flow<List<Service>?> {
        return database.serviceDao.getGETSomeServicesByApiBase(apiModel.apiBase, "Get one value")
    }

    fun getCurrentMeasurements(apiModel: ApiModel): Flow<List<DomainMeasure>?> {
        return database.timeseriesResponseDao.getSomeByApiBaseLimited(apiModel.apiBase, MEASUREMENT_LIMIT)
    }

//    val currentServices: MediatorLiveData<LiveData<List<Service>>> = MediatorLiveData()

//    val currentMeasurements: MediatorLiveData<List<DomainMeasure>> = MediatorLiveData()

    suspend fun getUrl(): Url? = run {
        withContext(Dispatchers.IO) {
            database.urlDao.getLastUrl()
        }
    }

    suspend fun setUrl(url: Url) {
        withContext(Dispatchers.IO) {
            database.urlDao.apply {
                setLastUrl(url)
            }
        }
    }

    suspend fun apiInfoFromUrl(url: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val model = NetworkService.API.getApiModel(url)
            database.apiModelDao.apply {
                setApiModel(model.asDomainApiModel())
            }
            _loadingStatus.postValue(true)
            fetchServices(model)
            true
        } catch (exception: Exception) {
            false
        }
    }

    private fun fetchServices(apiModel: NetworkApiModel) {
        for (service: Services in apiModel.services) {
            database.serviceDao.apply {
                insertService(service.asDomainService(apiBase = apiModel.apiBase))
            }
        }
    }

    suspend fun fetchSingleResponse(service: Service): Boolean = withContext(Dispatchers.IO) {
        try {
            val single = NetworkService.API.getMeasurement(service.apiBase + service.endpoint)
            database.singleResponseDao.setSingle(
                single.asDomainSingle(
                    service.apiBase,
                    service.endpoint
                )
            )
            true
        } catch (exception: Exception) {
            false
        }
    }

    suspend fun getSingleResponseByEndpoint(service: Service): SingleResponse? {
        return withContext(Dispatchers.IO) {
            database.singleResponseDao.getByApiBaseAndEndpoint(service.apiBase, service.endpoint)
        }
    }

    suspend fun fetchFlagResponse(service: Service): Boolean = withContext(Dispatchers.IO) {
        try {
            val flag = NetworkService.API.getFlag(service.apiBase + service.endpoint)
            database.flagResponseDao.setFlag(flag.asDomainFlag(service.apiBase, service.endpoint))
            true
        } catch (exception: Exception) {
            false
        }
    }

    suspend fun getFlagResponseByEndpoint(service: Service): FlagResponse? {
        return withContext(Dispatchers.IO) {
            database.flagResponseDao.getByApiBaseAndEndpoint(service.apiBase, service.endpoint)
        }
    }

    suspend fun fetchTimeseriesResponse(service: Service): Boolean = withContext(Dispatchers.IO) {
        try {
            val timeMonthAgo = Date.from(
                ZonedDateTime.now().minusMonths(2).toInstant()).time
            val result: List<Measurement> = NetworkService.API.getTimeseries(service.apiBase + service.endpoint, timeMonthAgo)
            val timeseries = result.map { it.asDomainMeasurement(service.apiBase) }
            database.timeseriesResponseDao.insertTimeseries(timeseries)
            true
        } catch (exception: Exception) {
            println(exception.message)
            false
        }
    }

    suspend fun getTimeseriesByApiBase(service: Service): List<DomainMeasure>? {
        return withContext(Dispatchers.IO) {
            database.timeseriesResponseDao.getByApiBaseLimited(service.apiBase, MEASUREMENT_LIMIT)
        }
    }

    suspend fun sendActionCommand(service: Service): Boolean = withContext(Dispatchers.IO) {
        try {
            NetworkService.API.sendActionCommand(service.apiBase + service.endpoint)
            true
        } catch (exeption: Exception) {
            false
        }
    }

//    suspend fun initializeCurrentMeasurementSource() {
//        withContext(Dispatchers.IO) {
//            currentMeasurements.addSource(
//                currentApiModel
//            ) {
//                currentMeasurements.value = currentApiModel.value?.let { apiModel ->
//                    database.timeseriesResponseDao.getByApiBaseLimited(
//                        apiModel.apiBase, MEASUREMENT_LIMIT
//                    )
//                }
//            }
//        }
//    }
//
//    suspend fun initializeCurrentServiceSource() {
//        currentServices.addSource(
//            currentApiModel
//        ) {
//            currentServices.value = currentApiModel.value?.let { apiModel ->
//                database.serviceDao.getGETServicesByApiBase(
//                    apiModel.apiBase, "Get one value")
//            }
//        }
//
//    }
}

