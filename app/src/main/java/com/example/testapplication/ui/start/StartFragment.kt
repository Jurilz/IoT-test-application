package com.example.testapplication.ui.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentStartBinding


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class StartFragment : Fragment() {

    private val viewModel: StartViewModel by viewModels {
        StartViewModel.Factory(requireActivity().application)
    }

    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStartBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.apiRecyclerView.adapter = ServiceCardAdapter()
        binding.qrButton.setOnClickListener {
            findNavController().navigate(R.id.qrFragment)
        }
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onResume() {
        viewModel.getApiInformation()
        super.onResume()
    }
}