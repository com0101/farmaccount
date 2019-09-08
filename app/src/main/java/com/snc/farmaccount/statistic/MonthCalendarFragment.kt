package com.snc.farmaccount.statistic


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.snc.farmaccount.databinding.FragmentMonthCalendarBinding
import com.snc.farmaccount.helper.NavigationListener

class MonthCalendarFragment : Fragment() {



    private lateinit var binding: FragmentMonthCalendarBinding
    var navListener: NavigationListener?= null
    var date = ""
    var title = ""

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
        binding.textMonth.text = title
        viewModel.getFirebase()

        binding.detailList.adapter = StatisticEventAdapter(StatisticEventAdapter.OnClickListener {

        }, viewModel)

        binding.totalList.adapter = StatisticEventAdapter(StatisticEventAdapter.OnClickListener {

        }, viewModel)

        viewModel.eventByCatagory.observe(this, Observer { it ->
            Log.i("Sophie_eat", "$it")
            it?.let {
                (binding.detailList.adapter as StatisticEventAdapter).submitList(it)
            }
        })
        arrowButtons()
        return binding.root
    }

    private fun arrowButtons(){

        binding.imageArrowRight.setOnClickListener {
            navListener?.goRight()
        }

        binding.imageArrowLeft.setOnClickListener {
            navListener?.goLeft()
        }

    }

    fun updateCalendar() {
//        val startTimes = Format.getDayStartTS(date)
//        val endTimes = Format.getDayEndTS(date)
//        context?.eventsHelper?.getEvents(startTS, endTS) {
//            receivedEvents(it)
//        }
    }

}
