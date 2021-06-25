package com.osung.parabara.datasource.remote

import com.osung.parabara.datasource.ProductDataSource
import com.osung.parabara.datasource.model.BaseResponse
import com.osung.parabara.datasource.model.ResponseImageUpload
import com.osung.parabara.datasource.model.ResponseProductData
import com.osung.parabara.datasource.model.ResponseProductList
import io.reactivex.rxjava3.core.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class RemoteProductDataSource(
    private val api: Api
) : ProductDataSource {

    override fun requestProductPage(
        page: Int,
        size: Int
    ): Single<BaseResponse<ResponseProductList>> {
        return api.requestProductPage(page, size)
    }

    override fun requestUploadProductImage(
        path: String,
        fileName: String
    ): Single<BaseResponse<ResponseImageUpload>> {
        val imageFile = File(path)
        val requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile)
        val body = MultipartBody.Part.createFormData("image", fileName, requestFile)

        return api.requestUploadProductImage(body)
    }

    override fun requestInsertProduct(
        title: String,
        content: String,
        price: Long,
        images: List<Long>
    ): Single<BaseResponse<ResponseProductData>> {
        return api.requestInsertProduct(title, content, price, images)
    }

    override fun requestUpdateProduct(
        id: Long,
        title: String,
        content: String,
        price: Long
    ): Single<BaseResponse<ResponseProductData>> {
        return api.requestUpdateProduct(id, title, content, price)
    }

    override fun requestDeleteProduct(id: Long): Single<BaseResponse<Boolean>> {
        return api.requestDeleteProduct(id)
    }

}