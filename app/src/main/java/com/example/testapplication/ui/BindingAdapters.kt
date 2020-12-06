package com.example.testapplication.ui

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.network.ServiceResponse
import com.example.testapplication.network.Services
import com.example.testapplication.ui.start.ServiceCardAdapter

//@BindingAdapter("services")
//fun RecyclerView.bindServices(services: List<Services>?) {
//    val adapter = adapter as ServiceCardAdapter
//    adapter.submitList(services)
//}

@BindingAdapter("serviceResponses")
fun RecyclerView.bindServiceResponses(serviceResponses: List<ServiceResponse>?) {
    val adapter = adapter as ServiceCardAdapter
    adapter.submitList(serviceResponses)
}