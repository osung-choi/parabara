package com.osung.parabara.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.*
import com.osung.parabara.datasource.ProductDataSource
import com.osung.parabara.datasource.remote.ProductPagingSource
import com.osung.parabara.repository.entity.EntityGalleryImage
import com.osung.parabara.repository.entity.EntityProduct
import com.osung.parabara.repository.entity.mapper
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class ProductRepositoryImpl(
    private val remote: ProductDataSource
) : ProductRepository {

    override fun getProductListPager(size: Int): LiveData<PagingData<EntityProduct>> {
        return Pager(
            config = PagingConfig(
                pageSize = size,
                initialLoadSize = size,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ProductPagingSource(remote) }
        ).liveData
            .map { it.map { response -> response.mapper() } }
    }

    override fun insertProduct(
        title: String,
        content: String,
        price: Long,
        images: List<EntityGalleryImage>
    ): Single<EntityProduct> {
        return Observable.fromIterable(images)
            .concatMapEager { remote.requestUploadProductImage(it.imagePath, it.fileName).toObservable() } //이미지 비동기 전송
            .map { it.data.id }
            .toList()
            .flatMap { remote.requestInsertProduct(title, content, price, it) }
            .map { it.mapper() }
    }

    override fun updateProduct(
        id: Long,
        title: String,
        content: String,
        price: Long
    ): Single<EntityProduct> {
        return remote.requestUpdateProduct(id, title, content, price)
            .map { it.mapper() }
    }

    override fun deleteProduct(id: Long): Single<Boolean> {
        return remote.requestDeleteProduct(id)
            .map { it.data }
    }
}