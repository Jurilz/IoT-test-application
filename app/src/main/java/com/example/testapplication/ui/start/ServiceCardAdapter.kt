package com.example.testapplication.ui.start

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.databinding.CardServiceBinding
import com.example.testapplication.network.ServiceResponse
import com.example.testapplication.network.Services

class ServiceCardAdapter : ListAdapter<ServiceResponse, ServiceCardAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: CardServiceBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(serviceResponse: ServiceResponse) {
            binding.serviceResponse = serviceResponse
            binding.executePendingBindings()
        }

    }

    companion object DiffCallback: DiffUtil.ItemCallback<ServiceResponse>() {
        override fun areItemsTheSame(oldItem: ServiceResponse, newItem: ServiceResponse): Boolean {
            return oldItem.service.name == newItem.service.name
        }

        override fun areContentsTheSame(oldItem: ServiceResponse, newItem: ServiceResponse): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CardServiceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val serviceResponse: ServiceResponse = getItem(position)
        holder.bind(serviceResponse)
    }
}
