package com.example.testapplication.utility

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified VM : ViewModel> Fragment.viewModelFactories(
    crossinline provider: () -> VM
): Lazy<VM> {
    val factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return provider() as T
            }
        }
    }
    return viewModels(factoryProducer = factoryProducer)
}
