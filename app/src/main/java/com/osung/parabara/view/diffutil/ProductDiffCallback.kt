package com.osung.parabara.view.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.osung.parabara.repository.entity.EntityProduct

class ProductDiffCallback: DiffUtil.ItemCallback<EntityProduct>() {
    override fun areItemsTheSame(oldItem: EntityProduct, newItem: EntityProduct) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: EntityProduct, newItem: EntityProduct) = oldItem == newItem
}