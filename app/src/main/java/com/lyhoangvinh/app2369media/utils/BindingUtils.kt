package com.lyhoangvinh.app2369media.utils


import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.lyhoangvinh.app2369media.Constants
import com.squareup.picasso.Picasso
import com.lyhoangvinh.app2369media.R


object BindingUtils {

    @JvmStatic
    @BindingAdapter("loadImageURL")
    fun loadImageURL(imageView: ImageView, url: String?) {
        val imgUrl = Constants.IMAGE_URL + url
        Picasso.get()
            .load(imgUrl)
            .placeholder(R.drawable.ic_placeholder_rectangle_200px)
            .error(R.drawable.ic_placeholder_rectangle_200px)
            .centerCrop()
            .fit()
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("favoriteCount")
    fun favoriteCount(textView: TextView, favoriteCount: Int?){
        textView.text = "$favoriteCount"
    }

    @JvmStatic
    @BindingAdapter("setStartCollapsingAnimation")
    fun setStartCollapsingAnimation(view: TextView, text: String) {
        view.startCollapsingAnimation(text, 500L)
    }
}