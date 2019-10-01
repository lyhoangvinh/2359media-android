package com.lyhoangvinh.app2369media.ui.base.viewmodel

import androidx.annotation.CallSuper
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.lyhoangvinh.app2369media.data.entities.DataEmpty
import com.lyhoangvinh.app2369media.utils.SafeMutableLiveData


abstract class BasePagingViewModel<A : RecyclerView.Adapter<*>> : BaseViewModel() {

    @Nullable
    lateinit var adapter: A

    var dataEmptySafeMutableLiveData = SafeMutableLiveData<DataEmpty>()

    @CallSuper
    open fun initAdapter(@NonNull adapter: A) {
        this.adapter = adapter
    }

    /**
     * refreshUi All
     */
    @CallSuper
    fun refresh() {
        fetchData()
    }

    protected abstract fun fetchData()

    /**
     *  update empty view
     */
    fun hideNoDataState(isEmpty: Boolean) {
        dataEmptySafeMutableLiveData.setValue(DataEmpty(isEmpty))
    }
}