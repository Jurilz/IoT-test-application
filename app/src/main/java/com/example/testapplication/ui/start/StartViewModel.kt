package com.example.testapplication.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapplication.repository.QrRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartViewModel(val qrRepository: QrRepository): ViewModel() {

//    var apiModel = qrRepository.apiModel

    val apiModel = qrRepository.currentApiModel

    val services = qrRepository.currentServices

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