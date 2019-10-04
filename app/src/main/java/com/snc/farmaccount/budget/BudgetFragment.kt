package com.snc.farmaccount.budget


import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.snc.farmaccount.MainActivity
import com.snc.farmaccount.MainViewModel
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Budget
import com.snc.farmaccount.databinding.DialogCheckBinding
import com.snc.farmaccount.databinding.DialogNumberpickBinding
import com.snc.farmaccount.databinding.FragmentBudgetBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList



class BudgetFragment : Fragment() {

    private lateinit var binding: FragmentBudgetBinding
    private lateinit var numberPickerDialog: Dialog
    private lateinit var warningDialog: Dialog
    private lateinit var bindingNumberPicker: DialogNumberpickBinding
    private lateinit var bindingCheck: DialogCheckBinding
    var budget = ArrayList<Budget>()
    var budgetUnselect = ArrayList<Budget>()

    private val viewModel: BudgetViewModel by lazy {
        ViewModelProviders.of(this).get(BudgetViewModel::class.java)
    }
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this.requireActivity()).get(MainViewModel::class.java)
    }
    private val getViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBudgetBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.farmList.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.textBudget.isEnabled = false
        addBudget()
        unEditBudget()
        viewModel.getBudget()
        getViewPager()
        viewModel.getBudgetPrice()
        changeArrow()
        numberPicker()
        backState()

        binding.farmList.adapter = BudgetAdapter(budget,BudgetAdapter.OnClickListener {
            viewModel.getBudgetType.value = it
        })

        binding.unSelectFarmList.adapter = BudgetAdapter(budgetUnselect,
            BudgetAdapter.OnClickListener {
        })

        viewModel.position.observe(this, Observer {
            binding.farmList.setCurrentItem(it, false)
            binding.unSelectFarmList.setCurrentItem(it, false)
            binding.unSelectFarmList.isUserInputEnabled = false
        })

        binding.imageSend.setOnClickListener {
            viewModel.editBudgetPrice()
            warningDialog()
        }

        binding.imageBackState.setOnClickListener {
            findNavController()
                .navigate(R.id.action_global_homeFragment)
        }

        binding.imageEdit.setOnClickListener {
            binding.textBudget.isEnabled = true
            binding.textBudget.setTextColor(resources.getColor(R.color.money_text))
            binding.price.background = resources.getDrawable(R.drawable.money_border)
            binding.imageCoin.background = resources.getDrawable(R.drawable.money_icon)
            binding.imageArrowRight.visibility = View.VISIBLE
            binding.imageArrowRight.setImageResource(R.drawable.arrow)
            binding.imageArrowLeft.setImageResource(R.drawable.arrow)

            binding.imageArrowRight.setOnClickListener {
                binding.farmList.currentItem = binding.farmList.currentItem + 1

                if (binding.farmList.currentItem == 2) {
                    binding.imageArrowRight.visibility = View.INVISIBLE
                } else {
                    binding.imageArrowLeft.visibility = View.VISIBLE
                }
            }

            binding.imageArrowLeft.setOnClickListener {
                binding.farmList.currentItem = binding.farmList.currentItem - 1

                if (binding.farmList.currentItem == 0) {
                    binding.imageArrowLeft.visibility = View.INVISIBLE
                } else {
                    binding.imageArrowRight.visibility = View.VISIBLE
                }
            }

            binding.farmList.visibility = View.VISIBLE
            binding.unSelectFarmList.visibility = View.GONE
        }

        return binding.root
    }

    private fun getViewPager() {
        binding.farmList.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.selectPosition.value = position
                viewModel.getBudgetType.value = budget[position]
                Log.i("Sophie","${viewModel.getBudgetType.value}")
                // No boilerplate, only useful
            }
        })
    }

    private fun changeArrow() {
        viewModel.selectPosition.observe(this, Observer {
            when (it) {
                0 -> binding.imageArrowLeft.visibility = View.GONE

                2 -> binding.imageArrowRight.visibility = View.INVISIBLE

                else -> {
                    binding.imageArrowRight.visibility = View.VISIBLE
                    binding.imageArrowLeft.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun addBudget() {
        budget.add(Budget(R.drawable.type1, R.drawable.rangelow, getString(R.string.range_start),
            getString(R.string.range_low_end),"",0,"",1))
        budget.add(Budget(R.drawable.type2, R.drawable.rangemiddle, getString(R.string.range_start),
            getString(R.string.range_middle_end),"",1,"",1))
        budget.add(Budget(R.drawable.type3, R.drawable.rangehigh, getString(R.string.range_start),
            getString(R.string.range_high_end),"",2,"",1))
        viewModel.budgetType.value = budget
    }

    private fun unEditBudget() {
        budgetUnselect.add(Budget(R.drawable.type1un, R.drawable.rangelow_un,
            getString(R.string.range_start), getString(R.string.range_low_end),
            "",0,"",1))
        budgetUnselect.add(Budget(R.drawable.type2un, R.drawable.rangemiddle_un,
            getString(R.string.range_start), getString(R.string.range_middle_end),
            "",1,"",1))
        budgetUnselect.add(Budget(R.drawable.type3un, R.drawable.ranghigh_un,
            getString(R.string.range_start), getString(R.string.range_high_end),
            "",2,"",1))
    }

    private fun numberPicker() {
        binding.numberTitle.text = String.format(getString(R.string.overage_cycle),
            "${mainViewModel.pickdate.value}")

        (activity as MainActivity).showCheckDialog()
        binding.numberTitle.setOnClickListener {
            numberPickerDialog.show()
        }

        mainViewModel.maxDay.observe(this, Observer { maxDay->
            bindingNumberPicker.numberPicker.maxValue = maxDay

            bindingNumberPicker.numberPicker.setOnValueChangedListener {
                    _, _, newVal ->
                bindingNumberPicker.save.setOnClickListener {
                    mainViewModel.pickdate.value = newVal
                    mainViewModel.postCycleDay()
                    getViewModel.getCycleDay()
                    binding.numberTitle.text = String.format(getString(R.string.overage_cycle),
                        "${mainViewModel.pickdate.value}")
                    numberPickerDialog.dismiss()
                }
            }
        })

        bindingNumberPicker.cancel.setOnClickListener {
            numberPickerDialog.dismiss()
        }
    }

    private fun warningDialog() {
        showCheckDialog()
        bindingCheck.imageCancel.visibility = View.GONE
        bindingCheck.imageSave.visibility = View.GONE

        GlobalScope.launch(context = Dispatchers.Main) {
            when (viewModel.isPriceMoreThan.value) {
                true -> {
                    bindingCheck.checkContent.setText(R.string.price_over_check)
                    warningDialog.show()
                    delay(1000)
                    warningDialog.dismiss()
                }

                false -> {
                    bindingCheck.checkContent.setText(R.string.price_less_check)
                    warningDialog.show()
                    delay(1000)
                    warningDialog.dismiss()
                }

                else -> {
                    bindingCheck.checkContent.setText(R.string.edit_complete)
                    warningDialog.show()
                    delay(1000)
                    warningDialog.dismiss()
                    findNavController()
                        .navigate(R.id.action_global_homeFragment)}
            }
        }
    }

    private fun backState() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().
                    navigate(R.id.action_global_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    fun showCheckDialog() {
        warningDialog = Dialog(this.requireContext())
        bindingCheck = DialogCheckBinding.inflate(layoutInflater)
        warningDialog.setContentView(bindingCheck.root)
        warningDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
