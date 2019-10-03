package com.snc.farmaccount.dialog


import android.app.Dialog
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
import com.snc.farmaccount.R
import com.snc.farmaccount.budget.AmountViewModelFactory
import com.snc.farmaccount.databinding.DialogAmountInputBinding
import com.snc.farmaccount.databinding.DialogCheckBinding
import com.snc.farmaccount.helper.SOPHIE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AmountInputDialog : DialogFragment() {

    private lateinit var binding: DialogAmountInputBinding
    private lateinit var warningDialog: Dialog
    private lateinit var bindingCheck: DialogCheckBinding
    var warningText = 0

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
                viewModel.isPriceMoreThan.value == true -> {
                    warningText = 1
                    warningDialog()
                }

                viewModel.isPriceMoreThan.value == false -> {
                    warningText = 2
                    warningDialog()
                }

                else -> findNavController()
                    .navigate(R.id.action_global_homeFragment)
            }
            viewModel.addBudget()
        }

        binding.imageCancel.setOnClickListener {
            dismiss()
        }

        viewModel.detail.observe(this, Observer{
            Log.i(SOPHIE, "getType:$it")
            viewModel.getRange(it)
        })

        return binding.root
    }

    private fun warningDialog() {
        warningDialog = Dialog(this.requireContext())
        bindingCheck = DialogCheckBinding.inflate(layoutInflater)
        warningDialog.setContentView(bindingCheck.root)
        warningDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        bindingCheck.imageCancel.visibility = View.GONE
        bindingCheck.imageSave.visibility = View.GONE

        GlobalScope.launch(context = Dispatchers.Main) {
            when(warningText) {
                1 -> {
                    bindingCheck.checkContent.setText(R.string.price_over_check)
                    warningDialog.show()
                    delay(1000)
                    warningDialog.dismiss()
                }
                2 -> {
                    bindingCheck.checkContent.setText(R.string.price_less_check)
                    warningDialog.show()
                    delay(1000)
                    warningDialog.dismiss()
                }
            }
        }
    }

}
