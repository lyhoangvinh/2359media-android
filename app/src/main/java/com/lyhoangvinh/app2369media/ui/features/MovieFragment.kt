package com.lyhoangvinh.app2369media.ui.features

import android.content.Context
import android.view.View
import com.lyhoangvinh.app2369media.R
import com.lyhoangvinh.app2369media.databinding.FragmentMovieBinding
import com.lyhoangvinh.app2369media.ui.base.fragment.BaseViewModelPagingFragment
import com.lyhoangvinh.app2369media.ui.features.movie.MovieAdapter
import com.lyhoangvinh.app2369media.ui.features.movie.MovieViewModel

class MovieFragment :
    BaseViewModelPagingFragment<FragmentMovieBinding, MovieViewModel, MovieAdapter>() {
    override fun createViewModelClass() = MovieViewModel::class.java
    override fun getLayoutResource() = R.layout.fragment_movie
    override fun initialize(view: View, ctx: Context?) {
        super.initialize(view, ctx)
        binding.vm = viewModel
        binding.toolbar.imvBack.setOnClickListener { onBackPressed() }
    }

    override fun onBackPressed(): Boolean {
        fragmentManager?.popBackStack()
        return true
    }
}