package com.snc.farmaccount.budget


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.snc.farmaccount.MainViewModel
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Budget
import com.snc.farmaccount.databinding.DialogCheckBinding
import com.snc.farmaccount.databinding.DialogNumberpickBinding
import com.snc.farmaccount.databinding.FragmentBudgetBinding
import com.snc.farmaccount.dialog.AmountInputDialogDirections
import com.snc.farmaccount.statistic.StatisticViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList



class BudgetFragment : Fragment() {

    private lateinit var binding: FragmentBudgetBinding
    var budget = ArrayList<Budget>()
    var budgetUnselect = ArrayList<Budget>()

    private val viewModel: BudgetViewModel by lazy {
        ViewModelProviders.of(this).get(BudgetViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBudgetBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
//        binding.farmList.visibility = View.GONE
//        binding.farmList.visibility = View.VISIBLE
        binding.farmList.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.textBudget.isEnabled = false
        binding.textBudget.setTextColor(resources.getColor(R.color.deep_gray))
        binding.price.background = resources.getDrawable(R.drawable.unedit_radius_border)
        binding.imageCoin.background = resources.getDrawable(R.drawable.money_unedit)


        addBudget()
        unEditBudget()
        viewModel.getBudget()
        getPager2()
        viewModel.getBudgetPrice()
        changeArrow()
        numberPicker()
        binding.farmList.adapter = BudgetAdapter(budget,BudgetAdapter.OnClickListener {
            viewModel.getBudgetType.value = it
            Snackbar.make(this.requireView(), "選擇更改金額或直接儲存", Snackbar.LENGTH_LONG).show()
        })

        binding.unSelectFarmList.adapter = BudgetAdapter(budgetUnselect,BudgetAdapter.OnClickListener {

        })

        viewModel.position.observe(this, Observer {
            binding.farmList.setCurrentItem(it, false)
            binding.unSelectFarmList.setCurrentItem(it, false)
            binding.unSelectFarmList.isUserInputEnabled = false
        })

        binding.imageSend.setOnClickListener {
            viewModel.editBudgetPrice()
            var dialog = Dialog(this.requireContext())
            var bindingCheck = DialogCheckBinding.inflate(layoutInflater)
            dialog.setContentView(bindingCheck.root)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            GlobalScope.launch(context = Dispatchers.Main) {
                when {
                    viewModel.amountCheck.value == true -> {
                        bindingCheck.checkContent.text = "超出範圍啦!"
                        bindingCheck.imageCancel.visibility = View.GONE
                        bindingCheck.imageSave.visibility = View.GONE
                        dialog.show()
                        delay(1000)
                        dialog.dismiss()
                    }
                    viewModel.amountCheck.value == false -> {
                        bindingCheck.checkContent.text = "省錢還是要顧身體啦!"
                        bindingCheck.imageCancel.visibility = View.GONE
                        bindingCheck.imageSave.visibility = View.GONE
                        dialog.show()
                        delay(1000)
                        dialog.dismiss()
                    }
                    else -> {
                        bindingCheck.checkContent.text = "編輯完成!"
                        bindingCheck.imageCancel.visibility = View.GONE
                        bindingCheck.imageSave.visibility = View.GONE
                        dialog.show()
                        delay(1000)
                        dialog.dismiss()
                        findNavController()
                        .navigate(AmountInputDialogDirections.actionGlobalHomeFragment())}
                }
            }

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

    private fun getPager2() {
        binding.farmList.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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
            Log.i("Sophie2","$it")
            if(it == 0) {
                binding.imageArrowLeft.visibility = View.GONE
            } else {
                binding.imageArrowRight.visibility = View.VISIBLE
            }
            if (it == 2) {
                binding.imageArrowRight.visibility = View.INVISIBLE
            } else {
                binding.imageArrowLeft.visibility = View.VISIBLE
            }
        })

    }

    private fun addBudget() {
        budget.add(Budget(R.drawable.type1, R.drawable.rangelow, "10000", "15000","",0,"",1))
        budget.add(Budget(R.drawable.type2, R.drawable.rangemiddle, "10000", "20000","",1,"",1))
        budget.add(Budget(R.drawable.type3, R.drawable.rangehigh, "10000", "25000","",2,"",1))
        viewModel.budgetType.value = budget
    }

    private fun unEditBudget() {
        budgetUnselect.add(Budget(R.drawable.type1un, R.drawable.rangelow_un, "10000", "15000","",0,"",1))
        budgetUnselect.add(Budget(R.drawable.type2un, R.drawable.rangemiddle_un, "10000", "20000","",1,"",1))
        budgetUnselect.add(Budget(R.drawable.type3un, com.snc.farmaccount.R.drawable.ranghigh_un, "10000", "25000","",2,"",1))
    }

    fun numberPicker() {
        val activityViewModel = ViewModelProviders.of(this.requireActivity())
            .get(MainViewModel::class.java)
        binding.numberTitle.text = "每個月第 ${activityViewModel.pickdate.value} 天結算"
        var dialog = Dialog(this.requireContext())
        var bindingCheck = DialogNumberpickBinding.inflate(layoutInflater)
        dialog.setContentView(bindingCheck.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        binding.numberTitle.setOnClickListener {
            dialog.show()
        }

        activityViewModel.maxDay.observe(this, Observer { maxDay->
            bindingCheck.numberPicker.maxValue = maxDay
            bindingCheck.numberPicker.setOnValueChangedListener {
                    _, _, newVal ->
                bindingCheck.save.setOnClickListener {
                    activityViewModel.pickdate.value = newVal
                    activityViewModel.postCircleDay()
                    if (newVal == maxDay) {
                        binding.numberTitle.text = "每個月最後一天結算"
                    } else {
                        binding.numberTitle.text = "每個月第 ${activityViewModel.pickdate.value} 天結算"
                    }
                    dialog.dismiss()
                    Log.d("Sophie", "$newVal")
                }

            }
        })
        bindingCheck.cancel.setOnClickListener {
            activityViewModel.pickdate.value = 1
            dialog.dismiss()
        }



    }


}
