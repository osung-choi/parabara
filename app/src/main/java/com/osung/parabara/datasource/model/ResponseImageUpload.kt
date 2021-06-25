package com.osung.parabara.datasource.model
import com.google.gson.annotations.SerializedName


data class ResponseImageUpload(
    @SerializedName("id")
    val id: Long,
    @SerializedName("url")
    val url: String
)