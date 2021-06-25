package com.osung.parabara.datasource.model
import com.google.gson.annotations.SerializedName


data class ResponseProductList(
    @SerializedName("page")
    val page: Int,
    @SerializedName("records")
    val records: Int,
    @SerializedName("rows")
    val productList: List<ResponseProductData>,
    @SerializedName("total")
    val total: Int
)

