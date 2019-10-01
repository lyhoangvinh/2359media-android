package com.lyhoangvinh.app2369media.data.services

import com.lyhoangvinh.app2369media.data.response.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("movie/{movie_id}/list")
    fun getMovies(@Path("movie_id") movie_id: Int, @Query("api_key") api_key: String, @Query("page") page: Int): Single<MovieResponse>
}