package com.lyhoangvinh.app2369media.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.lyhoangvinh.app2369media.data.entities.Movie

data class MovieResponse(
    @SerializedName("id")
    @Expose
    var id : Long? = 0,

    @SerializedName("page")
    @Expose
    var page : Int? = 0,

    @SerializedName("results")
    @Expose
    var results : List<Movie>? = null
)