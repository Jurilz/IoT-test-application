package com.example.testapplication.adapter

import androidx.recyclerview.widget.DiffUtil

fun <T> serviceCallBack(isSame: (T, T) -> Boolean, hasSameContent: (T, T) -> Boolean) = object : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return isSame(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return hasSameContent(oldItem, newItem)
    }
}
