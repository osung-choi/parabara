package com.osung.parabara.datasource.provider

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.osung.parabara.datasource.GalleryDataSource
import com.osung.parabara.datasource.model.GalleryData
import com.osung.parabara.datasource.model.ResponseGallery
import io.reactivex.rxjava3.core.Single

class ProviderGalleryDataSource(
    private val context: Context
): GalleryDataSource {

    override fun requestGalleryImageList(): Single<ResponseGallery> {
        val gallerys = mutableListOf<GalleryData>()

        val sortOrder = "${MediaStore.Images.Media._ID} DESC"

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            sortOrder
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))

                val contentUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                gallerys.add(GalleryData(id, name, contentUri.toString()))

            }
            cursor.close()
        }

        return Single.just(ResponseGallery(gallerys))
    }
}