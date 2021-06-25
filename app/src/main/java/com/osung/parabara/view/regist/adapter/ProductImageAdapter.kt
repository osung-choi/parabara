package com.osung.parabara.view.regist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.osung.parabara.databinding.ItemProductImageBinding
import com.osung.parabara.repository.entity.EntityGalleryImage
import com.osung.parabara.view.diffutil.GalleryImageDiffCallback

class ProductImageAdapter(
    private val listener: ProductImageClickListener
): ListAdapter<EntityGalleryImage, ProductImageAdapter.ProductImageViewHolder>(GalleryImageDiffCallback()) {

    var isEditMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ProductImageViewHolder(ItemProductImageBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ProductImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductImageViewHolder(
        private val binding: ItemProductImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: EntityGalleryImage) {
            binding.removeImage.visibility = if(isEditMode)
                View.GONE
            else
                View.VISIBLE

            binding.productImage = image

            binding.removeImage.setOnClickListener {
                listener.onProductImageRemove(image)
            }
        }
    }

}