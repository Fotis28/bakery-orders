package com.fotis.bakeryorders.ui.Product_Item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fotis.bakeryorders.ui.Product_Item.Data.Products.ProductRepository

class ProductItemViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductItemViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}