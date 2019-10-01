package com.lyhoangvinh.app2369media.di.component

import com.lyhoangvinh.app2369media.MyApplication
import com.lyhoangvinh.app2369media.di.module.AppModule
import com.lyhoangvinh.app2369media.di.module.BuildersModule
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import com.lyhoangvinh.app2369media.di.module.NetworkModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by lyhoangvinh on 05/01/18.
 */
@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class,
        AppModule::class,
        NetworkModule::class,
        BuildersModule::class]
)
interface AppComponent : AndroidInjector<MyApplication> {
    @Suppress("DEPRECATION")
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MyApplication>()
}
