package com.example.testapplication.ui.qr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapplication.domain.Url
import com.example.testapplication.repository.QrRepository
import kotlinx.coroutines.launch

class QrViewModel(private val qrRepository: QrRepository) : ViewModel() {

    fun setUrl(url: String) {
        viewModelScope.launch {
            qrRepository.setUrl(Url(url))
        }
    }
}
