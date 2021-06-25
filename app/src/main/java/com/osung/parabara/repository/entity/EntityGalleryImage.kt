package com.osung.parabara.repository.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EntityGalleryImage(
    val id: Long = 0,
    val fileName: String = "",
    val imagePath: String
): Parcelable
