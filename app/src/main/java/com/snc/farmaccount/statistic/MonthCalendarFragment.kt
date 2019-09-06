package com.snc.farmaccount.statistic


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.FragmentMonthCalendarBinding

class MonthCalendarFragment : Fragment() {

    private lateinit var binding: FragmentMonthCalendarBinding
    private val viewModel: StatisticViewModel by lazy {
        ViewModelProviders.of(this).get(StatisticViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMonthCalendarBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.getFirebase()

        binding.detailList.adapter = StatisticEventAdapter(StatisticEventAdapter.OnClickListener {

        }, viewModel)

        binding.totalList.adapter = StatisticEventAdapter(StatisticEventAdapter.OnClickListener {

        }, viewModel)

        return binding.root
    }


}
