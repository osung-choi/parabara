package com.osung.parabara.view.gallery

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.osung.parabara.R
import com.osung.parabara.databinding.ActivityGalleryBinding
import com.osung.parabara.view.gallery.adapter.GalleryAdapter
import com.osung.parabara.view.regist.ProductRegistActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class GalleryActivity : AppCompatActivity() {
    private val viewModel: GalleryViewModel by viewModel()
    private lateinit var binding: ActivityGalleryBinding

    private val adapter by lazy { GalleryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery)

        init()
    }

    private fun init() {
        with(binding) {
            viewModel = this@GalleryActivity.viewModel
            lifecycleOwner = this@GalleryActivity
            photoList.adapter = adapter
        }

        with(binding.galleryToolbar) {
            title.text = getString(R.string.gallery)
            back.setOnClickListener { finish() }
        }

        binding.galleryToolbar.complete.setOnClickListener {
            if(adapter.selectImage.size > 0) {
                setResult(RESULT_OK, Intent().apply {
                    putParcelableArrayListExtra(ProductRegistActivity.INTENT_UPLOAD_IMAGE, ArrayList(adapter.selectImage))
                })

                finish()
            }
        }
    }
}