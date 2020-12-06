package com.example.testapplication.ui.start

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.testapplication.domain.Url
import com.example.testapplication.network.ApiModel
import com.example.testapplication.repository.QrRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartViewModel(application: Application): ViewModel() {

    private val qrRepository = QrRepository(application)

    var apiModel = qrRepository.apiModel

    val serviceResponses = qrRepository.serviceResponses

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

    class Factory(
        private val application: Application
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StartViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return StartViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}