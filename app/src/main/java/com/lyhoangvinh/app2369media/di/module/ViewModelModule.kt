package com.lyhoangvinh.app2369media.di.module

import androidx.lifecycle.ViewModelProvider
import com.lyhoangvinh.app2369media.di.ViewModelFactory
import dagger.Binds
import dagger.Module


@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}