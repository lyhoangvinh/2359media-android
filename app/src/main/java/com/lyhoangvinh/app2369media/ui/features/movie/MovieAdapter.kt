package com.lyhoangvinh.app2369media.ui.features.movie

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.lyhoangvinh.app2369media.R
import com.lyhoangvinh.app2369media.data.entities.Movie
import com.lyhoangvinh.app2369media.databinding.ItemMovieBinding
import com.lyhoangvinh.app2369media.di.qualifier.ActivityContext
import com.lyhoangvinh.app2369media.ui.base.adapter.BasePagedAdapter
import com.lyhoangvinh.app2369media.ui.base.adapter.BaseViewHolder
import com.lyhoangvinh.app2369media.utils.NavigatorHelper

import javax.inject.Inject

class MovieAdapter @Inject constructor(@ActivityContext context: Context, private val navigatorHelper: NavigatorHelper) :
    BasePagedAdapter<Movie, ItemMovieBinding>(context, ItemCallBack) {
    override fun itemLayoutResource() = R.layout.item_movie
    override fun createViewHolder(itemView: View) = VideoViewHolder(itemView)
    override fun onBindViewHolder(binding: ItemMovieBinding, dto: Movie, position: Int) {
        binding.dto = dto
    }
    class VideoViewHolder(itemView: View) : BaseViewHolder<ItemMovieBinding>(itemView)
    private object ItemCallBack : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(currentItem: Movie, nextItem: Movie): Boolean {
            return currentItem.id == nextItem.id
        }
        override fun areContentsTheSame(currentItem: Movie, nextItem: Movie): Boolean {
            return currentItem == nextItem
        }
    }
}