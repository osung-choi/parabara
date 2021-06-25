package com.osung.parabara.view.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.osung.parabara.databinding.ItemProductBinding
import com.osung.parabara.repository.entity.EntityProduct
import com.osung.parabara.view.diffutil.ProductDiffCallback

class ProductListAdapter(
    private val listener: ProductClickListener
): PagingDataAdapter<EntityProduct, ProductListAdapter.ProductViewHolder>(
    ProductDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(ItemProductBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        itemCount
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(productItem: EntityProduct) {
            binding.product = productItem

            binding.root.setOnClickListener {
                listener.onProductItemClick(productItem)
            }

            binding.productRemove.setOnClickListener {
                listener.onProductRemoveClick(productItem)
            }
        }
    }
}