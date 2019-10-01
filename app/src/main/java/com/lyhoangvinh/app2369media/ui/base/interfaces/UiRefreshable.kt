package com.lyhoangvinh.app2369media.ui.base.interfaces


interface UiRefreshable : Refreshable {
    fun doneRefresh()
    fun refreshWithUi()
    fun refreshWithUi(delay: Long)
}
