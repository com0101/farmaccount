package com.snc.farmaccount.dialog


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.snc.farmaccount.ApplicationContext
import com.snc.farmaccount.budget.AmountViewModelFactory
import com.snc.farmaccount.databinding.DialogAmountInputBinding

class AmountInputDialog : DialogFragment() {

    private lateinit var binding: DialogAmountInputBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogAmountInputBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val application = requireNotNull(activity).application

        val budget = AmountInputDialogArgs.fromBundle(arguments!!).amount

        val viewModelFactory = AmountViewModelFactory(budget , application)

        val viewModel = ViewModelProviders.of(
            this, viewModelFactory).get(AmountViewModel::class.java)

        binding.viewModel = viewModel

        binding.imageSave.setOnClickListener {
            viewModel.getInput()
            when {
                viewModel.amountCheck.value == true -> Toast.makeText(context, "超出範圍啦", Toast.LENGTH_SHORT).show()
                viewModel.amountCheck.value == false -> Toast.makeText(context, "省錢還是要顧身體啦", Toast.LENGTH_SHORT).show()
                else -> findNavController()
                    .navigate(AmountInputDialogDirections.actionGlobalHomeFragment())
            }
            viewModel.addBudget()
        }

        binding.imageCancel.setOnClickListener {
            dismiss()
        }

        viewModel.detail.observe(this, Observer{
            Log.i("Sophie_getType", "$it")
            val usersToken = ApplicationContext.applicationContext()
                .getSharedPreferences("Budget", Context.MODE_PRIVATE)
            val editor = usersToken!!.edit()
            editor.putInt("FarmImage", it.farmImage ).apply()
            editor.putInt("Farmtype", it.farmtype ).apply()
            editor.putString("RangeStart", it.rangeStart ).apply()
            editor.putString("RangeEnd", it.rangeEnd ).apply()
            editor.putString("BudgetPrice", it.budgetPrice ).apply()
            viewModel.getRange(it)
        })
        // Inflate the layout for this fragment
        return binding.root
    }


}
