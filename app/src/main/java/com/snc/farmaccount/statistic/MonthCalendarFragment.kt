package com.snc.farmaccount.statistic


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.`object`.SumEvent
import com.snc.farmaccount.databinding.FragmentMonthCalendarBinding
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import kotlin.math.roundToInt

class MonthCalendarFragment : Fragment() {

    private lateinit var binding: FragmentMonthCalendarBinding
    var sumEvent = ArrayList<SumEvent>()
    val pieData = ArrayList<SliceValue>()
    var date = ""
    var title = ""
    var total = 0



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
                binding.spendHint.visibility = View.GONE
            }
        })

        viewModel.eventByCatagory.observe(viewLifecycleOwner, Observer {
            (binding.detailList.adapter as StatisticEventAdapter).submitList(it)
            when (it) {
                null -> binding.spendHint.visibility = View.VISIBLE
                emptyList<Event>() -> binding.spendHint.visibility = View.VISIBLE
                else -> binding.spendHint.visibility = View.GONE
            }
        })

        viewModel.eventByTotalPrice.observe(this, Observer { price ->
            total = price
            viewModel.eventByEatPrice.observe(this, Observer { it ->
                it?.let {
                    if (it != 0) {
                        var percent=Math.round((viewModel.eventByEatPrice.value!!.toFloat()/total)*100)
                        pieData.add(SliceValue(viewModel.eventByEatPrice.value!!.toFloat(), Color.argb(100,253,104,57)).setLabel("食: $percent %"))

                    }
                    sumEvent.add(SumEvent(R.drawable.tag_eat,getString(R.string.catalog_eat),viewModel.eventByEatPrice.value.toString()))
                }
            })

            viewModel.eventByClothPrice.observe(this, Observer { it ->
                Log.i("Sophie_cloth", "$it")
                it?.let {
                    sumEvent.add(SumEvent(R.drawable.tag_cloth,getString(R.string.catalog_cloth),viewModel.eventByClothPrice.value.toString()))
                    if (it != 0) {
                        var percent=Math.round((viewModel.eventByClothPrice.value!!.toFloat()/total)*100)
                        pieData.add(SliceValue(viewModel.eventByClothPrice.value!!.toFloat(), Color.argb(100,255,152,0)).setLabel("衣: $percent %"))
                    }
                }
            })

            viewModel.eventByLivePrice.observe(this, Observer { it ->
                Log.i("Sophie_live", "$it")
                it?.let {
                    sumEvent.add(SumEvent(R.drawable.tag_live,getString(R.string.catalog_live),viewModel.eventByLivePrice.value.toString()))
                    if (it != 0) {
                        var percent=Math.round((viewModel.eventByLivePrice.value!!.toFloat()/total)*100)
                        pieData.add(SliceValue(viewModel.eventByLivePrice.value!!.toFloat(), Color.argb(100,255,193,7)).setLabel("住: $percent %"))
                    }
                }
            })

            viewModel.eventByTrafficPrice.observe(this, Observer { it ->
                Log.i("Sophie_traffic", "$it")
                it?.let {
                    sumEvent.add(SumEvent(R.drawable.tag_traffic,getString(R.string.catalog_traffic),viewModel.eventByTrafficPrice.value.toString()))
                    if (it != 0) {
                        var percent=Math.round((viewModel.eventByTrafficPrice.value!!.toFloat()/total)*100)
                        pieData.add(SliceValue(viewModel.eventByTrafficPrice.value!!.toFloat(), Color.argb(100,255,235,59)).setLabel("行: $percent %"))
                    }
                }
            })

            viewModel.eventByFunPrice.observe(this, Observer { it ->
                Log.i("Sophie_fun", "$it")
                it?.let {
                    sumEvent.add(SumEvent(R.drawable.tag_fun,getString(R.string.catalog_fun),viewModel.eventByFunPrice.value.toString()))
                    if (it != 0) {
                        var percent=Math.round((viewModel.eventByFunPrice.value!!.toFloat()/total)*100)
                        pieData.add(SliceValue(viewModel.eventByFunPrice.value!!.toFloat(), Color.argb(100,205,220,57)).setLabel("樂: $percent %"))
                    }
                }
            })

            viewModel.eventByIncomePrice.observe(this, Observer { it ->
                Log.i("Sophie_income", "$it")
                it?.let {
                    sumEvent.add(SumEvent(R.drawable.tag_income,getString(R.string.catalog_income),viewModel.eventByIncomePrice.value.toString()))
                    viewModel.sumEvent.value = sumEvent
                    if (it != 0) {
                        var percent=Math.round((viewModel.eventByIncomePrice.value!!.toFloat()/total)*100)
                        pieData.add(SliceValue(viewModel.eventByIncomePrice.value!!.toFloat(), Color.argb(100,139,195,74)).setLabel("收入: $percent %"))
                    }
                    pieChart()
                }
            })
        })
        binding.totalList.adapter = StatisticCatagoryAdapter(StatisticCatagoryAdapter.OnClickListener {

        }, viewModel)

        return binding.root
    }


    private fun pieChart() {
        val pieChartData = PieChartData(pieData)
        pieChartData.setHasLabels(true).valueLabelTextSize = 14
        pieChartData.setHasCenterCircle(true)
        binding.dynamicArcView.pieChartData = pieChartData
    }

    fun updateCalendar() {
//        val startTimes = Format.getDayStartTS(date)
//        val endTimes = Format.getDayEndTS(date)
//        context?.eventsHelper?.getEvents(startTS, endTS) {
//            receivedEvents(it)
//        }
    }

}
