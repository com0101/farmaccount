package com.snc.farmaccount.choose


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Budget
import com.snc.farmaccount.budget.BudgetAdapter
import com.snc.farmaccount.budget.BudgetViewModel
import com.snc.farmaccount.databinding.FragmentChooseBinding


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

        binding.farmList.adapter = BudgetAdapter(BudgetAdapter.OnClickListener {
            findNavController()
                .navigate(ChooseFragmentDirections.actionGlobalAmountInputDialog())
        },viewModel)
        binding.farmIndicator.attachToRecyclerView(binding.farmList)

        binding.imageArrowRight.setOnClickListener {

            Log.i("Sophie_budget","${ binding.farmList.getChildItemId(it)}")
//            if (binding.slideViewPager.currentItem == 2) {
//                binding.imageArrowRight.visibility = View.INVISIBLE
//            } else {
//                binding.imageArrowLeft.visibility = View.VISIBLE
//            }
        }

        binding.imageArrowLeft.setOnClickListener {
//            binding.slideViewPager.currentItem = binding.slideViewPager.currentItem - 1
//            if (binding.slideViewPager.currentItem == 0) {
//                binding.imageArrowLeft.visibility = View.INVISIBLE
//            } else {
//                binding.imageArrowRight.visibility = View.VISIBLE
//            }
        }
        addBudget()
        viewModel.getBudget()

        return binding.root
    }

    private fun addBudget() {
        budget.add(Budget(R.drawable.hen, R.drawable.tag_lottery, "10000", "15000"))
        budget.add(Budget(R.drawable.hen, R.drawable.tag_breakfast, "10000", "20000"))
        budget.add(Budget(R.drawable.hen, R.drawable.tag_lunch_press, "10000", "25000"))
        viewModel.budgetType.value = budget
    }




}
