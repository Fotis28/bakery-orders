package com.fotis.bakeryorders.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CakeOrderViewModelFactory(private val repository: CakeOrderRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CakeOrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CakeOrderViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}