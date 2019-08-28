package com.snc.farmaccount.statistic


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.FragmentStatisticBinding

class StatisticFragment : Fragment() {

    private lateinit var binding: FragmentStatisticBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding  = FragmentStatisticBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }


}
