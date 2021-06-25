package com.osung.parabara.view.gallery

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.osung.parabara.repository.entity.EntityGalleryImage
import com.osung.parabara.view.gallery.adapter.GalleryAdapter

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("app:setGalleryImageList")
    fun setGalleryImageList(recyclerView: RecyclerView, galleryImageList: List<EntityGalleryImage>?) {
        galleryImageList?.let {
            (recyclerView.adapter as? GalleryAdapter)?.submitList(it)
        }
    }

    @JvmStatic
    @BindingAdapter("app:showGalleryImage")
    fun setGalleryImage(imageView: ImageView, uri: String) {
        Glide.with(imageView)
            .load(Uri.parse(uri))
            .centerCrop()
            .into(imageView)
    }
}