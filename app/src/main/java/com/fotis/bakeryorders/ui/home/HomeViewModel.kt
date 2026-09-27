package com.fotis.bakeryorders.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fotis.bakeryorders.ui.gallery.CakeData
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _ordersMap = MutableLiveData<Map<String, List<CakeData>>>()
    val ordersMap: LiveData<Map<String, List<CakeData>>> = _ordersMap

    fun fetchCakes(shopName: String? = null) {
        val db = FirebaseFirestore.getInstance()
        val ordersMap = mutableMapOf<String, MutableList<CakeData>>()

        val onResult: (Map<String, List<CakeData>>) -> Unit = {
            _ordersMap.value = it
        }


    }

    private var _orders: List<CakeData> = emptyList()
    val orders: List<CakeData> get() = _orders



    private var flagNotification =false
    private var ShopName: String=""

    private val _selectedShopName = MutableLiveData<String?>()
    val selectedShopName: LiveData<String?> get() = _selectedShopName

    fun setSelectedShopName(name: String?) {
        _selectedShopName.value = name
    }



    fun setShopName(name:String){
        ShopName= name
    }


    fun getShopName(): String {
        return ShopName
    }
    fun setFlagNotification(flag:Boolean){
    flagNotification=flag

    }

    fun getFlagNotification():Boolean{
        return flagNotification
    }

    fun getSelectedShopName(): String? {
        return _selectedShopName.value
    }


    fun setOrders(orderList: List<CakeData>) {
        _orders = orderList
    }
    private var hasInitializedRadioGroup = false
    fun setRadioGroupInitialized(value: Boolean) {
        hasInitializedRadioGroup = value
    }
    fun gethasInitialLizedRadioGroup ():Boolean{
        return hasInitializedRadioGroup
    }

}