package com.osung.parabara.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.osung.parabara.repository.ProductRepository
import com.osung.parabara.repository.entity.EntityProduct
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel(
    private val repository: ProductRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    //상품 리스트 페이징 관찰
    val productList: LiveData<PagingData<EntityProduct>> = repository.getProductListPager(10)
        .cachedIn(this)

    //상품 삭제 결과 관찰
    private val _completeRemoveProduct = MutableLiveData<Boolean>()
    val completeRemoveProduct: LiveData<Boolean> = _completeRemoveProduct

    /**
     * 상품 삭제 요청
     *
     * @param removeProduct
     */
    fun removeProductItem(removeProduct: EntityProduct) {
        repository.deleteProduct(removeProduct.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _completeRemoveProduct.value = result
            }, {})
            .run {
                compositeDisposable.add(this)
            }
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}