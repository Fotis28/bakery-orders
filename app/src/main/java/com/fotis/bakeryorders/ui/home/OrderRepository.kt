package com.fotis.bakeryorders.ui.home

import android.content.Context
import com.fotis.bakeryorders.ui.gallery.CakeData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Single place where a cake order is written to Firestore and to the local Room database.
 * The adapter decides what changed; the repository decides how it is stored.
 */
class OrderRepository(private val shop: String) {

    private val firestore = FirebaseFirestore.getInstance()

    private fun orderDocument(orderId: Int) =
        firestore.collection("CakeOrders")
            .document(shop)
            .collection("CakeOrders")
            .document(orderId.toString())

    /** Updates one field of an order, locally and in Firestore. */
    fun updateField(
        context: Context,
        order: CakeData,
        field: String,
        value: Any,
        updatedOrder: CakeData,
        onResult: (Boolean) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getDatabase(context).cakeDataDao().upsertCake(updatedOrder)
        }

        orderDocument(order.cakeorderid)
            .update(field, value)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    /** Deletes an order locally and in Firestore. */
    fun deleteOrder(context: Context, order: CakeData, onResult: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getDatabase(context).cakeDataDao().deleteCake(order)
        }

        orderDocument(order.cakeorderid)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    /** Marks an order as prepared. */
    fun markAsChecked(context: Context, order: CakeData, onResult: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getDatabase(context).cakeDataDao().upsertCake(order.copy(IsChecked = true))
        }

        orderDocument(order.cakeorderid)
            .update("IsChecked", true)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }
}