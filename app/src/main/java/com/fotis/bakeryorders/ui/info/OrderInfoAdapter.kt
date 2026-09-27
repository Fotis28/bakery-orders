package com.fotis.bakeryorders.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fotis.bakeryorders.R
import com.fotis.bakeryorders.ui.gallery.CakeData

class OrderInfoAdapter(
    private var orders: List<CakeData>,
    private val shopName: String
) : RecyclerView.Adapter<OrderInfoAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val paraggeliaN: TextView = view.findViewById(R.id.ParaggeliaN)
        val productName: TextView = view.findViewById(R.id.tv_product_name)
        val productTilefono: TextView = view.findViewById(R.id.tv_product_Tilefono)
        val productEidos: TextView = view.findViewById(R.id.tv_product_Eidos)
        val productDay: TextView = view.findViewById(R.id.tv_product_Day)
        val productGeusi: TextView = view.findViewById(R.id.tv_product_Geusi)
        val productSxima: TextView = view.findViewById(R.id.tv_product_Sxima)
        val productSxolia: TextView = view.findViewById(R.id.tv_product_Sxolia)
        val productTime: TextView = view.findViewById(R.id.tv_product_Time)
        val productVafla: TextView = view.findViewById(R.id.tv_product_Vafla)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.info_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        holder.paraggeliaN.text = "Παραγγελία από: $shopName"
        holder.productName.text = "Όνομα: ${order.NameAndLastName}"
        holder.productTilefono.text = "Τηλέφωνο: ${order.Tilefono}"
        holder.productEidos.text = "Είδος: ${order.Eidos}"
        holder.productDay.text = "Ημερομηνία: ${order.Day}"
        holder.productGeusi.text = "Γεύση: ${order.Geusi}"
        holder.productSxima.text = "Σχήμα: ${order.Sxima}"
        holder.productSxolia.text = "Σχόλια: ${order.Sxolia}"
        holder.productTime.text = "Ώρα: ${order.Time}"
        holder.productVafla.text = "Βάφλα: ${order.Vafla}"
    }

    override fun getItemCount(): Int = orders.size

    fun updateData(newOrders: List<CakeData>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}
