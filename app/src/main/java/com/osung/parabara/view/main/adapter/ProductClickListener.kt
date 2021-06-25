package com.osung.parabara.view.main.adapter

import com.osung.parabara.repository.entity.EntityProduct

interface ProductClickListener {
    fun onProductItemClick(productItem: EntityProduct)
    fun onProductRemoveClick(productItem: EntityProduct)
}