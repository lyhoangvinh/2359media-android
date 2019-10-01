package com.lyhoangvinh.app2369media.di.module

import com.google.gson.Gson
import com.lyhoangvinh.app2369media.Constants
import com.lyhoangvinh.app2369media.MyApplication
import com.lyhoangvinh.app2369media.data.services.MovieService
import com.lyhoangvinh.app2369media.di.qualifier.ApplicationContext
import com.lyhoangvinh.app2369media.di.qualifier.OkHttpNoAuth
import com.lyhoangvinh.app2369media.utils.ConnectionLiveData
import com.lyhoangvinh.app2369media.utils.makeOkHttpClientBuilder
import com.lyhoangvinh.app2369media.utils.makeService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @OkHttpNoAuth
    @Singleton
    internal fun provideOkHttpClientNoAuth(@ApplicationContext context: MyApplication): OkHttpClient = makeOkHttpClientBuilder(context).build()


    @Singleton
    @Provides
    internal fun providesConnectionLiveData(context: MyApplication): ConnectionLiveData = ConnectionLiveData(context)

    @Provides
    @Singleton
    internal fun provideMovieService(gSon: Gson, @OkHttpNoAuth okHttpClient: OkHttpClient): MovieService =
        makeService(MovieService::class.java, gSon, okHttpClient, Constants.HOST)

}
