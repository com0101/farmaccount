package com.snc.farmaccount.statistic


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.FragmentStatisticBinding

class StatisticFragment : Fragment() {

    private lateinit var binding: FragmentStatisticBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding  = FragmentStatisticBinding.inflate(inflater)
        binding.lifecycleOwner = this

         binding.imageBackState.setOnClickListener {
            findNavController()
                .navigate(StatisticFragmentDirections.actionGlobalHomeFragment())
        }
        // Inflate the layout for this fragment
        return binding.root
    }


}
