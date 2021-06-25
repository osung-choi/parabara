package com.osung.parabara.repository.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EntityProduct(
    val content: String,
    val id: Long,
    val images: List<String>,
    val price: Long,
    val title: String

):Parcelable