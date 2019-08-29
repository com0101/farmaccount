package com.snc.farmaccount.home


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.snc.farmaccount.NavigationListener

import com.snc.farmaccount.databinding.FragmentDayCalendarBinding
import com.snc.farmaccount.helper.Format
import java.util.*

class DayCalendarFragment : Fragment() {

    private lateinit var binding: FragmentDayCalendarBinding
    var navListener: NavigationListener?= null
    private var hash = 0
    var date = ""
    var title = ""

    private val viewModel: DayViewModel by lazy {
        ViewModelProviders.of(this).get(DayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDayCalendarBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.textDate.text = title
        arrowButtons()
        Log.i("Sophie_date_observe","${viewModel.date.value}")
        return binding.root
    }

    private fun arrowButtons(){

        binding.imageArrowRight.setOnClickListener {
            navListener?.goRight()
            Toast.makeText(context, "right", Toast.LENGTH_SHORT).show()
        }

        binding.imageArrowLeft.setOnClickListener {
            navListener?.goLeft()
            Toast.makeText(context, "left", Toast.LENGTH_SHORT).show()

        }

//        val day = Format.getDayTitle(context!!, date)
//        binding.textDate.text = viewModel.date.value

    }

    fun updateCalendar() {
//        val startTimes = Format.getDayStartTS(date)
//        val endTimes = Format.getDayEndTS(date)
//        context?.eventsHelper?.getEvents(startTS, endTS) {
//            receivedEvents(it)
//        }
    }

}
