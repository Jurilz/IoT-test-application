package com.example.testapplication.ui.qr


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.testapplication.databinding.FragmentQrBinding
import com.example.testapplication.repository.QrRepository
import com.example.testapplication.utility.viewModelFactories
import com.google.zxing.integration.android.IntentIntegrator


/**
 * A simple [Fragment] subclass.

 * create an instance of this fragment.
 */
class QrFragment : Fragment() {

    private val qrViewModel: QrViewModel by viewModelFactories {
        QrViewModel(QrRepository(requireContext()))
    }

    private lateinit var binding: FragmentQrBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentQrBinding.inflate(inflater)
        binding.viewModel = qrViewModel
        binding.lifecycleOwner = this

        IntentIntegrator.forSupportFragment(this).initiateScan()

        binding.barCodeView.decodeSingle { result ->
                    run {
                        binding.barCodeView.setStatusText(result.text)
                    }
        }
        return binding.root
    }

    override fun onResume() {
        binding.barCodeView.resume()
        super.onResume()
    }

    override fun onPause() {
        binding.barCodeView.pause()
        super.onPause()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        val result =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this.context, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this.context, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                qrViewModel.setUrl(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}