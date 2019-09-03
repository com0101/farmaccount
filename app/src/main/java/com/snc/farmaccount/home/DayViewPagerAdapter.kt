package com.snc.farmaccount.home

import android.os.Bundle
import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.snc.farmaccount.helper.NavigationListener
import com.snc.farmaccount.helper.DAY_CODE


class DayViewPagerAdapter
    (fragmentManager: FragmentManager,
     private val dayCodes: List<String>,
     private val navListener: NavigationListener,
     var viewModel: DayViewModel) :
    FragmentStatePagerAdapter(fragmentManager) {
    private val homeFragment = SparseArray<DayCalendarFragment>()

    override fun getCount() = dayCodes.size

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        val code = dayCodes[position]
        bundle.putString(DAY_CODE, code)


        val fragment = DayCalendarFragment()
        fragment.arguments = bundle
        fragment.navListener = navListener
        fragment.title = "${bundle["day_code"]}"

        homeFragment.put(position, fragment)
        return fragment
    }

    fun updateCalendars(pos: Int) {
        for (i in -1..1) {
            homeFragment[pos + i]?.updateCalendar()
        }
    }
}
