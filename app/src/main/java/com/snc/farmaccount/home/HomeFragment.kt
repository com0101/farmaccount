package com.snc.farmaccount.home


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
import com.snc.farmaccount.NavigationListener
import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.FragmentHomeBinding
import com.snc.farmaccount.helper.Format
import org.joda.time.DateTime
import java.util.ArrayList

class HomeFragment : Fragment(),NavigationListener{

    private lateinit var binding: FragmentHomeBinding
    private val PREFILLED_DAYS = 251
    private var defaultDailyPage = 0
    private var todayDayCode = ""
    private var currentDayCode = ""
    private var isGoToTodayVisible = false

    private val viewModel: DayViewModel by lazy {
        ViewModelProviders.of(this).get(DayViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        currentDayCode=  viewModel.DATE_MODE
        todayDayCode = viewModel.DATE_MODE

        binding.dayViewpager.id = (System.currentTimeMillis() % 100000).toInt()

        binding.buttonDatePicker.setOnClickListener {
            findNavController()
                .navigate(R.id.action_global_datePickerDialog)
        }

        binding.buttonBudget.setOnClickListener {
            findNavController()
                .navigate(R.id.action_global_budgetFragment)
        }

        binding.buttonStatistic.setOnClickListener {
            findNavController()
                .navigate(R.id.action_global_statisticFragment)
        }

        binding.buttonScan.setOnClickListener {
            findNavController()
                .navigate(R.id.action_global_qrCodeFragment)
        }
        setViewPager()
        refreshEvents()


        return binding.root
    }


    private fun setViewPager() {
        val codes = getDays(currentDayCode)
        val dailyAdapter = DayViewPagerAdapter(childFragmentManager, codes, this,viewModel)
        defaultDailyPage = codes.size / 2

        binding.dayViewpager.apply {
            adapter = dailyAdapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    currentDayCode = codes[position]
                    viewModel.date.value = currentDayCode
                    Log.i("Sophie_position","${viewModel.date.value}")
//                    val shouldGoToTodayBeVisible = shouldGoToTodayBeVisible()
//                    if (isGoToTodayVisible != shouldGoToTodayBeVisible) {
//                        (activity as? MainActivity)?.toggleGoToTodayVisibility(shouldGoToTodayBeVisible)
//                        isGoToTodayVisible = shouldGoToTodayBeVisible
//                    }
                }
            })
            currentItem = defaultDailyPage
        }
    }

    private fun getDays(code: String): List<String> {
        val days = ArrayList<String>(PREFILLED_DAYS)
        val today = Format.getDateTimeFromCode(code)
        for (i in -PREFILLED_DAYS / 2..PREFILLED_DAYS / 2) {
            days.add(Format.getDayCodeFromDateTime(today.plusDays(i)))
        }
        return days
    }

    override fun goLeft() {
        binding.dayViewpager.currentItem = binding.dayViewpager.currentItem - 1
    }

    override fun goRight() {
        binding.dayViewpager.currentItem = binding.dayViewpager.currentItem + 1
    }

    override fun goToDateTime(dateTime: DateTime) {
        currentDayCode = Format.getDayCodeFromDateTime(dateTime)
        setViewPager()
    }

//    override fun goToToday() {
//        currentDayCode = todayDayCode
//        setViewPager()
//    }

    private fun refreshEvents() {
        (binding.dayViewpager.adapter as? DayViewPagerAdapter)?.updateCalendars(binding.dayViewpager.currentItem)
    }


//    override fun shouldGoToTodayBeVisible() = currentDayCode != todayDayCode




}
