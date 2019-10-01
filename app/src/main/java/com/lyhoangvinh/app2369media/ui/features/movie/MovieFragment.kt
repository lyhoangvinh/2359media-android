package com.lyhoangvinh.app2369media.ui.features.movie

import android.app.Dialog
import android.content.Context
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lyhoangvinh.app2369media.R
import com.lyhoangvinh.app2369media.databinding.FragmentMovieBinding
import com.lyhoangvinh.app2369media.ui.base.fragment.BaseViewModelPagingFragment
import com.lyhoangvinh.app2369media.utils.createConnectionDialog
import com.lyhoangvinh.app2369media.utils.setStatusBarColor
import com.lyhoangvinh.app2369media.utils.setVisibility

class MovieFragment :
    BaseViewModelPagingFragment<FragmentMovieBinding, MovieViewModel, MovieAdapter>() {
    private var connectionDialog: Dialog? = null
    override fun createViewModelClass() = MovieViewModel::class.java
    override fun getLayoutResource() = R.layout.fragment_movie
    override fun initialize(view: View, ctx: Context?) {
        setStatusBarColor(R.color.material_blue_700)
        super.initialize(view, ctx)
        binding.vm = viewModel
        binding.toolbar.imvBack.setVisibility(false)
        connectionDialog = createConnectionDialog()
        viewModel.connectionLiveData.observe(this, Observer {
            if (!connectionDialog!!.isShowing && !it.isConnected) {
                connectionDialog!!.show()
            } else {
                connectionDialog!!.hide()
            }
        })
    }

    override fun createLayoutManager(): RecyclerView.LayoutManager = GridLayoutManager(activity, 2)
}