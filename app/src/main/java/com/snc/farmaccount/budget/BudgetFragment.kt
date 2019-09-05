package com.snc.farmaccount.budget


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
import com.snc.farmaccount.choose.ChooseFragmentDirections
import com.snc.farmaccount.databinding.FragmentBudgetBinding


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
        binding.farmList.adapter = BudgetAdapter(budget,BudgetAdapter.OnClickListener {
            viewModel.getBudgetType.value = it
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
        budget.add(Budget(R.drawable.hen, R.drawable.tag_lottery, "10000", "15000"))
        budget.add(Budget(R.drawable.hen, R.drawable.tag_breakfast, "10000", "20000"))
        budget.add(Budget(R.drawable.hen, R.drawable.tag_lunch_press, "10000", "25000"))
        viewModel.budgetType.value = budget
    }


}
