package com.snc.farmaccount.statistic


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.SumEvent

import com.snc.farmaccount.databinding.FragmentMonthCalendarBinding
import com.snc.farmaccount.helper.NavigationListener

class MonthCalendarFragment : Fragment() {



    private lateinit var binding: FragmentMonthCalendarBinding
    var navListener: NavigationListener?= null
    var sumEvent = ArrayList<SumEvent>()
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
        viewModel.currentMonth.value = title.substring(5,7)
        viewModel.getCurrentMonth()
        viewModel.getFirebase()

        binding.detailList.adapter = StatisticEventAdapter(StatisticEventAdapter.OnClickListener {

        }, viewModel)

        viewModel.eventByCatagory.observe(this, Observer { it ->
            Log.i("Sophie_eat", "$it")
            it?.let {
                (binding.detailList.adapter as StatisticEventAdapter).submitList(it)
            }
        })

        viewModel.eventByEatPrice.observe(this, Observer {
            it?.let {
                sumEvent.add(SumEvent(R.drawable.tag_eat,getString(R.string.catalog_eat),viewModel.eventByEatPrice.value.toString()))
                Log.i("Sophie_eat", "$sumEvent")
            }
        })

        viewModel.eventByClothPrice.observe(this, Observer {
            Log.i("Sophie_cloth", "$it")
            it?.let {
                sumEvent.add(SumEvent(R.drawable.tag_cloth,getString(R.string.catalog_cloth),viewModel.eventByClothPrice.value.toString()))
            }
        })

        viewModel.eventByLivePrice.observe(this, Observer {
            Log.i("Sophie_live", "$it")
            it?.let {
                sumEvent.add(SumEvent(R.drawable.tag_live,getString(R.string.catalog_live),viewModel.eventByLivePrice.value.toString()))
            }
        })

        viewModel.eventByTrafficPrice.observe(this, Observer {
            Log.i("Sophie_traffic", "$it")
            it?.let {
                sumEvent.add(SumEvent(R.drawable.tag_traffic,getString(R.string.catalog_traffic),viewModel.eventByTrafficPrice.value.toString()))
            }
        })

        viewModel.eventByFunPrice.observe(this, Observer {
            Log.i("Sophie_fun", "$it")
            it?.let {
                sumEvent.add(SumEvent(R.drawable.tag_fun,getString(R.string.catalog_fun),viewModel.eventByFunPrice.value.toString()))
            }
        })

        viewModel.eventByIncomePrice.observe(this, Observer {
            Log.i("Sophie_income", "$it")
            it?.let {
                sumEvent.add(SumEvent(R.drawable.tag_income,getString(R.string.catalog_income),viewModel.eventByIncomePrice.value.toString()))
                viewModel.sumEvent.value = sumEvent
                Log.i("Sophie_sumEvent", "${viewModel.sumEvent.value}")
            }
        })

        binding.totalList.adapter = StatisticCatagoryAdapter(StatisticCatagoryAdapter.OnClickListener {

        }, viewModel)

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
