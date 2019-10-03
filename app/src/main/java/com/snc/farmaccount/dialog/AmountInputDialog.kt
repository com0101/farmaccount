package com.snc.farmaccount.dialog


import android.app.Dialog
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
import com.snc.farmaccount.R
import com.snc.farmaccount.budget.AmountViewModelFactory
import com.snc.farmaccount.databinding.DialogAmountInputBinding
import com.snc.farmaccount.databinding.DialogCheckBinding

class AmountInputDialog : DialogFragment() {

    private lateinit var binding: DialogAmountInputBinding
    private lateinit var warningDialog: Dialog
    private lateinit var bindingCheck: DialogCheckBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogAmountInputBinding.inflate(inflater)
        binding.lifecycleOwner = this
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val application = requireNotNull(activity).application
        val budget = AmountInputDialogArgs.fromBundle(arguments!!).amount
        val viewModelFactory = AmountViewModelFactory(budget , application)
        val viewModel = ViewModelProviders.of(
            this, viewModelFactory).get(AmountViewModel::class.java)
        binding.viewModel = viewModel

        binding.imageSave.setOnClickListener {
            viewModel.getInput()
            when {
                viewModel.amountCheck.value == true ->
                    Toast.makeText(context, getString(R.string.price_over_check), Toast.LENGTH_SHORT).show()
                viewModel.amountCheck.value == false ->
                    Toast.makeText(context, getString(R.string.price_less_check), Toast.LENGTH_SHORT).show()
                else -> findNavController()
                    .navigate(R.id.action_global_homeFragment)
            }
            viewModel.addBudget()
        }

        binding.imageCancel.setOnClickListener {
            dismiss()
        }

        viewModel.detail.observe(this, Observer{
            Log.i("Sophie_getType", "$it")
            viewModel.getRange(it)
        })
        // Inflate the layout for this fragment
        return binding.root
    }


}
