package com.snc.farmaccount.statistic

import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.snc.farmaccount.helper.DAY_CODE
import com.snc.farmaccount.helper.NavigationListener
import com.snc.farmaccount.home.DayCalendarFragment
import com.snc.farmaccount.home.DayViewModel

class MonthViewPagerAdapter
    (fragmentManager: FragmentManager,
     private val dayCodes: List<String>,
     var viewModel: StatisticViewModel
) :
    FragmentStatePagerAdapter(fragmentManager) {
    private val homeFragment = SparseArray<MonthCalendarFragment>()

    override fun getCount() = dayCodes.size

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        val code = dayCodes[position]
        bundle.putString(DAY_CODE, code)


        val fragment = MonthCalendarFragment()
        fragment.arguments = bundle
        fragment.title = "${bundle["day_code"]}".substring(0,7)
//        Log.i("Sophie_bundle", "${bundle["day_code"]}")

        homeFragment.put(position, fragment)
        return fragment
    }

    fun updateCalendars(pos: Int) {
        for (i in -1..1) {
            homeFragment[pos + i]?.updateCalendar()
        }
    }
}
