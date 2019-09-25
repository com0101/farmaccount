package com.snc.farmaccount.choose


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
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Budget
import com.snc.farmaccount.budget.BudgetViewModel
import com.snc.farmaccount.databinding.FragmentChooseBinding
import com.snc.farmaccount.dialog.AmountInputDialogArgs


class ChooseFragment : Fragment() {

    private lateinit var binding: FragmentChooseBinding
    var budget = ArrayList<Budget>()

    private val viewModel: BudgetViewModel by lazy {
        ViewModelProviders.of(this).get(BudgetViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChooseBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.farmList.orientation = ViewPager2.ORIENTATION_HORIZONTAL

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

        addBudget()
        viewModel.getBudget()
        binding.farmList.adapter = ChooseAdapter(budget,ChooseAdapter.OnClickListener {
            viewModel.getBudgetType.value = it
            findNavController()
                .navigate(ChooseFragmentDirections.actionGlobalAmountInputDialog(it))
        })
        getPager2()
        changeArrow()
        return binding.root
    }

    private fun getPager2() {
        binding.farmList.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.selectPosition.value = position
                Log.i("Sophie","${viewModel.selectPosition.value}")
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
        budget.add(Budget(R.drawable.type2, R.drawable.rangemiddle, "10000", "25000","",1,"",1))
        budget.add(Budget(R.drawable.type3, R.drawable.rangehigh, "10000", "35000","",2,"",1))
        viewModel.budgetType.value = budget
    }

}
