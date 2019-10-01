package com.lyhoangvinh.app2369media.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lyhoangvinh.app2369media.di.ViewModelFactory
import com.lyhoangvinh.app2369media.di.qualifier.ViewModelKey
import com.lyhoangvinh.app2369media.ui.features.movie.MovieViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MovieViewModel::class)
    internal abstract fun movieViewModel(viewModel: MovieViewModel): ViewModel
}