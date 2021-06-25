package com.osung.parabara.view.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.osung.parabara.R
import com.osung.parabara.databinding.ActivityMainBinding
import com.osung.parabara.repository.entity.EntityProduct
import com.osung.parabara.view.main.adapter.ProductClickListener
import com.osung.parabara.view.main.adapter.ProductListAdapter
import com.osung.parabara.view.regist.ProductRegistActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), ProductClickListener {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    private val adapter by lazy { ProductListAdapter(this) }

    //상품 등록, 수정 결과
    private val productLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK) {
                productListAdapterRefresh()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        init()
    }

    private fun init() {

        with(binding) {
            viewModel = this@MainActivity.viewModel
            lifecycleOwner = this@MainActivity

            productList.adapter = adapter

            //상품 추가
            productAdd.setOnClickListener {
                Intent(this@MainActivity, ProductRegistActivity::class.java).run {
                    productLauncher.launch(this)
                }
            }
        }

        //toolbar 세팅
        with(binding.mainToolbar) {
            back.visibility = View.GONE
            complete.visibility = View.GONE
            title.text = getString(R.string.product_list)
        }

        viewModel.completeRemoveProduct.observe(this, {
            if(it) productListAdapterRefresh()
        })

        viewModel.productList.observe(this, {
            adapter.submitData(lifecycle, it)
        })
    }

    /**
     * 상품 리스트 갱신
     *
     */
    private fun productListAdapterRefresh() {
        adapter.refresh()

        Handler(Looper.getMainLooper()).postDelayed({
            binding.productList.scrollToPosition(0)
        },200)
    }

    /**
     * 상품 클릭 이벤트
     * 상품 수정으로 진입.
     *
     * @param productItem
     */
    override fun onProductItemClick(productItem: EntityProduct) {
        Intent(this, ProductRegistActivity::class.java).apply {
            putExtra(ProductRegistActivity.INTENT_UPDATE_PRODUCT, productItem)
        }.run {
            productLauncher.launch(this)
        }
    }

    /**
     * 상품 삭제 이벤트
     *
     * @param productItem
     */
    override fun onProductRemoveClick(productItem: EntityProduct) {
        AlertDialog.Builder(this)
            .setTitle(R.string.product_remove)
            .setMessage(R.string.product_remove_comment)
            .setPositiveButton(R.string.remove) { _, _ ->
                viewModel.removeProductItem(productItem)
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}