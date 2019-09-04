package com.snc.farmaccount.budget

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Budget
import com.snc.farmaccount.choose.ChooseFragmentDirections
import com.snc.farmaccount.databinding.FragmentBudgetBinding
import com.snc.farmaccount.databinding.ItemFarmTypeBinding

class BudgetFragment : Fragment() {

    private lateinit var binding: FragmentBudgetBinding
    var budget = ArrayList<Budget>()

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

        binding.farmList.adapter = BudgetAdapter(BudgetAdapter.OnClickListener {

        }, viewModel)

//        binding.slideViewPager.setOnClickListener {
//            findNavController()
//                .navigate(ChooseFragmentDirections.actionGlobalAmountInputDialog())
//        }

//        binding.imageArrowRight.setOnClickListener {
//            binding.slideViewPager.currentItem = binding.slideViewPager.currentItem + 1
//            if (binding.slideViewPager.currentItem == 2) {
//                binding.imageArrowRight.visibility = View.INVISIBLE
//            } else {
//                binding.imageArrowLeft.visibility = View.VISIBLE
//            }
//        }
//
//        binding.imageArrowLeft.setOnClickListener {
//            binding.slideViewPager.currentItem = binding.slideViewPager.currentItem - 1
//            if (binding.slideViewPager.currentItem == 0) {
//                binding.imageArrowLeft.visibility = View.INVISIBLE
//            } else {
//                binding.imageArrowRight.visibility = View.VISIBLE
//            }
//        }
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
