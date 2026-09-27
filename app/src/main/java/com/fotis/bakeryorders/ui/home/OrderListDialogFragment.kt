package com.fotis.bakeryorders.ui.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fotis.bakeryorders.R
import com.fotis.bakeryorders.ui.gallery.CakeData

class OrderListDialogFragment1() : DialogFragment() {


   private var orders: List<CakeData> = emptyList()
    private var shopName: String? = null
   private  lateinit var layoutManager :LinearLayoutManager
    fun setOrders(orderList: List<CakeData>, shopName: String?) {
        this.orders = orderList
        this.shopName = shopName
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_orders_recyclerview1, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.ordersRecyclerView)
        val btn_close = view.findViewById<ImageButton>(R.id.btnClose)

        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        val ordersViewModel= ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        val orders = ordersViewModel.orders

        // Στο Activity/Fragment
        recyclerView.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
                setDrawable(context?.let { ContextCompat.getDrawable(it, android.R.color.transparent) }!!)
            }
        )


        val shop = ordersViewModel.selectedShopName.value
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = OrderAdapter(orders, shop.toString())


        btn_close.setOnClickListener {
            dismiss()
        }

        dialog.setContentView(view)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return dialog
    }

}