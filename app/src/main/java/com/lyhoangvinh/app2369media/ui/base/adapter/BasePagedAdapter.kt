package com.lyhoangvinh.app2369media.ui.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.lyhoangvinh.app2369media.R
import com.lyhoangvinh.app2369media.data.entities.State
import com.lyhoangvinh.app2369media.data.entities.Status
import com.lyhoangvinh.app2369media.databinding.ItemLoadingBinding
import com.lyhoangvinh.app2369media.di.qualifier.ActivityContext
import com.lyhoangvinh.app2369media.utils.genericCastOrNull


abstract class BasePagedAdapter<T, B : ViewDataBinding>(
    @ActivityContext val context: Context, diffUtil: DiffUtil.ItemCallback<T>
) : PagedListAdapter<T, BaseViewHolder<B>>(diffUtil) {

    private var state: State? = null

    companion object {
        const val ITEM_LOADING = 0
        const val ITEM_DATA = 1
    }

    abstract fun itemLayoutResource(): Int

    abstract fun createViewHolder(itemView: View):  BaseViewHolder<B>

    protected abstract fun onBindViewHolder(binding: B, dto: T, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  BaseViewHolder<B> {
        return when(viewType){
            ITEM_DATA -> genericCastOrNull(this.createViewHolder(LayoutInflater.from(context).inflate(itemLayoutResource(), parent, false)))
            else -> genericCastOrNull(LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false)))
        }
    }

    override fun onBindViewHolder(vh:  BaseViewHolder<B>, position: Int) {
        if (vh is LoadingViewHolder) {
            vh.binding.isShow = true
        } else {
            val item = getItem(position)
            if (item != null) {
                this.onBindViewHolder(vh.binding, item, position)
            }
        }
    }

    class LoadingViewHolder(itemView: View) : BaseViewHolder<ItemLoadingBinding>(itemView)

    override fun getItemCount(): Int {
        return super.getItemCount() + getExtraRow()
    }

    override fun getItemViewType(position: Int): Int {
        // Reached at the end
        return if (hasExtraRow() && position == itemCount - 1) {
            ITEM_LOADING
        } else {
            ITEM_DATA
        }
    }

    fun submitState(newState: State) {
        val previousNetworkState = State
        val hadExtraRow = hasExtraRow()
        state = newState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && !previousNetworkState.equals(newState)) {
            notifyItemChanged(itemCount - 1)
        }
    }

    // Add an item for the state
    private fun getExtraRow(): Int {
        return if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow(): Boolean {
        return state != null && state?.status != Status.SUCCESS
    }
}