package com.example.testapplication.ui.start

import androidx.lifecycle.*
import com.example.testapplication.repository.QrRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartViewModel(val qrRepository: QrRepository): ViewModel() {

    val apiModel = qrRepository.apiModel.asLiveData()

    val services = qrRepository.currentServices.asLiveData()

    val loadingStatus = qrRepository.loadingStatus

    fun getApiInformation() {
        viewModelScope.launch {
            if (qrRepository.getUrl() != null) {
                withContext(Dispatchers.IO) {
                    qrRepository.apiInfoFromUrl(qrRepository.getUrl()!!.value)
                }
            }
        }
    }
}