package com.osung.parabara

import android.app.Application
import com.osung.parabara.di.dataModule
import com.osung.parabara.di.networkModule
import com.osung.parabara.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(networkModule, viewModelModule, dataModule)
        }
    }
}