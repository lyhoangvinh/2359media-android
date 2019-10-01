package com.lyhoangvinh.app2369media.data.source

import android.os.Handler
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.lyhoangvinh.app2369media.Constants
import com.lyhoangvinh.app2369media.data.entities.Movie
import com.lyhoangvinh.app2369media.data.entities.State
import com.lyhoangvinh.app2369media.data.entities.Status
import com.lyhoangvinh.app2369media.data.services.MovieService
import com.lyhoangvinh.app2369media.utils.SafeMutableLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import lyhoangvinh.com.myutil.navigation.PlainConsumer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDataSource @Inject constructor(private val movieService: MovieService) :
    PageKeyedDataSource<Int, Movie>() {

    private var stateLiveData: SafeMutableLiveData<State>? = null

    private var compositeDisposable: CompositeDisposable? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        callApi(page = 1, loadInitialCallback = callback)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        callApi(page = params.key, loadCallback = callback)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        // Do nothing, since data is loaded from our initial load itself
    }

    fun setCompositeDisposable(compositeDisposable: CompositeDisposable) {
        this.compositeDisposable = compositeDisposable
    }

    fun dispose() {
        if (compositeDisposable != null) {
            compositeDisposable?.dispose()
        }
        stateLiveData = null
        compositeDisposable = null
    }

    private fun callApi(
        page: Int,
        loadInitialCallback: LoadInitialCallback<Int, Movie>? = null,
        loadCallback: LoadCallback<Int, Movie>? = null
    ) {
        publishState(State.loading(null))
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(getResourceFollowable(page).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe { resource ->
                if (resource != null) {
                    Log.d("source", "addRequest: resource changed: $resource")
                    if (resource.data != null) {
                        val nextPage = page + 1
                        resource.data.results?.let {
                            loadInitialCallback?.onResult(
                                it,
                                0,
                                resource.data.results!!.size,
                                null,
                                nextPage
                            )
                        }
                        resource.data.results?.let { loadCallback?.onResult(it, nextPage) }
                    }
                    if (resource.state.status == Status.LOADING) {
                        // do nothing if progress showing is not allowed
                    } else {
                        publishState(resource.state)
                    }
                }
            })
    }


    fun getStateLiveData(): SafeMutableLiveData<State> {
        if (stateLiveData == null) {
            stateLiveData = SafeMutableLiveData()
        }
        return stateLiveData!!
    }

    private fun publishState(state: State) {
        if (stateLiveData == null) {
            stateLiveData = SafeMutableLiveData()
        }
        stateLiveData?.postValue(state)
        if (!TextUtils.isEmpty(state.message)) {
            // if state has a message, after show it, we should reset to prevent
            //            // message will still be shown if fragment / activity is rotated (re-observe state live data)
            Handler().postDelayed({
                stateLiveData?.setValue(
                    State.success(
                        null
                    )
                )
            }, 100)
        }
    }

    private fun getResourceFollowable(page: Int) =
        createResource(movieService.getMovies(500, Constants.KEY, page), null)


    @Singleton
    class MovieFactory @Inject constructor(
        private val movieService: MovieService
    ) :
        DataSource.Factory<Int, Movie>() {
        private val sourceLiveData = MutableLiveData<MovieDataSource>()
        private var newSource: MovieDataSource? = MovieDataSource(movieService)
        fun invalidate() {
            sourceLiveData.value?.invalidate()
        }
        override fun create(): DataSource<Int, Movie> {
            newSource = null
            newSource = MovieDataSource(movieService)
            sourceLiveData.postValue(newSource)
            return newSource!!
        }
    }
}


/**
 * For single data
 * @param remote
 * @param onSave
 * @param <T>
 * @return
</T> */
fun <T> createResource(
    remote: Single<T>,
    onSave: PlainConsumer<T>?
): Flowable<Resource<T>> {
    return Flowable.create({
        object : SimpleNetworkBoundSource<T>(it, true) {
            override fun getRemote() = remote
            override fun saveCallResult(data: T, isRefresh: Boolean) {
                onSave?.accept(data)
            }
        }
    }, BackpressureStrategy.BUFFER)
}
