package com.fotis.bakeryorders.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fotis.bakeryorders.R
import com.fotis.bakeryorders.ui.gallery.CakeData

class OrderAdapterDetails (
    private val order: List<CakeData> // ή δώσε δικό σου data class αν έχεις
) : RecyclerView.Adapter<OrderAdapterDetails.OrderAdapterDetailsViewHolder>() {

    inner class OrderAdapterDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.tv_product_name)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAdapterDetailsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.for_every_information_order_have, parent, false) // το XML που έστειλες
        return OrderAdapterDetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderAdapterDetailsViewHolder, position: Int) {
        val order = order[position]
        holder.productName.text = order.NameAndLastName
        holder.productName.text = order.Day

        holder.btnDelete.setOnClickListener {
            //Toast.makeText(holder.itemView.context, "Διαγραφή προϊόντος: $order", Toast.LENGTH_SHORT).show()
            // Διαγραφή προϊόντος, αν χρειάζεται
        }
    }

    override fun getItemCount() = order.size
}