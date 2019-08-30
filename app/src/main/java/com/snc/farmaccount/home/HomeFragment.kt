package com.snc.farmaccount.home


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.snc.farmaccount.NavigationListener
import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.FragmentHomeBinding
import com.snc.farmaccount.helper.Format
import java.text.SimpleDateFormat
import java.util.*
import android.app.DatePickerDialog
import android.widget.Toast


class HomeFragment : Fragment(),NavigationListener{

    private lateinit var binding: FragmentHomeBinding
    private val PREFILLED_DAYS = 251
    private var defaultDailyPage = 0
    private var todayDayCode = ""
    private var currentDayCode = ""



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
        currentDayCode= viewModel.DATE_MODE
        todayDayCode = viewModel.DATE_MODE

        binding.dayViewpager.id = (System.currentTimeMillis() % 100000).toInt()
        setViewPager()
        refreshEvents()

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

        binding.buttonDatePicker.setOnClickListener {
            showDialog()
        }

        binding.buttonDateToday.setOnClickListener {
            goToToday()
        }

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

    private fun goToToday() {
        currentDayCode = todayDayCode
        setViewPager()
    }

    private fun showDialog() {
        val calendar = Calendar.getInstance()
        val simpledateformat = SimpleDateFormat("EEEE")
        val datePickdialog = DatePickerDialog(
            this.context!!, AlertDialog.THEME_HOLO_LIGHT,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                // Display Selected date in Toast
                val date = Date(year, month, day+3)
                val dayOfWeek = simpledateformat.format(date)
                currentDayCode = "$year.${month+1}.$day ($dayOfWeek)"
                setViewPager()
                Log.i("Sophie_date","$year.$month.$day ($dayOfWeek)")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))
        datePickdialog.show()
    }

    private fun refreshEvents() {
        (binding.dayViewpager.adapter as? DayViewPagerAdapter)?.updateCalendars(binding.dayViewpager.currentItem)
    }

}
