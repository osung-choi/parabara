package com.osung.parabara.view.regist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.osung.parabara.R
import com.osung.parabara.databinding.ActivityProductRegistBinding
import com.osung.parabara.repository.entity.EntityGalleryImage
import com.osung.parabara.repository.entity.EntityProduct
import com.osung.parabara.view.gallery.GalleryActivity
import com.osung.parabara.view.regist.adapter.ProductImageAdapter
import com.osung.parabara.view.regist.adapter.ProductImageClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductRegistActivity : AppCompatActivity(), ProductImageClickListener {
    private val viewModel: ProductRegistViewModel by viewModel()
    private lateinit var binding: ActivityProductRegistBinding

    private val adapter by lazy { ProductImageAdapter(this) }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                val intent = Intent(this, GalleryActivity::class.java)
                uploadImageLauncher.launch(intent)
            }
        }

    private val uploadImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val selectImageList =
                    it.data?.getParcelableArrayListExtra<EntityGalleryImage>(INTENT_UPLOAD_IMAGE) ?: arrayListOf()

                viewModel.addProductImageList(selectImageList)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_regist)

        init()
    }

    private fun init() {
        with(binding) {
            viewModel = this@ProductRegistActivity.viewModel
            lifecycleOwner = this@ProductRegistActivity

            productImageList.layoutManager = LinearLayoutManager(this@ProductRegistActivity).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            productImageList.adapter = adapter

        }

        //toolbar 세팅
        with(binding.productToolbar) {
            title.text = getString(R.string.product)
            complete.visibility = View.GONE

            back.setOnClickListener { finish() }
        }

        viewModel.completeSaveProduct.observe(this, {
            setResult(RESULT_OK)
            finish()
        })

        //intent 상품 데이터 전달 여부에 따라 상품 추가 또는 상품 수정 모드로 변경
        intent.getParcelableExtra<EntityProduct>(INTENT_UPDATE_PRODUCT)?.let {
            updateMode()
            viewModel.setProductEditMode(it)

        }?: insertMode()
    }

    /**
     * 상품 추가 모드
     *
     */
    private fun insertMode() {
        with(binding){
            selectImage.setOnClickListener {
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            productRegist.text = getString(R.string.product_regist)
        }

        adapter.isEditMode = false
    }

    /**
     * 상품 수정 모드
     *
     */
    private fun updateMode() {
        binding.productRegist.text = getString(R.string.product_update)
        adapter.isEditMode = true
    }

    /**
     * 선택한 상품(갤러리) 이미지 삭제
     *
     * @param image
     */
    override fun onProductImageRemove(image: EntityGalleryImage) {
        viewModel.removeProductImage(image)
    }

    companion object {
        const val INTENT_UPLOAD_IMAGE = "intent_upload_image"
        const val INTENT_UPDATE_PRODUCT = "intent_update_product"
    }
}