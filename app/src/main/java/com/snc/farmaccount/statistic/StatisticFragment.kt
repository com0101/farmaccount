package com.snc.farmaccount.statistic


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.snc.farmaccount.ApplicationContext
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.StatisticCatalog
import com.snc.farmaccount.databinding.FragmentStatisticBinding
import com.snc.farmaccount.helper.Format
import kotlinx.android.synthetic.main.fragment_home.*


class StatisticFragment : Fragment() {

    private lateinit var binding: FragmentStatisticBinding
    var tag = ArrayList<StatisticCatalog>()
    private val PREFILLED_MONTHS = 251
    private var defaultDailyPage = 0
    private var currentDayCode = ""
    var RESPONSE_EVALUATE = "response_evaluate"

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
            viewModel.catagory.value = it
            val params =  binding.imageArrowRight.layoutParams as ConstraintLayout.LayoutParams
            val paramsleft =  binding.imageArrowLeft.layoutParams as ConstraintLayout.LayoutParams

            when(it.name){
                "總覽" -> {
                    viewModel.filter.value = false
                    params.setMargins(0, 281.dpToPx(),27.dpToPx(),0)
                    paramsleft.setMargins(25.dpToPx(), 281.dpToPx(),0,0)
                    binding.imageArrowRight.requestLayout()
                    binding.imageArrowLeft.requestLayout()
                }
                else -> {
                    viewModel.filter.value = true
                    params.setMargins(0,15.dpToPx(),27.dpToPx(),0)
                    paramsleft.setMargins(25.dpToPx(),15.dpToPx(),0,0)
                    binding.imageArrowRight.requestLayout()
                    binding.imageArrowLeft.requestLayout()
                }
            }
            Log.i("Sophie_taglist", "${viewModel.catagory.value}")
        })

        binding.imageBackState.setOnClickListener {
            findNavController()
                .navigate(StatisticFragmentDirections.actionGlobalHomeFragment())
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 做你要的事，這邊是跳轉首頁
                findNavController().
                    navigate(R.id.action_global_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        statisticTag()
        setViewPager()
        refreshEvents()
        arrowButtons()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setViewPager() {
        val codes = getMonths(currentDayCode)
        val monthlyAdapter = MonthViewPagerAdapter(childFragmentManager, codes, viewModel)
        defaultDailyPage = codes.size / 2

        binding.dayViewpager.apply {
            adapter = monthlyAdapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    Log.i("Sophie_position","$positionOffset+${position}")
                    if (position==125 && positionOffset>0) {
                        binding.dayViewpager.setCurrentItem(125, true)
//                        binding.dayViewpager.beginFakeDrag()
                    }
                }

                override fun onPageSelected(position: Int) {
                    currentDayCode = codes[position]
                    if (position == 125) {
                        binding.imageArrowRight.visibility = View.GONE

                    } else {
                        binding.imageArrowRight.visibility = View.VISIBLE

                    }

                }
            })
            currentItem = defaultDailyPage
            Log.i("Sophie_current", "$currentItem + $defaultDailyPage")
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

    private fun arrowButtons(){

        binding.imageArrowRight.setOnClickListener {
            binding.dayViewpager.currentItem = binding.dayViewpager.currentItem + 1
        }

        binding.imageArrowLeft.setOnClickListener {
            binding.dayViewpager.currentItem = binding.dayViewpager.currentItem - 1
        }

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

    private fun Number.dpToPx(): Int {
        return (this.toFloat() * (ApplicationContext.applicationContext().resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }
}
