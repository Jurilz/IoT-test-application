package com.example.testapplication.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.ui.OnClickListener
import com.example.testapplication.adapter.GenericPagedListAdapter.ViewHolder
import com.example.testapplication.adapter.GenericPagedListAdapter.ViewHolderFactory

class GenericPagedListAdapter<T>(
    private val onClickListener: OnClickListener<T>,
    private val viewHolderFactory: ViewHolderFactory<T>,
    diffCallback: DiffUtil.ItemCallback<T>
): PagedListAdapter<T, ViewHolder<T>>(diffCallback) {

    interface ViewHolderFactory<T> {
        fun createViewHolder(parent: ViewGroup): ViewHolder<T>
    }

    abstract class ViewHolder<T>(view: View): RecyclerView.ViewHolder(view) {
        abstract fun bind(item: T, onClickListener: OnClickListener<T>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        return viewHolderFactory.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        when (val item = getItem(position)) {
            null -> Unit
            else -> holder.bind(item, onClickListener)
        }
    }
}