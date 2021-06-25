package com.osung.parabara.di

import com.osung.parabara.datasource.GalleryDataSource
import com.osung.parabara.datasource.ProductDataSource
import com.osung.parabara.datasource.provider.ProviderGalleryDataSource
import com.osung.parabara.datasource.remote.Api
import com.osung.parabara.datasource.remote.RemoteProductDataSource
import com.osung.parabara.repository.GalleryRepository
import com.osung.parabara.repository.GalleryRepositoryImpl
import com.osung.parabara.repository.ProductRepository
import com.osung.parabara.repository.ProductRepositoryImpl
import com.osung.parabara.view.gallery.GalleryViewModel
import com.osung.parabara.view.main.MainViewModel
import com.osung.parabara.view.regist.ProductRegistViewModel
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { ProductRegistViewModel(androidApplication(), get()) }
    viewModel { GalleryViewModel(get()) }
}

val dataModule = module {
    single<ProductRepository> {
        ProductRepositoryImpl(get())
    }

    single<ProductDataSource> {
        RemoteProductDataSource(get())
    }

    single<GalleryRepository> {
        GalleryRepositoryImpl(get())
    }

    single<GalleryDataSource> {
        ProviderGalleryDataSource(androidContext())
    }
}

val networkModule = module {
    single {
        Interceptor { chain ->
            chain.proceed(chain.request().newBuilder().apply {
                header("x-token", "bEWeyJpZCI6OPa5MiwicGj")
            }.build())
        }
    }

    single {
        OkHttpClient.Builder().apply {
            addInterceptor(get())
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }.build()
    }

    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Api.baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(get())
            .build()
            .create(Api::class.java)
    }
}