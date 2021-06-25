package com.osung.parabara.datasource.remote

import com.osung.parabara.datasource.model.*
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface Api {
    @GET("/api/product")
    fun requestProductPage(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<BaseResponse<ResponseProductList>>

    @Multipart
    @POST("/api/product/upload")
    fun requestUploadProductImage(@Part image: MultipartBody.Part): Single<BaseResponse<ResponseImageUpload>>

    @FormUrlEncoded
    @POST("/api/product")
    fun requestInsertProduct(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("price") price: Long,
        @Field("images") images: List<Long>
    ): Single<BaseResponse<ResponseProductData>>

    @FormUrlEncoded
    @PUT("/api/product")
    fun requestUpdateProduct(
        @Field("id") id: Long,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("price") price: Long
    ): Single<BaseResponse<ResponseProductData>>

    @DELETE("/api/product/{id}")
    fun requestDeleteProduct(
        @Path("id") id: Long
    ): Single<BaseResponse<Boolean>>

    companion object {
        const val baseUrl = "https://api.recruit-test.parabara.kr"
    }
}