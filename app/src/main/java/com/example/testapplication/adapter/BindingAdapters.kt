package com.example.testapplication.ui

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.adapter.ServiceCardAdapter
import com.example.testapplication.domain.Service

@BindingAdapter("services")
fun RecyclerView.bindServices(services: List<Service>?) {
    val adapter = adapter as ServiceCardAdapter
    adapter.submitList(services)
}

@BindingAdapter("present")
fun View.bindPresence(present: Boolean?) {
    visibility = if (present == true) View.VISIBLE else View.GONE
}

@BindingAdapter("visible")
fun View.bindVisibility(visible: Boolean?) {
    visibility = if (visible == true) View.VISIBLE else View.INVISIBLE
}
