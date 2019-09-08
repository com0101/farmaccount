package com.snc.farmaccount.statistic


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager

import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.StatisticCatalog
import com.snc.farmaccount.databinding.FragmentStatisticBinding
import com.snc.farmaccount.helper.Format
import com.snc.farmaccount.helper.NavigationListener
import com.snc.farmaccount.home.DayViewModel
import com.snc.farmaccount.home.DayViewPagerAdapter

class StatisticFragment : Fragment(), NavigationListener {

    private lateinit var binding: FragmentStatisticBinding
    var tag = ArrayList<StatisticCatalog>()
    private val PREFILLED_MONTHS = 251
    private var defaultDailyPage = 0
    private var todayDayCode = ""
    private var currentDayCode = ""

    private val viewModel: StatisticViewModel by lazy {
        ViewModelProviders.of(this).get(StatisticViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding  = FragmentStatisticBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        currentDayCode= viewModel.DATE_MODE
        binding.tagList.adapter = StatisticTagAdapter(tag,StatisticTagAdapter.OnClickListener {

        })


        binding.imageBackState.setOnClickListener {
            findNavController()
                .navigate(StatisticFragmentDirections.actionGlobalHomeFragment())
        }

        statisticTag()
        setViewPager()
        refreshEvents()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setViewPager() {
        val codes = getMonths(currentDayCode)
        val monthlyAdapter = MonthViewPagerAdapter(childFragmentManager, codes, this,viewModel)
        defaultDailyPage = codes.size / 2

        binding.dayViewpager.apply {
            adapter = monthlyAdapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    currentDayCode = codes[position]
                    Log.i("Sophie_position","$currentDayCode")
                }
            })
            currentItem = defaultDailyPage
        }
    }

    private fun getMonths(code: String): List<String> {
        val months = ArrayList<String>(PREFILLED_MONTHS)
        val today = Format.getDateTimeFromCode(code).withDayOfMonth(1)
        for (i in -PREFILLED_MONTHS / 2..PREFILLED_MONTHS / 2) {
            months.add(Format.getDayCodeFromDateTime(today.plusMonths(i)))
        }
        return months
    }

    override fun goLeft() {
        binding.dayViewpager.currentItem = binding.dayViewpager.currentItem - 1
    }

    override fun goRight() {
        binding.dayViewpager.currentItem = binding.dayViewpager.currentItem + 1
    }

    private fun refreshEvents() {
        (binding.dayViewpager.adapter as? MonthViewPagerAdapter)?.updateCalendars(binding.dayViewpager.currentItem)
    }

    private fun statisticTag() {
       tag.add(StatisticCatalog(getString(R.string.statistic_tag)))
       tag.add(StatisticCatalog(getString(R.string.catalog_eat)))
       tag.add(StatisticCatalog(getString(R.string.catalog_cloth)))
       tag.add(StatisticCatalog(getString(R.string.catalog_live)))
       tag.add(StatisticCatalog(getString(R.string.catalog_traffic)))
       tag.add(StatisticCatalog(getString(R.string.catalog_fun)))
       tag.add(StatisticCatalog(getString(R.string.catalog_income)))
    }

}
