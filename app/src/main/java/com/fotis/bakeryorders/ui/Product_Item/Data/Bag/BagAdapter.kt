package com.fotis.bakeryorders.ui.Product_Item.Data.Bag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fotis.bakeryorders.R

class BagAdapter(
    private val onDeleteClick: (BagData) -> Unit,
    private val onQuantityChange: (BagData) -> Unit
) : RecyclerView.Adapter<BagAdapter.BagViewHolder>() {

    private var bags: MutableList<BagData> = mutableListOf()
    private var expandedPosition = RecyclerView.NO_POSITION

    fun setData(newBags: List<BagData>) {
        bags = newBags.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return BagViewHolder(view)
    }

    override fun onBindViewHolder(holder: BagViewHolder, position: Int) {
        holder.bind(bags[position])
    }

    override fun getItemCount(): Int = bags.size

    fun getRedBags(): List<BagData> {
        return bags.filter { it.quantity==0 }
    }

    fun getYellowBags(): List<BagData> {
        return bags.filter { it.quantity ==1 }

    }
    fun updateQuantity(newQuantity: Int, editText: EditText, bag: BagData){

    }

    inner class BagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(bag: BagData) {
            val nameText = itemView.findViewById<TextView>(R.id.tv_product_name)
            val quantityEdit = itemView.findViewById<EditText>(R.id.et_quantity)
            val colorBox = itemView.findViewById<View>(R.id.color_box)
            val btnExpand = itemView.findViewById<ImageButton>(R.id.btn_expand)
            val btnDelete = itemView.findViewById<ImageButton>(R.id.btn_delete)
            val btnMinus = itemView.findViewById<ImageButton>(R.id.btn_minus)
            val btnPlus = itemView.findViewById<ImageButton>(R.id.btn_plus)
            val quantityButtons = itemView.findViewById<LinearLayout>(R.id.quantity_buttons)

            nameText.text = bag.name
            quantityEdit.setText(bag.quantity.toString())
            setColorBasedOnQuantity(bag.quantity, colorBox)

            val isExpanded = adapterPosition == expandedPosition
            quantityButtons.visibility = if (isExpanded) View.VISIBLE else View.GONE
            btnDelete.visibility = if (isExpanded) View.GONE else View.VISIBLE
            btnExpand.setImageResource(
                if (isExpanded) R.drawable.baseline_check_24
                else R.drawable.sharp_add_24
            )


            // +/- buttons
            btnMinus.setOnClickListener {
                val current = quantityEdit.text.toString().toIntOrNull() ?: 1
                val newQuantity = (current - 1).coerceAtLeast(0)
                updateQuantity(newQuantity, quantityEdit, bag)
            }

            btnPlus.setOnClickListener {
                val current = quantityEdit.text.toString().toIntOrNull() ?: 0
                val newQuantity = current + 1
                updateQuantity(newQuantity, quantityEdit, bag)
            }

            btnExpand.setOnClickListener {
                val previousExpandedPosition = expandedPosition
                if (isExpanded) {
                    expandedPosition = RecyclerView.NO_POSITION
                } else {
                    expandedPosition = adapterPosition
                }
                notifyItemChanged(previousExpandedPosition)
                notifyItemChanged(adapterPosition)
            }
            // delete
            btnDelete.setOnClickListener {
                onDeleteClick(bag)
            }
        }


        private  fun updateQuantity(newQuantity: Int,  bag: BagData) {

            //updateQuantity(Int,)
        }

        private fun updateQuantity(newQuantity: Int, editText: EditText, bag: BagData) {
            editText.setText(newQuantity.toString())
            val updatedBag = bag.copy(quantity = newQuantity)
            bags[adapterPosition] = updatedBag
            onQuantityChange(updatedBag)
        }

        private fun setColorBasedOnQuantity(quantity: Int, colorBox: View) {
            when {
                quantity == 0 -> colorBox.setBackgroundColor(android.graphics.Color.RED)
                quantity < 2 -> colorBox.setBackgroundColor(android.graphics.Color.YELLOW)
                else -> colorBox.setBackgroundColor(android.graphics.Color.GREEN)
            }
        }
    }




    fun updateLocalQuantity(updatedBag: BagData) {
        val index = bags.indexOfFirst { it.id == updatedBag.id }
        if (index != -1) {
            bags[index] = updatedBag
            notifyItemChanged(index)
            onQuantityChange(updatedBag)
        }
    }


}
