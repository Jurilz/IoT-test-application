package com.example.testapplication.ui.qr

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.testapplication.domain.Url
import com.example.testapplication.repository.QrRepository
import kotlinx.coroutines.launch
import java.util.prefs.Preferences

class QrViewModel(application: Application): ViewModel() {

    val qrRepository = QrRepository(application)

    fun setUrl(url: String) {
        viewModelScope.launch {
            qrRepository.setUrl(Url(url))
        }
    }

    class Factory(
        private val application: Application
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(QrViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return QrViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}