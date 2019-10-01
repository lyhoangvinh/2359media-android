package com.lyhoangvinh.app2369media.di.module

import com.lyhoangvinh.app2369media.ui.features.MainActivity
import com.lyhoangvinh.app2369media.ui.features.MainModule
import com.lyhoangvinh.app2369media.ui.features.movie.MovieFragment
import com.lyhoangvinh.app2369media.ui.features.movie.MovieModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun comicActivity(): MainActivity

    @ContributesAndroidInjector(modules = [MovieModule::class])
    abstract fun movieModule(): MovieFragment
}