package com.osung.parabara.datasource

import com.osung.parabara.datasource.model.ResponseGallery
import io.reactivex.rxjava3.core.Single

interface GalleryDataSource {
    /**
     * 갤러리 이미지 요청
     *
     * @return
     */
    fun requestGalleryImageList(): Single<ResponseGallery>
}