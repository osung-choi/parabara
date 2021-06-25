package com.osung.parabara.datasource

import com.osung.parabara.datasource.model.BaseResponse
import com.osung.parabara.datasource.model.ResponseImageUpload
import com.osung.parabara.datasource.model.ResponseProductData
import com.osung.parabara.datasource.model.ResponseProductList
import io.reactivex.rxjava3.core.Single

interface ProductDataSource {
    /**
     * 상품 페이지 요청
     *
     * @param page 페이지 번호
     * @param size 페이지 사이즈
     * @return
     */
    fun requestProductPage(page: Int, size: Int): Single<BaseResponse<ResponseProductList>>

    /**
     * 상품 이미지 업로드
     *
     * @param path 이미지 경로
     * @param fileName 이미지 파일 이름
     * @return
     */
    fun requestUploadProductImage(path: String, fileName: String): Single<BaseResponse<ResponseImageUpload>>

    /**
     * 상품 등록 요청
     *
     * @param title
     * @param content
     * @param price
     * @param images
     * @return
     */
    fun requestInsertProduct(title: String, content: String, price: Long, images: List<Long>): Single<BaseResponse<ResponseProductData>>

    /**
     * 상품 수정 요청
     *
     * @param id
     * @param title
     * @param content
     * @param price
     * @return
     */
    fun requestUpdateProduct(id: Long, title: String, content: String, price: Long): Single<BaseResponse<ResponseProductData>>

    /**
     * 상품 삭제 요청
     *
     * @param id
     * @return
     */
    fun requestDeleteProduct(id: Long): Single<BaseResponse<Boolean>>
}