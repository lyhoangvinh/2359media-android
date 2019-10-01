package com.lyhoangvinh.app2369media.ui.features

import com.lyhoangvinh.app2369media.ui.base.activity.BaseSingleFragmentActivity
import com.lyhoangvinh.app2369media.ui.features.movie.MovieFragment

class MainActivity : BaseSingleFragmentActivity<MovieFragment>() {
    override fun createFragment() = MovieFragment()
}
