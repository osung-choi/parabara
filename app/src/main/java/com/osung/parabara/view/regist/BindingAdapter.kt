package com.osung.parabara.view.regist

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.osung.parabara.repository.entity.EntityGalleryImage
import com.osung.parabara.view.regist.adapter.ProductImageAdapter

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("app:setProductImageList")
    fun setProductImageList(recyclerView: RecyclerView, list: List<EntityGalleryImage>?) {
        list?.let {
            (recyclerView.adapter as? ProductImageAdapter)?.submitList(it)
        }
    }



    @JvmStatic
    @BindingAdapter("app:showProductImage")
    fun showProductImage(imageView: ImageView, uri: String) {
        Glide.with(imageView)
            .load(Uri.parse(uri))
            .centerCrop()
            .into(imageView)
    }
}