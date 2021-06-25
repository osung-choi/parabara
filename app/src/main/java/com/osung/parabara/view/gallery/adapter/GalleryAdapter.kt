package com.osung.parabara.view.gallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.osung.parabara.databinding.ItemGalleryImageBinding
import com.osung.parabara.repository.entity.EntityGalleryImage
import com.osung.parabara.view.diffutil.GalleryImageDiffCallback

class GalleryAdapter: ListAdapter<EntityGalleryImage, GalleryAdapter.GalleryViewHolder>(
    GalleryImageDiffCallback()
) {
    var selectImage = mutableListOf<EntityGalleryImage>()
    private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GalleryViewHolder(ItemGalleryImageBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GalleryViewHolder(
        private val binding: ItemGalleryImageBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(galleryImage: EntityGalleryImage) {
            with(binding) {
                this.galleryImage = galleryImage

                selectGallery.setOnCheckedChangeListener { _, isChecked ->
                    if(isChecked) {
                        selectImage.add(galleryImage)
                    }else {
                        selectImage.remove(galleryImage)
                    }
                }
            }
        }
    }
}