package com.snc.farmaccount.dialog


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.DialogDatePickerBinding

class DatePickerDialog : DialogFragment() {

    private lateinit var binding: DialogDatePickerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogDatePickerBinding.inflate(inflater)
        binding.lifecycleOwner = this
        // Inflate the layout for this fragment
        return binding.root
    }


}
