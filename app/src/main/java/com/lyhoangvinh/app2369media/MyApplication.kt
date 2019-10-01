package com.lyhoangvinh.app2369media

import com.lyhoangvinh.app2369media.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class MyApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().create(this)

    private var mDeviceWidth = 0

    companion object {
        private lateinit var instance: MyApplication

        fun getInstance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}