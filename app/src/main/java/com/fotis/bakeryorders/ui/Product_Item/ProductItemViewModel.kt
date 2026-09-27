package com.fotis.bakeryorders.ui.Product_Item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fotis.bakeryorders.ui.Product_Item.Data.Bag.BagData
import com.fotis.bakeryorders.ui.Product_Item.Data.Box.BoxData
import com.fotis.bakeryorders.ui.Product_Item.Data.Cup.CupData
import com.fotis.bakeryorders.ui.Product_Item.Data.Products.Product
import com.fotis.bakeryorders.ui.Product_Item.Data.Products.ProductRepository
import kotlinx.coroutines.launch

class ProductItemViewModel(private val repository: ProductRepository) : ViewModel() {

    val allProducts: LiveData<List<Product>> = repository.allProducts.asLiveData()

    val cupItems: LiveData<List<CupData>> = repository.getCupsWithProduct("CUP")
    val boxItems: LiveData<List<BoxData>> = repository.getBoxesWithProduct("BOX")
    val bagItems: LiveData<List<BagData>> = repository.getBagsWithProduct("BAG")






    private val _isProductListEmpty = MutableLiveData<Boolean>()
    val isProductListEmpty: LiveData<Boolean> = _isProductListEmpty






    fun updateBox(box: BoxData) {
        viewModelScope.launch {
            repository.updateBoxWithProduct(box)
        }
    }

    fun updateCup(cup: CupData) {
        viewModelScope.launch {
            repository.updateCupWithProduct(cup)
        }
    }

    fun updateBag(bag: BagData) {
        viewModelScope.launch {
            repository.updateBagWithProduct(bag)
        }
    }

    fun insertBox(box: BoxData) {
        viewModelScope.launch {
            repository.insertBoxWithProduct(box)
        }
    }

    fun insertCup(cup: CupData) {
        viewModelScope.launch {
            repository.insertCupWithProduct(cup)
        }
    }

    fun insertBag(bag: BagData) {
        viewModelScope.launch {
            repository.insertBagWithProduct(bag)
        }

    }

    fun deleteBox(box: BoxData) {
        viewModelScope.launch {
            repository.deleteBoxWithProduct(box)
        }

    }

    fun deleteBag(bag: BagData) {
        viewModelScope.launch {
            repository.deleteBagWithProduct(bag)
        }
    }

    fun deleteCup(cup: CupData) {
        viewModelScope.launch {
            repository.deleteCupWithProduct(cup)
        }

    }

    fun createProduct(product: Product){
        viewModelScope.launch {
            repository.insert(product)
        }

    }


    fun deleteProduct(product: Product){
        viewModelScope.launch {
            repository.delete(product)
        }
    }

    fun updateProduct(product: Product){
        viewModelScope.launch {
            repository.update(product)
        }
    }

    fun insertBoxWithProductAndReturn(box: BoxData, callback: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.insertBoxWithProductAndReturn(box)
            callback(id)
        }
    }

    fun insertCupWithProductAndReturn(cup: CupData, callback: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.insertCupWithProductAndReturn(cup)
            callback(id)
        }

    }

    fun insertBagWithProductAndReturn(bag: BagData, callback: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.insertBagWithProductAndReturn(bag)
            callback(id)
        }
    }

    // Συνάρτηση για να φορτώσεις τα προϊόντα


  suspend  fun getcountProducts(): Int{
          var  count = repository.countProducts()

        return count
    }
    fun increaseProductQuantity(productName: String) {
        viewModelScope.launch {
            repository.increaseQuantity(productName)
        }
    }

    fun decreaseProductQuantity(productName: String) {
        viewModelScope.launch {
            repository.decreaseQuantity(productName)
        }
    }

}