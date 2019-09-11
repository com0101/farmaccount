package com.snc.farmaccount.statistic


import android.app.Activity
import android.content.Intent
import android.graphics.Color.DKGRAY
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.StatisticCatalog
import com.snc.farmaccount.`object`.SumEvent
import com.snc.farmaccount.databinding.FragmentMonthCalendarBinding
import com.snc.farmaccount.helper.NavigationListener





class MonthCalendarFragment : Fragment() {

    private lateinit var binding: FragmentMonthCalendarBinding
    var sumEvent = ArrayList<SumEvent>()
    var date = ""
    var title = ""
    var RESPONSE = "response"
    var EVALUATE_DIALOG = "evaluate_dialog"
    var REQUEST_EVALUATE = 0X110

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

        val parentViewModel = ViewModelProviders.of(this.requireParentFragment()).get(StatisticViewModel::class.java)

        parentViewModel.catagory.observe(this.requireParentFragment(), Observer {
            viewModel.catagoryMap.value = it
        })

        binding.textMonth.text = title
        viewModel.currentMonth.value = title.substring(5,7)
        viewModel.getCurrentMonth()
        viewModel.getFirebase()

        binding.detailList.adapter = StatisticEventAdapter(StatisticEventAdapter.OnClickListener {

        }, viewModel)

        parentViewModel.filter.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.detailList.visibility = View.VISIBLE
                binding.totalList.visibility = View.GONE
            } else {
                binding.detailList.visibility = View.GONE
                binding.totalList.visibility = View.VISIBLE
            }
        })

        viewModel.eventByCatagory.observe(viewLifecycleOwner, Observer {
            Log.i("Sophie_Catagory", "$it")
            (binding.detailList.adapter as StatisticEventAdapter).submitList(it)
            if (it.isEmpty()) {
                binding.spendHint.visibility = View.VISIBLE
            } else {
                binding.spendHint.visibility = View.GONE
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
                pieChart()
            }
        })

        binding.totalList.adapter = StatisticCatagoryAdapter(StatisticCatagoryAdapter.OnClickListener {

        }, viewModel)

        return binding.root
    }


    private fun pieChart() {
        binding.pieChart.setUsePercentValues(true)
        val y = ArrayList<Entry>()
        y.add(Entry(viewModel.eventByEatPrice.value!!.toFloat(), 0))
        y.add(Entry(viewModel.eventByClothPrice.value!!.toFloat(), 1))
        y.add(Entry(viewModel.eventByLivePrice.value!!.toFloat(), 2))
        y.add(Entry(viewModel.eventByTrafficPrice.value!!.toFloat(), 3))
        y.add(Entry(viewModel.eventByFunPrice.value!!.toFloat(), 4))
        y.add(Entry(viewModel.eventByIncomePrice.value!!.toFloat(), 5))


        val dataSet = PieDataSet(y, "month")
        val x = ArrayList<String>()

        x.add(getString(R.string.catalog_eat))
        x.add(getString(R.string.catalog_cloth))
        x.add(getString(R.string.catalog_live))
        x.add(getString(R.string.catalog_traffic))
        x.add(getString(R.string.catalog_fun))
        x.add(getString(R.string.catalog_income))
        val data = PieData(x, dataSet)
        // In Percentage term
        data.setValueFormatter(PercentFormatter())

        val colors = ArrayList<Int>()
        colors.add(resources.getColor(R.color.md_blue_100))
        colors.add(resources.getColor(R.color.colorPrimary))
        colors.add(resources.getColor(R.color.gray_green))
        colors.add(resources.getColor(R.color.md_yellow_100))
        colors.add(resources.getColor(R.color.md_yellow_300))
        colors.add(resources.getColor(R.color.md_yellow_400))
        dataSet.colors = colors
        // Default value
        //data.setValueFormatter(new DefaultValueFormatter(0));
        binding.pieChart.data = data
        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.transparentCircleRadius = 10f
        binding.pieChart.holeRadius = 30f

        data.setValueTextSize(13f)
        data.setValueTextColor(DKGRAY)
//        binding.pieChart.setOnChartValueSelectedListener(this)

        binding.pieChart.animateXY(1400, 1400)
    }

    fun updateCalendar() {
//        val startTimes = Format.getDayStartTS(date)
//        val endTimes = Format.getDayEndTS(date)
//        context?.eventsHelper?.getEvents(startTS, endTS) {
//            receivedEvents(it)
//        }
    }

}
