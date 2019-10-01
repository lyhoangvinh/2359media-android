package com.lyhoangvinh.app2369media.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*
      "description": "<3",
      "favorite_count": 0,
      "id": 101542,
      "item_count": 610,
      "iso_639_1": "en",
      "list_type": "movie",
      "name": "Vaached❤",
      "poster_path": null
 */
data class Movie(
    @SerializedName("id")
    @Expose
    var id : Long? = 0,

    @SerializedName("description")
    @Expose
    var description : String? = "",

    @SerializedName("favorite_count")
    @Expose
    var favoriteCount : Int? = 0,

    @SerializedName("item_count")
    @Expose
    var item_count : Int? = 0,

    @SerializedName("name")
    @Expose
    var name : String? = "",

    @SerializedName("poster_path")
    @Expose
    var posterPath : String? = ""
)