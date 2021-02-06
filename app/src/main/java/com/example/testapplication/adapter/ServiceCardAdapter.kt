package com.example.testapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.databinding.CardServiceBinding
import com.example.testapplication.domain.Service
import com.example.testapplication.network.ServiceKind
import com.example.testapplication.repository.QrRepository
import com.example.testapplication.ui.start.StartFragment
import com.example.testapplication.utility.clicks
import com.example.testapplication.utility.initLineChart
import com.example.testapplication.utility.updateLineChart
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*


class ServiceCardAdapter(private val fragment: StartFragment, private val qrRepository: QrRepository, private val scope: CoroutineScope): ListAdapter<Service, ServiceCardAdapter.ViewHolder>(DiffCallback) {

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
                serviceValue.visibility = View.GONE
                serviceTimestamp.visibility = View.GONE
                serviceFlag.visibility = View.GONE
                timeseriesChart.visibility = View.GONE
                button.visibility = View.GONE
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
                ServiceKind.timeseries.toString() -> bindTimeseriesResponse(service)
                ServiceKind.action.toString() -> bindActionButton(service)
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

        suspend fun bindTimeseriesResponse(service: Service) {
            qrRepository.fetchTimeseriesResponse(service)
            binding.apply {
                initLineChart(timeseriesChart)
                timeseriesChart.visibility = View.VISIBLE
                binding.loadingStatus = true
            }.executePendingBindings()


            qrRepository.currentMeasurements.asLiveData()
                .observe(fragment.viewLifecycleOwner,  { timeseries ->
                    run {
                        if (timeseries == null || timeseries.isEmpty()) return@run

                        val referenceTimestamp = timeseries[0].timestamp
                        val entries = timeseries.map { value ->
                            Entry(
                                (value.timestamp - referenceTimestamp).toFloat(),
                                value.value
                            )
                        }
                        // resources.configuration.locales[0] requires API level 24
                        @Suppress("DEPRECATION")
                        // resources.configuration.locales[0] requires API level 24
                        updateLineChart(
                            binding.timeseriesChart,
                            entries,
                            "Last Measurements of 2 Months",
                            Locale.GERMAN,
                            referenceTimestamp
                        )

                    }
                })

        }

        suspend fun bindActionButton(service: Service) {
            binding.apply {
                loadingBarCard.visibility = View.INVISIBLE
                doneImageCard.visibility = View.INVISIBLE
                button.visibility = View.VISIBLE
                button.text = service.actionLabel
                button.clicks().onEach {
                    qrRepository.sendActionCommand(service)
                }.launchIn(CoroutineScope(Dispatchers.IO))
            }.executePendingBindings()

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
