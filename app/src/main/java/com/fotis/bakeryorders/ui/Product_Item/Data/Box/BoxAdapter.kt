package com.fotis.bakeryorders.ui.Product_Item.Data.Box

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fotis.bakeryorders.R

class BoxAdapter(
    private val onDeleteClick: (BoxData) -> Unit,
    private val onQuantityChange: (BoxData) -> Unit
) : RecyclerView.Adapter<BoxAdapter.BoxViewHolder>() {

    private var boxes: MutableList<BoxData> = mutableListOf()

    private var expandedPosition = RecyclerView.NO_POSITION
    private val expandedStateMap = mutableMapOf<Int, Boolean>()

    fun setData(newBoxes: List<BoxData>) {
        boxes = newBoxes.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoxViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return BoxViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoxViewHolder, position: Int) {
        holder.bind(boxes[position])

        /*val isExpanded = expandedStateMap[position] ?: false
        val quantityButtons = holder.itemView.findViewById<LinearLayout>(R.id.quantity_buttons)
        quantityButtons.visibility = if (isExpanded) View.VISIBLE else View.GONE
         */
    }
    fun getRedBoxs(): List<BoxData> {
        return boxes.filter { it.quantity==0 }
    }

    fun getYellowBoxs(): List<BoxData> {
        return boxes.filter { it.quantity==1 }
    }


    override fun getItemCount(): Int = boxes.size

    inner class BoxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(box: BoxData) {
            val nameText = itemView.findViewById<TextView>(R.id.tv_product_name)
            val quantityEdit = itemView.findViewById<EditText>(R.id.et_quantity)
            val colorBox = itemView.findViewById<View>(R.id.color_box)
            val btnExpand = itemView.findViewById<ImageButton>(R.id.btn_expand)
            val btnDelete = itemView.findViewById<ImageButton>(R.id.btn_delete)
            val btnMinus = itemView.findViewById<ImageButton>(R.id.btn_minus)
            val btnPlus = itemView.findViewById<ImageButton>(R.id.btn_plus)
            val quantityButtons = itemView.findViewById<LinearLayout>(R.id.quantity_buttons)

            nameText.text = box.name
            quantityEdit.setText(box.quantity.toString())
            setColorBasedOnQuantity(box.quantity, colorBox)


            val isExpanded = adapterPosition == expandedPosition
            quantityButtons.visibility = if (isExpanded) View.VISIBLE else View.GONE
            btnDelete.visibility = if (isExpanded) View.GONE else View.VISIBLE
            btnExpand.setImageResource(
                if (isExpanded) R.drawable.baseline_check_24
                else R.drawable.sharp_add_24
            )


            /*
            // Restore visibility from the state
            quantityButtons.visibility = if (box.isExpanded) View.VISIBLE else View.GONE
            btnDelete.visibility = if (box.isExpanded) View.GONE else View.VISIBLE
            btnExpand.setImageResource(
                if (box.isExpanded) R.drawable.baseline_check_24
                else R.drawable.sharp_add_24
            )
            */



            // +/- buttons
            btnMinus.setOnClickListener {
                val current = quantityEdit.text.toString().toIntOrNull() ?: 1
                val newQuantity = (current - 1).coerceAtLeast(0)
                updateQuantity(newQuantity, quantityEdit, box)
            }

            btnPlus.setOnClickListener {
                val current = quantityEdit.text.toString().toIntOrNull() ?: 0
                val newQuantity = current + 1
                updateQuantity(newQuantity, quantityEdit, box)
            }

            // expand toggle
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
                onDeleteClick(box)

            }
        }

        private fun updateQuantity(newQuantity: Int, editText: EditText, box: BoxData) {
            editText.setText(newQuantity.toString())
            val updatedBox = box.copy(quantity = newQuantity)
            boxes[adapterPosition] = updatedBox
            onQuantityChange(updatedBox)
        }

        private fun setColorBasedOnQuantity(quantity: Int, colorBox: View) {
            when {
                quantity == 0 -> colorBox.setBackgroundColor(android.graphics.Color.RED)
                quantity < 2 -> colorBox.setBackgroundColor(android.graphics.Color.YELLOW)
                else -> colorBox.setBackgroundColor(android.graphics.Color.GREEN)
            }
        }
    }

    fun updateLocalQuantity(updatedBox: BoxData) {
        val index = boxes.indexOfFirst { it.id == updatedBox.id }
        if (index != -1) {
            boxes[index] = updatedBox
            notifyItemChanged(index)
            onQuantityChange(updatedBox)
        }
    }
}
