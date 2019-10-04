package com.snc.farmaccount.home


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.FragmentHomeBinding
import com.snc.farmaccount.helper.Format
import java.text.SimpleDateFormat
import java.util.*
import android.app.DatePickerDialog
import android.app.Dialog
import com.snc.farmaccount.MainViewModel
import com.snc.farmaccount.databinding.DialogCheckBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.content.Intent
import com.snc.farmaccount.MainActivity


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var warningDialog: Dialog
    private lateinit var bindingCheck: DialogCheckBinding
    private val PREFILLED_DAYS = 251
    private var defaultDailyPage = 0
    private var todayDayCode = ""
    private var currentDayCode = ""
    private val calendar = Calendar.getInstance()
    private var buttonSense = false

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
        currentDayCode= viewModel.dateMode
        todayDayCode = viewModel.dateMode
        binding.dayViewpager.id = (System.currentTimeMillis() % 100000).toInt()
        setViewPager()
        refreshEvents()
        arrowButtons()
        viewModel.getOverage()

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
                binding.buttonSetting.animate().rotation(360f).start()
                true
            } else {
                binding.buttonBudget.animate().translationY(0f).start()
                binding.buttonStatistic.animate().translationY(0f).start()
                binding.buttonScan.animate().translationY(0f).start()
                binding.buttonSetting.animate().rotation(0f).start()
                false
            }

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

        binding.imageAddEvent.setOnClickListener {
            findNavController()
                .navigate(R.id.action_global_addEventFragment)
        }

        binding.buttonDatePicker.setOnClickListener {
            datePickerDialog()
        }

        binding.buttonDateToday.setOnClickListener {
            goToToday()
        }

        viewModel.overagePrice.observe(this, androidx.lifecycle.Observer {
            binding.textBudget.text = it
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
        showCheckDialog()
        bindingCheck.checkContent.setText(R.string.overage_zero)
        bindingCheck.imageCancel.visibility = View.GONE
        bindingCheck.imageSave.visibility = View.GONE

        GlobalScope.launch(context = Dispatchers.Main) {
            warningDialog.show()
            delay(2500)
            warningDialog.dismiss()
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

                override fun onPageScrolled(position: Int, positionOffset: Float,
                                            positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    currentDayCode = codes[position]
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
    }

    private fun datePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this.context!!, R.style.my_dialog_theme,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                // Display Selected date in Toast
                val date = Date(year, month, day-1)
                val dayOfWeek = Format.datePicker(date)
                currentDayCode = "$year.${month+1}.$day ($dayOfWeek)"
                setViewPager()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    private fun refreshEvents() {
        (binding.dayViewpager.adapter as DayViewPagerAdapter)
            .updateCalendars(binding.dayViewpager.currentItem)
    }

    private fun restart() {
        val intent = Intent(this.context, MainActivity::class.java)
        startActivity(intent)
    }

    fun showCheckDialog() {
        warningDialog = Dialog(this.requireContext())
        bindingCheck = DialogCheckBinding.inflate(layoutInflater)
        warningDialog.setContentView(bindingCheck.root)
        warningDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
