package com.example.testapplication.ui.licenses

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.testapplication.R
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class LicensesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        startActivity(Intent(context, OssLicensesMenuActivity::class.java))
        findNavController().navigate(R.id.navigation_service)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_licenses, container, false)
    }
}
