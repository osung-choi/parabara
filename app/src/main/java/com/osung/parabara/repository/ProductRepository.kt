package com.osung.parabara.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.osung.parabara.repository.entity.EntityGalleryImage
import com.osung.parabara.repository.entity.EntityProduct
import io.reactivex.rxjava3.core.Single

interface ProductRepository {
    /**
     * 상품 리스트 요청 - Paging3
     *
     * @param size 페이지 사이즈
     * @return
     */
    fun getProductListPager(size: Int): LiveData<PagingData<EntityProduct>>

    /**
     * 상품 추가
     * 이미지 전송 완료 후 상품 추가 요청
     *
     * @param title : 상품 제목
     * @param content : 상품 내용
     * @param price : 상품 가격
     * @param images : 상품 이미지 (선택 : list size 0)
     * @return
     */
    fun insertProduct(title: String, content: String, price: Long, images: List<EntityGalleryImage>): Single<EntityProduct>

    /**
     * 상품 수정
     *
     * @param id : 상품 ID
     * @param title : 상품 제목
     * @param content : 상품 내용
     * @param price : 상품 가격
     * @return
     */
    fun updateProduct(id: Long, title: String, content: String, price: Long): Single<EntityProduct>

    /**
     * 상품 삭제
     *
     * @param id : 상품 ID
     * @return
     */
    fun deleteProduct(id: Long): Single<Boolean>
}