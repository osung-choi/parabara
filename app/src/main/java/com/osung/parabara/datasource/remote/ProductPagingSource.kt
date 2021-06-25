package com.osung.parabara.datasource.remote

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.osung.parabara.datasource.ProductDataSource
import com.osung.parabara.datasource.model.ResponseProductData
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException

class ProductPagingSource(
    private val remote: ProductDataSource
): RxPagingSource<Int, ResponseProductData>() {
    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ResponseProductData>> {
        val position = params.key?: 1

        return remote.requestProductPage(position, params.loadSize)
            .subscribeOn(Schedulers.io())
            .map<LoadResult<Int, ResponseProductData>> { result ->
                LoadResult.Page(
                    data = result.data.productList,
                    prevKey = if(position == 1) null else position - 1,
                    nextKey = if(result.data.total == position) null else position + 1)
            }
            .onErrorReturn { e ->
                when(e) {
                    is IOException -> LoadResult.Error(e)
                    is HttpException -> LoadResult.Error(e)
                    else -> throw e
                }
            }
    }


    override fun getRefreshKey(state: PagingState<Int, ResponseProductData>): Int {
        return 1
    }
}