package com.example.testapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.databinding.CardServiceBinding
import com.example.testapplication.domain.Service
import com.example.testapplication.network.ServiceKind
import com.example.testapplication.network.Services
import com.example.testapplication.repository.QrRepository
import kotlinx.coroutines.*


class ServiceCardAdapter(private val qrRepository: QrRepository, private val scope: CoroutineScope): ListAdapter<Service, ServiceCardAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(
        private val binding: CardServiceBinding,
        private val qrRepository: QrRepository,
        private val scope: CoroutineScope
        ): RecyclerView.ViewHolder(binding.root) {

        private var job: Job? = null

        fun bind(service: Service) {
            job?.cancel()
            binding.apply {
                binding.service = service
                binding.singleResponse = null
                binding.flagResponse = null
                serviceValue.visibility = View.INVISIBLE
                serviceTimestamp.visibility = View.INVISIBLE
                serviceFlag.visibility = View.INVISIBLE
                loadingStatus = false
            }.executePendingBindings()

            job = scope.launch {
                getAndBindServiceResponse(service)
            }
        }

        suspend fun getAndBindServiceResponse(service: Service) {
            when (service.kind) {
                ServiceKind.single.toString() -> bindSingleResponse(service)
                ServiceKind.flag.toString() -> bindFlagResponse(service)
//                ServiceKind.timeseries.toString() -> setTimeseriesResponse(           )
            }
        }

        suspend fun bindSingleResponse(service: Service) {
            qrRepository.fetchSingleResponse(service)
            val singleResponse = qrRepository.getSingleResponseByEndpoint(service)
            binding.apply {
                binding.singleResponse = singleResponse
                serviceValue.visibility = View.VISIBLE
                serviceTimestamp.visibility = View.VISIBLE
            }.executePendingBindings()
            binding.loadingStatus = true
        }

        suspend fun bindFlagResponse(service: Service) {
            qrRepository.fetchFlagResponse(service)
            val flag = qrRepository.getFlagResponseByEndpoint(service)
            binding.apply {
                serviceFlag.visibility = View.VISIBLE
                binding.flagResponse = flag
            }.executePendingBindings()
            binding.loadingStatus = true
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Service>() {
        override fun areItemsTheSame(oldItem: Service, newItem: Service): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Service, newItem: Service): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CardServiceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            qrRepository,
            scope
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val service: Service = getItem(position)
        holder.bind(service)
    }
}
