package com.lyhoangvinh.app2369media.data.repo

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.lyhoangvinh.app2369media.data.entities.Movie
import com.lyhoangvinh.app2369media.data.source.MovieDataSource
import javax.inject.Inject

class MovieRepo @Inject constructor(private val movieFactory: MovieDataSource.MovieFactory) : BaseRepo() {

    fun liveMovie(): LiveData<PagedList<Movie>> {
        val config = PagedList.Config.Builder()
            .setPageSize(50)
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(100)
            .setPrefetchDistance(50)
            .build()
        return LivePagedListBuilder(movieFactory, config).build()
    }

    fun reset() {
        execute {
            movieFactory.invalidate()
        }
    }
}