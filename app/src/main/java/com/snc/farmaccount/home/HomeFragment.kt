package com.snc.farmaccount.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTabHost
import androidx.navigation.fragment.findNavController
import com.snc.farmaccount.DataBinderMapperImpl

import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.buttonDatePicker.setOnClickListener {
            findNavController()
                .navigate(HomeFragmentDirections.actionGlobalDatePickerDialog())
        }

        binding.buttonBudget.setOnClickListener {
            findNavController()
                .navigate(HomeFragmentDirections.actionGlobalBudgetFragment())
        }

        binding.buttonStatistic.setOnClickListener {
            findNavController()
                .navigate(HomeFragmentDirections.actionGlobalStatisticFragment())
        }

        binding.buttonScan.setOnClickListener {
            findNavController()
                .navigate(HomeFragmentDirections.actionGlobalQrCodeFragment())
        }

        binding.imageAddEvent.setOnClickListener {
            findNavController()
                .navigate(HomeFragmentDirections.actionGlobalAddEventFragment())
        }

        binding.textBudgetEdit.setOnClickListener{
            findNavController()
                .navigate(HomeFragmentDirections.actionGlobalDetailFragment())
        }

        // Inflate the layout for this fragment
        return binding.root
    }

}
