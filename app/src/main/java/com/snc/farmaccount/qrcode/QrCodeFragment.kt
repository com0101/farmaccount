package com.snc.farmaccount.qrcode


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.FragmentQrCodeBinding

class QrCodeFragment : Fragment() {

    private lateinit var binding: FragmentQrCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentQrCodeBinding.inflate(inflater)


        // Inflate the layout for this fragment
        return binding.root
    }


}
