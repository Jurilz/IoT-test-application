package com.example.testapplication.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapplication.domain.Url
import com.example.testapplication.database.Database
import com.example.testapplication.database.getDatabase
import com.example.testapplication.domain.FlagResponse
import com.example.testapplication.domain.Service
import com.example.testapplication.domain.SingleResponse
import com.example.testapplication.network.*
import com.example.testapplication.utility.asDomainFlag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class QrRepository(private val database: Database) {

    constructor(context: Context) : this(getDatabase(context))



    private val _loadingStatus = MutableLiveData<Boolean>(false)
    val loadingStatus: LiveData<Boolean> = _loadingStatus

    val currentApiModel by lazy {
        database.apiModelDao.getLatestApiModel()
    }

    val currentServices: MediatorLiveData<LiveData<List<Service>>> = MediatorLiveData()

    init {
        currentServices.addSource(
            currentApiModel
        ) {
            run {
                currentServices.value = database.serviceDao.getGETServicesByApiBase(it.apiBase, "Get one value")
            }
        }
    }

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
                    getUrl()!!.value,
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

}

