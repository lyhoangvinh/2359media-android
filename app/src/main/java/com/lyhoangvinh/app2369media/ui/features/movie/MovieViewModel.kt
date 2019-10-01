package com.lyhoangvinh.app2369media.ui.features.movie

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.lyhoangvinh.app2369media.data.entities.Movie
import com.lyhoangvinh.app2369media.data.entities.State
import com.lyhoangvinh.app2369media.data.entities.Status
import com.lyhoangvinh.app2369media.data.repo.MovieRepo
import com.lyhoangvinh.app2369media.data.source.MovieDataSource
import com.lyhoangvinh.app2369media.ui.base.viewmodel.BasePagingViewModel
import javax.inject.Inject

class MovieViewModel @Inject constructor(private val movieRepo: MovieRepo) :
    BasePagingViewModel<MovieAdapter>() {

    val title = "Now PLaying"

    override fun fetchData() {
        movieRepo.reset()
        adapter.submitState(State(Status.LOADING, null))
    }

    override fun onFirstTimeUiCreate(lifecycleOwner: LifecycleOwner, bundle: Bundle?) {
        movieRepo.liveMovie().observe(lifecycleOwner, Observer {
            adapter.submitList(it)
            publishState(State.success(null))
        })
    }
}