package com.osung.parabara.repository.entity

import com.osung.parabara.datasource.model.*

fun ResponseProductData.mapper(): EntityProduct {
    return EntityProduct(content, id, images, price, title)
}

fun BaseResponse<ResponseProductData>.mapper(): EntityProduct {
    return data.run { EntityProduct(content, id, images, price, title) }
}

fun ResponseGallery.mapper(): List<EntityGalleryImage> {
    return galleryData.map { EntityGalleryImage(it.id, it.fileName, it.imagePath) }
}