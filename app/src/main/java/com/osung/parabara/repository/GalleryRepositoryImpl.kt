package com.osung.parabara.repository

import com.osung.parabara.datasource.GalleryDataSource
import com.osung.parabara.repository.entity.EntityGalleryImage
import com.osung.parabara.repository.entity.mapper
import io.reactivex.rxjava3.core.Single

class GalleryRepositoryImpl(
    private val provider: GalleryDataSource,
): GalleryRepository {
    override fun getGalleryImageList(): Single<List<EntityGalleryImage>> {
        return provider.requestGalleryImageList().map { it.mapper() }
    }
}