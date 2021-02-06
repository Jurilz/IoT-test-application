package com.example.testapplication.ui.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.testapplication.R
import com.example.testapplication.adapter.ServiceCardAdapter
import com.example.testapplication.databinding.FragmentStartBinding
import com.example.testapplication.repository.QrRepository
import com.example.testapplication.utility.viewModelFactories
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class StartFragment : Fragment() {

    private val startViewModel: StartViewModel by viewModelFactories {
        StartViewModel(QrRepository(requireContext()))
    }

    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentStartBinding.inflate(inflater)
        binding.viewModel = startViewModel

        binding.apiRecyclerView.adapter = ServiceCardAdapter(
            this,
            QrRepository(requireContext()),
            CoroutineScope(Dispatchers.Main))
//        binding.qrButton.setOnClickListener {
//            findNavController().navigate(R.id.navigation_qr_scanner)
//        }
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onResume() {
        startViewModel.getApiInformation()
        super.onResume()
    }
}