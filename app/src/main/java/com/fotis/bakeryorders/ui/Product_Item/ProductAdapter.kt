package com.fotis.bakeryorders.ui.Product_Item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fotis.bakeryorders.R
import com.fotis.bakeryorders.databinding.ItemProductBinding
import com.fotis.bakeryorders.ui.Product_Item.Data.Products.ProductType

private lateinit var  color_box1: View
class ProductAdapter(
    private val onDeleteClick: (ProductType) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var products: MutableList<ProductType> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size

    // Method to update products list
    fun updateProducts(newProducts: List<ProductType>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductType) {
            when (product) {
                is ProductType.Cup -> {
                    binding.tvProductName.text = product.cupData.name
                    binding.etQuantity.setText(product.cupData.quantity.toString())
                    binding.quantityButtons.visibility = if (product.product.isExpanded) View.VISIBLE else View.GONE
                }
                is ProductType.Box -> {
                    binding.tvProductName.text = product.boxData.name
                    binding.etQuantity.setText(product.boxData.quantity.toString())
                    binding.quantityButtons.visibility = if (product.product.isExpanded) View.VISIBLE else View.GONE
                }
                is ProductType.Bag -> {
                    binding.tvProductName.text = product.bagData.name
                    binding.etQuantity.setText(product.bagData.quantity.toString())
                    binding.quantityButtons.visibility = if (product.product.isExpanded) View.VISIBLE else View.GONE
                }
            }

            // Set expand/collapse state
            binding.btnExpand.setOnClickListener {
                when (product) {
                    is ProductType.Cup -> {
                        product.product.isExpanded = !product.product.isExpanded
                    }
                    is ProductType.Box -> {
                        product.product.isExpanded = !product.product.isExpanded
                    }
                    is ProductType.Bag -> {
                        product.product.isExpanded = !product.product.isExpanded
                    }
                }

                // Toggle visibility of the quantity buttons
                binding.quantityButtons.visibility = if (product.product.isExpanded) View.VISIBLE else View.GONE
                binding.btnExpand.setImageResource(
                    if (product.product.isExpanded) R.drawable.baseline_check_24 else R.drawable.sharp_add_24
                )
            }

            // Handle delete button
            binding.btnDelete.setOnClickListener {
                onDeleteClick(product)
            }
        }
    }
}