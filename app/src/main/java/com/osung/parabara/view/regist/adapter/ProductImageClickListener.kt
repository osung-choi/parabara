package com.osung.parabara.view.regist.adapter

import com.osung.parabara.repository.entity.EntityGalleryImage

interface ProductImageClickListener {
    fun onProductImageRemove(image: EntityGalleryImage)
}