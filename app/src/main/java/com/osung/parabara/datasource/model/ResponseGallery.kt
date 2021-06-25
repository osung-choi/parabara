package com.osung.parabara.datasource.model

data class ResponseGallery(
    val galleryData: List<GalleryData>
)

data class GalleryData(
    val id: Long,
    val fileName: String,
    val imagePath: String
)