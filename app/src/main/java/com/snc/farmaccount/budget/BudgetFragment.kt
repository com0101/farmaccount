package com.snc.farmaccount.budget


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Budget
import com.snc.farmaccount.choose.ChooseFragmentDirections
import com.snc.farmaccount.databinding.FragmentBudgetBinding
import com.snc.farmaccount.databinding.ItemFarmEditBinding
import com.snc.farmaccount.dialog.AmountInputDialogDirections





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
        binding.farmList.visibility = View.GONE
        binding.unSelectFarmList.visibility = View.VISIBLE
        binding.farmList.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.textBudget.isEnabled = false
        binding.textBudget.setTextColor(resources.getColor(R.color.light_gray))
        binding.price.background = resources.getDrawable(R.drawable.unedit_radius_border)
        binding.imageCoin.background = resources.getDrawable(R.drawable.money_unedit)


        addBudget()
        unEditBudget()
        viewModel.getBudget()
        getPager2()
        viewModel.getBudgetPrice()
        changeArrow()
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
            when {
                viewModel.amountCheck.value == true -> Toast.makeText(context, "超出範圍啦", Toast.LENGTH_SHORT).show()
                viewModel.amountCheck.value == false -> Toast.makeText(context, "省錢還是要顧身體啦", Toast.LENGTH_SHORT).show()
                else -> findNavController()
                    .navigate(AmountInputDialogDirections.actionGlobalHomeFragment())
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
        budget.add(Budget(R.drawable.type1, R.drawable.rangebar, "10000", "15000","",0,""))
        budget.add(Budget(R.drawable.type2, R.drawable.rangebar, "10000", "20000","",1,""))
        budget.add(Budget(R.drawable.type3, R.drawable.rangebar, "10000", "25000","",2,""))
        viewModel.budgetType.value = budget
    }

    private fun unEditBudget() {
        budgetUnselect.add(Budget(R.drawable.type1un, R.drawable.rangebarun, "10000", "15000","",0,""))
        budgetUnselect.add(Budget(R.drawable.type2un, R.drawable.rangebarun, "10000", "20000","",1,""))
        budgetUnselect.add(Budget(R.drawable.type3un, R.drawable.rangebarun, "10000", "25000","",2,""))
    }


}
