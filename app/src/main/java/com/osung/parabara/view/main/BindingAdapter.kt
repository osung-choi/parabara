package com.osung.parabara.view.main

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.osung.parabara.R

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("app:showProductMainImage")
    fun showProductMainImage(imageView: ImageView, images: List<String>?) {
        images?.let {
            val imageUrl = if(it.isNotEmpty()) it[0] else ""

            Glide.with(imageView)
                .load(imageUrl)
                .placeholder(R.drawable.ic_default_image)
                .transform(CenterCrop(), RoundedCorners(15))
                .into(imageView)
        }
    }
}