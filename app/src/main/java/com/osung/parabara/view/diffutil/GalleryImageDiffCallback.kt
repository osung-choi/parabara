package com.osung.parabara.view.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.osung.parabara.repository.entity.EntityGalleryImage

class GalleryImageDiffCallback: DiffUtil.ItemCallback<EntityGalleryImage>() {
    override fun areItemsTheSame(oldItem: EntityGalleryImage, newItem: EntityGalleryImage) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: EntityGalleryImage, newItem: EntityGalleryImage) = oldItem == newItem
}