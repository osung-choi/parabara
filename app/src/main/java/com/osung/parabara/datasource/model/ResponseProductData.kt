package com.osung.parabara.datasource.model

import com.google.gson.annotations.SerializedName

data class ResponseProductData(
    @SerializedName("content")
    val content: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("images")
    val images: List<String>,
    @SerializedName("price")
    val price: Long,
    @SerializedName("title")
    val title: String
)