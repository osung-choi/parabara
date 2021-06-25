package com.osung.parabara.repository

import com.osung.parabara.repository.entity.EntityGalleryImage
import io.reactivex.rxjava3.core.Single

interface GalleryRepository {
    /**
     * 갤러리에서 이미지를 얻어온다
     *
     * @return
     */
    fun getGalleryImageList(): Single<List<EntityGalleryImage>>
}