package com.snc.farmaccount.home


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.snc.farmaccount.helper.NavigationListener
import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.FragmentHomeBinding
import com.snc.farmaccount.helper.Format
import java.text.SimpleDateFormat
import java.util.*
import android.app.DatePickerDialog
import android.app.Dialog
import android.view.animation.TranslateAnimation
import com.snc.farmaccount.MainViewModel
import com.snc.farmaccount.databinding.DialogCheckBinding
import com.snc.farmaccount.event.EditEventFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.content.Intent
import com.snc.farmaccount.MainActivity


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val PREFILLED_DAYS = 251
    private var defaultDailyPage = 0
    private var todayDayCode = ""
    private var currentDayCode = ""



    private val viewModel: DayViewModel by lazy {
        ViewModelProviders.of(this).get(DayViewModel::class.java)
    }
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
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
//        mainViewModel.getCircle()
        binding.dayViewpager.id = (System.currentTimeMillis() % 100000).toInt()
        setViewPager()
        refreshEvents()
        arrowButtons()
        viewModel.getOverage()
        var buttonSense = false

        mainViewModel.activityRestart.observe(this, androidx.lifecycle.Observer {
            it.let {
                restart()
            }
        })

        binding.buttonSetting.setOnClickListener {
            buttonSense = if (!buttonSense) {
                binding.buttonBudget.animate().translationY(180f).start() // move away
                binding.buttonStatistic.animate().translationY(360f).start()
                binding.buttonScan.animate().translationY(540f).start()
                true
            } else {
                binding.buttonBudget.animate().translationY(0f).start()
                binding.buttonStatistic.animate().translationY(0f).start()
                binding.buttonScan.animate().translationY(0f).start()
                false
            }
        }

        binding.buttonBudget.setOnClickListener {
            Log.i("Sophie_click","click")
            findNavController()
                .navigate(R.id.action_global_budgetFragment)
        }

        binding.buttonStatistic.setOnClickListener {
            Log.i("Sophie_click","click")
            findNavController()
                .navigate(R.id.action_global_statisticFragment)
        }

        binding.buttonScan.setOnClickListener {
            Log.i("Sophie_click","click")
            findNavController()
                .navigate(R.id.action_global_qrCodeFragment)
        }

        binding.imageAddEvent.setOnClickListener {
            findNavController()
                .navigate(R.id.action_global_addEventFragment)
        }

        binding.buttonDatePicker.setOnClickListener {
            showDialog()
        }

        binding.buttonDateToday.setOnClickListener {
            goToToday()
        }

        viewModel.overagePrice.observe(this, androidx.lifecycle.Observer {
            binding.textBudget.text = it
        })

        viewModel.date.observe(this, androidx.lifecycle.Observer {

        })

        viewModel.postPrice.observe(this, androidx.lifecycle.Observer {
            Log.i("Sophie_farm", "$it")
            viewModel.farmStatus.observe(this, androidx.lifecycle.Observer { status ->
                if (status == 0) {
                    when {
                        it > 1000 -> binding.imageFarm.setBackgroundResource(R.drawable.simple1)
                        it < 0 -> {
                            binding.imageFarm.setBackgroundResource(R.drawable.simple3)
                            showWarning()
                        }
                        else -> binding.imageFarm.setBackgroundResource(R.drawable.simple2)
                    }
                }
                if (status == 1) {
                    when {
                        it > 1000 -> binding.imageFarm.setBackgroundResource(R.drawable.middle)
                        it < 0 -> {
                            binding.imageFarm.setBackgroundResource(R.drawable.middle3)
                            showWarning()
                        }
                        else -> binding.imageFarm.setBackgroundResource(R.drawable.middle2)
                    }
                }
                if (status == 2) {
                    when {
                        it > 1000 -> binding.imageFarm.setBackgroundResource(R.drawable.richnew)
                        it < 0 -> {
                            binding.imageFarm.setBackgroundResource(R.drawable.rich3)
                            showWarning()
                        }
                        else -> binding.imageFarm.setBackgroundResource(R.drawable.rich2)
                    }
                }

            })

        })
        return binding.root
    }

    private fun showWarning() {
        var dialog = Dialog(this.requireContext())
        var bindingCheck = DialogCheckBinding.inflate(layoutInflater)
        dialog.setContentView(bindingCheck.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        bindingCheck.checkContent.text = "這個月透支了，農場被查封，宣告破產!!"
        bindingCheck.imageCancel.visibility = View.GONE
        bindingCheck.imageSave.visibility = View.GONE
        GlobalScope.launch(context = Dispatchers.Main) {
            dialog.show()
            delay(2500)
            dialog.dismiss()
        }
    }

    private fun note() {
        var dialog = Dialog(this.requireContext())
        var bindingCheck = DialogCheckBinding.inflate(layoutInflater)
        dialog.setContentView(bindingCheck.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        bindingCheck.checkContent.text = "跳至今日已完成"
        bindingCheck.imageCancel.visibility = View.GONE
        bindingCheck.imageSave.visibility = View.GONE
        GlobalScope.launch(context = Dispatchers.Main) {
            dialog.show()
            delay(1000)
            dialog.dismiss()
        }
    }


    private fun setViewPager() {
        val codes = getDays(currentDayCode)
        val dailyAdapter = DayViewPagerAdapter(childFragmentManager, codes, viewModel)
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

    private fun arrowButtons(){

        binding.imageArrowRight.setOnClickListener {
            binding.dayViewpager.currentItem = binding.dayViewpager.currentItem + 1
        }

        binding.imageArrowLeft.setOnClickListener {
            binding.dayViewpager.currentItem = binding.dayViewpager.currentItem - 1
        }

    }


    private fun goToToday() {
        currentDayCode = todayDayCode
        setViewPager()
//        note()
    }

    private fun showDialog() {
        val calendar = Calendar.getInstance()
        val simpledateformat = SimpleDateFormat("EEEE")
        val datePickdialog = DatePickerDialog(
            this.context!!, R.style.my_dialog_theme,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                // Display Selected date in Toast
                val date = Date(year, month, day-1)
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

    private fun restart(){
        val intent = Intent(this.context, MainActivity::class.java)
        startActivity(intent)
    }
}
