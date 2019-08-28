package com.snc.farmaccount.dialog


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.DialogCheckBinding

class CheckDialog : DialogFragment() {

    private lateinit var binding: DialogCheckBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogCheckBinding.inflate(inflater)
        binding.lifecycleOwner = this


        // Inflate the layout for this fragment
        return binding.root
    }


}
