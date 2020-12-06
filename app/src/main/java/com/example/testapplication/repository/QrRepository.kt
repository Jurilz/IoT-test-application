package com.example.testapplication.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapplication.domain.Url
import com.example.testapplication.database.Database
import com.example.testapplication.database.getDatabase
import com.example.testapplication.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class QrRepository(private val database: Database) {

    constructor(context: Context): this(getDatabase(context))

    private val _loadingStatus = MutableLiveData<Boolean>(false)
    val loadingStatus: LiveData<Boolean> = _loadingStatus

    private val _apiModel = MutableLiveData<ApiModel>()
    val apiModel: LiveData<ApiModel> = _apiModel

    private val _serviceResponses = MutableLiveData<MutableList<ServiceResponse>>()
    val serviceResponses: LiveData<MutableList<ServiceResponse>> = _serviceResponses

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
            val model = NetworkService.API.getApiModel(url);

            _apiModel.postValue(model)
            storeResponses()
            true
        } catch (exception: Exception) {
            false
        }
    }

    suspend fun storeResponses(): Boolean = withContext(Dispatchers.IO) {
        val servicesAndResponse = mutableListOf<ServiceResponse>()
        try {
            if (apiModel.value != null) {
                apiModel.value!!.services.map {
                    services -> getResponse(services, servicesAndResponse)
                }
            }
            _serviceResponses.postValue(servicesAndResponse)
            _loadingStatus.postValue(true)
            true
        } catch (exception: Exception) {
            false
        }
    }

    private suspend fun getResponse(services: Services, servicesAndResponse: MutableList<ServiceResponse>) {
        when (services.kind) {
            ServiceKind.single.toString() -> setSingleResponse(services, servicesAndResponse)
            ServiceKind.flag.toString() -> setFlagResponse(services, servicesAndResponse)
            ServiceKind.timeseries.toString() -> setTimeseriesResponse(services, servicesAndResponse)
        }
    }

    private suspend fun setTimeseriesResponse(
        services: Services,
        servicesAndResponse: MutableList<ServiceResponse>
    ) {
        if (apiModel.value == null) return
        val timeseries: List<Measurement> = NetworkService.API.getTimeseries(apiModel.value!!.apiBase + services.endpoint)
        val serviceResponse = ServiceResponse(services, single = null, timeseries = timeseries, status = null)
        servicesAndResponse.add(serviceResponse)
    }

    private suspend fun setFlagResponse(
        services: Services,
        servicesAndResponse: MutableList<ServiceResponse>
    ) {
        if (apiModel.value == null) return
        val flag: Boolean = NetworkService.API.getFlag(apiModel.value!!.apiBase + services.endpoint)
        val serviceResponse = ServiceResponse(services, single = null, timeseries = null, status = flag)
        servicesAndResponse.add(serviceResponse)
    }

    private suspend fun setSingleResponse(services: Services, servicesAndResponse: MutableList<ServiceResponse>) {
        if (apiModel.value == null) return
        val singleResponse = NetworkService.API.getMeasurement(apiModel.value!!.apiBase + services.endpoint)
        val serviceResponse = ServiceResponse(services, single = singleResponse, timeseries = null, status = null)
        servicesAndResponse.add(serviceResponse)
    }


}

