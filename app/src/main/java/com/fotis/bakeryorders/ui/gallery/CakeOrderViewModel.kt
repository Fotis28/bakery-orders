package com.fotis.bakeryorders.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class CakeOrderViewModel(private val repository: CakeOrderRepository) : ViewModel() {

    // gia to info
    private val _results = MutableLiveData<List<CakeData>>()
    val results: LiveData<List<CakeData>> = _results



    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text

    fun insertCakeOrder(cakeOrder: CakeData) {
        viewModelScope.launch {
            repository.insertCakeOrder(cakeOrder)
        }

    }

    fun updateCakeOrder(cakeOrder: CakeData) {
        viewModelScope.launch {
            repository.updateCakeOrder(cakeOrder)
        }
    }

    fun deleteCakeOrder(cakeOrder: CakeData) {
        viewModelScope.launch {
            repository.deleteCakeOrder(cakeOrder)
        }

    }


    fun insertCakeOrderBack(cakeOrder: CakeData, callback: (Long) -> Unit){
        viewModelScope.launch {
            val id = repository.insertCakeOrderBack(cakeOrder)
            callback(id)
        }
    }

    fun getAllCakeOrders(): LiveData<List<CakeData>> {
        return repository.getAllCakeOrders().asLiveData()
    }

   fun deleteAllCakeOrder(){
       viewModelScope.launch {
           repository.deleteAllCakeOrder()
       }

   }

    fun search(query: String, type: SearchType) {
        viewModelScope.launch {
            val result = when (type) {
                SearchType.BY_NAME -> repository.getOrdersByClientName(query)
                SearchType.BY_DAY -> repository.getOrdersByDay(query)
                SearchType.BY_TYPE -> repository.getOrdersByType(query)
                SearchType.BY_TIME -> {
                    val startTime = query.trim()
                    val endTime = "22:00"  // fixed end time
                    repository.getOrdersByTime(startTime, endTime)
                }
                SearchType.CHECKED_ONLY -> repository.getCheckedOrders()
                SearchType.BY_PHONE -> repository.getOrdersByPhone(query)
            }
            _results.postValue(result)
        }
    }

}

enum class SearchType {
    BY_NAME,
    BY_DAY,
    BY_TYPE,
    BY_TIME,
    CHECKED_ONLY,
    BY_PHONE
}

