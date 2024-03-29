package com.snc.farmaccount.statistic


import android.os.Bundle
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

class MonthCalendarFragment : Fragment() {

    private lateinit var binding: FragmentMonthCalendarBinding
    private val pieData = ArrayList<SliceValue>()
    private var sumEvent = ArrayList<SumEvent>()
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

        val parentViewModel = ViewModelProviders.of(this.requireParentFragment())
            .get(StatisticViewModel::class.java)

        parentViewModel.category.observe(this.requireParentFragment(), Observer {
            viewModel.categoryPress.value = it
        })

        binding.textMonth.text = title
        viewModel.currentMonth.value = title.substring(5,7)
        binding.spendHint.visibility = View.GONE
        viewModel.getCurrentMonth()
        viewModel.getEvent()

        binding.detailList.adapter = StatisticEventAdapter(StatisticEventAdapter.OnClickListener {

        }, viewModel)

        binding.totalList.adapter = StatisticCatagoryAdapter(StatisticCatagoryAdapter.OnClickListener {

        }, viewModel)

        parentViewModel.filter.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    binding.detailList.visibility = View.VISIBLE
                    binding.totalList.visibility = View.GONE
                    binding.dynamicArcView.visibility = View.GONE
                }

                else -> {
                    binding.detailList.visibility = View.GONE
                    binding.totalList.visibility = View.VISIBLE
                    binding.spendHint.visibility = View.GONE
                    binding.dynamicArcView.visibility = View.VISIBLE
                }
            }
        })

        viewModel.eventByCategory.observe(viewLifecycleOwner, Observer {
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
                        val percent= Math.round((viewModel.eventByEatPrice.value!!.toFloat()/total)*100)
                        pieData.add(SliceValue(viewModel.eventByEatPrice.value!!.toFloat(),
                            context!!.getColor(R.color.yellow)
                        ).setLabel(getString(R.string.eat_label)+"$percent"+getString(R.string.percent)))
                    }

                    sumEvent.add(SumEvent(R.drawable.cutlery,getString(R.string.catalog_eat),false,
                        viewModel.eventByEatPrice.value.toString()))
                }
            })

            viewModel.eventByClothPrice.observe(this, Observer { it ->
                it?.let {

                    if (it != 0) {
                        val percent= Math.round((viewModel.eventByClothPrice.value!!.toFloat()/total)*100)
                        pieData.add(SliceValue(viewModel.eventByClothPrice.value!!.toFloat(),
                            context!!.getColor(R.color.yellow_2)
                        ).setLabel(getString(R.string.cloth_label)+"$percent"+getString(R.string.percent)))
                    }

                    sumEvent.add(SumEvent(R.drawable.apron,getString(R.string.catalog_cloth),false,
                        viewModel.eventByClothPrice.value.toString()))
                }
            })

            viewModel.eventByLivePrice.observe(this, Observer { it ->
                it?.let {

                    if (it != 0) {
                        val percent= Math.round((viewModel.eventByLivePrice.value!!.toFloat()/total)*100)
                        pieData.add(SliceValue(viewModel.eventByLivePrice.value!!.toFloat(),
                            context!!.getColor(R.color.yellow_3)
                        ).setLabel(getString(R.string.live_label)+"$percent"+getString(R.string.percent)))
                    }

                    sumEvent.add(SumEvent(R.drawable.field,getString(R.string.catalog_live),false,
                        viewModel.eventByLivePrice.value.toString()))
                }
            })

            viewModel.eventByTrafficPrice.observe(this, Observer { it ->
                it?.let {

                    if (it != 0) {
                        val percent= Math.round((viewModel.eventByTrafficPrice.value!!.toFloat()/total)*100)
                        pieData.add(SliceValue(viewModel.eventByTrafficPrice.value!!.toFloat(),
                            context!!.getColor(R.color.yellow_4)
                        ).setLabel(getString(R.string.traffic_label)+"$percent"+getString(R.string.percent)))
                    }

                    sumEvent.add(SumEvent(R.drawable.tractor,getString(R.string.catalog_traffic),false,
                        viewModel.eventByTrafficPrice.value.toString()))
                }
            })

            viewModel.eventByFunPrice.observe(this, Observer { it ->
                it?.let {

                    if (it != 0) {
                        val percent= Math.round((viewModel.eventByFunPrice.value!!.toFloat()/total)*100)
                        pieData.add(SliceValue(viewModel.eventByFunPrice.value!!.toFloat(),
                            context!!.getColor(R.color.green)
                            ).setLabel(getString(R.string.fun_label)+"$percent"+getString(R.string.percent)))
                    }

                    sumEvent.add(SumEvent(R.drawable.kite,getString(R.string.catalog_fun),false,
                        viewModel.eventByFunPrice.value.toString()))
                }
            })

            viewModel.eventByIncomePrice.observe(this, Observer { it ->
                it?.let {
                    viewModel.sumEvent.value = sumEvent

                    if (it != 0) {
                        val percent= Math.round((viewModel.eventByIncomePrice.value!!.toFloat()/total)*100)
                        pieData.add(SliceValue(viewModel.eventByIncomePrice.value!!.toFloat(),
                            context!!.getColor(R.color.green_2)
                        ).setLabel(getString(R.string.income_label)+"$percent"+getString(R.string.percent)))
                    }

                    sumEvent.add(SumEvent(R.drawable.notes,getString(R.string.catalog_income),true,
                        viewModel.eventByIncomePrice.value.toString()))
                    pieChart()
                }
            })

            if (price == 0) {
                pieData.add(SliceValue(100f, context!!.getColor(R.color.gray)).setLabel(""))
            }
        })

        return binding.root
    }


    private fun pieChart() {
        val pieChartData = PieChartData(pieData)
        val expand = total - 2*viewModel.eventByIncomePrice.value!!

        pieChartData.setHasLabels(true).valueLabelTextSize = 14
        pieChartData.valueLabelBackgroundColor = resources.getColor(R.color.expend_title)
        pieChartData.setHasCenterCircle(true)
        pieChartData.centerCircleScale = 0.5F
        pieChartData.centerText2Typeface = resources.getFont(R.font.gen_normal)
        pieChartData.centerText1Typeface = resources.getFont(R.font.gen_normal)
        pieChartData.centerText1 = "$$expand"
        pieChartData.centerText1FontSize = 28
        pieChartData.centerText1Color = resources.getColor(R.color.expend_title)
        binding.dynamicArcView.isChartRotationEnabled = false
        binding.dynamicArcView.pieChartData = pieChartData
    }

    fun updateCalendar() {
    }

}
