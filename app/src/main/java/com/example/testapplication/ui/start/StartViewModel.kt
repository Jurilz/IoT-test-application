package com.example.testapplication.ui.start

import androidx.lifecycle.*
import com.example.testapplication.domain.ApiModel
import com.example.testapplication.domain.Service
import com.example.testapplication.repository.QrRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartViewModel(val qrRepository: QrRepository): ViewModel() {

    val apiModel = qrRepository.apiModel.asLiveData()

//    val apiModel = qrRepository.currentApiModel

//    val services = qrRepository.currentServices

    val services = qrRepository.currentServices.asLiveData().value

    val loadingStatus = qrRepository.loadingStatus

//    init {
//        initializeQrDataSources()
//    }

    fun getApiInformation() {
        viewModelScope.launch {
            if (qrRepository.getUrl() != null) {
                withContext(Dispatchers.IO) {
                    qrRepository.apiInfoFromUrl(qrRepository.getUrl()!!.value)
                }
            }
        }
    }

//    fun initializeQrDataSources() {
//        viewModelScope.launch {
//            qrRepository.initializeCurrentServiceSource()
//            qrRepository.initializeCurrentMeasurementSource()
//        }
//    }
}