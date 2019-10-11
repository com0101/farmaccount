package com.snc.farmaccount.detail


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
import com.snc.farmaccount.MainActivity

import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.DialogCheckBinding
import com.snc.farmaccount.databinding.FragmentDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var warningDialog: Dialog
    private lateinit var bindingCheck: DialogCheckBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val application = requireNotNull(activity).application
        val product = DetailFragmentArgs.fromBundle(arguments!!).detail
        val viewModelFactory = DetailFactory(product , application)
        val viewModel = ViewModelProviders.of(
            this, viewModelFactory).get(DetailViewModel::class.java)
        binding.viewModel = viewModel

        binding.imageEdit.setOnClickListener {
            binding.imageEdit.setImageResource(R.drawable.edit_press)
            binding.imageEdit.isClickable = false
            findNavController()
                .navigate(DetailFragmentDirections.actionGlobalEditEventFragment(product))
        }

        binding.imageDelete.setOnClickListener {
            checkEdit()

            bindingCheck.imageCancel.setOnClickListener {
                warningDialog.dismiss()
                binding.imageDelete.setImageResource(R.drawable.delete)
                binding.imageDelete.isClickable = true
                bindingCheck.imageCancel.setImageResource(R.drawable.cancel_press)
            }

            bindingCheck.imageSave.setOnClickListener {
                viewModel.deleteEvent()
                successDialog()
            }
        }

        binding.imageBackState.setOnClickListener {
            findNavController()
                .navigate(R.id.action_global_homeFragment)
        }

        viewModel.detail.observe(this , Observer {
            Log.i("Sophie_detail","$it")
            if(it.tag == getString(R.string.tag_breakfast)) {
                binding.tagImage.setImageResource(R.drawable.tag_egg_press)
            }
            if(it.tag == getString(R.string.tag_lunch)) {
                binding.tagImage.setImageResource(R.drawable.tag_pig_press)
            }
            if(it.tag == getString(R.string.tag_dinner)) {
                binding.tagImage.setImageResource(R.drawable.tag_cow_press)
            }
            if(it.tag == getString(R.string.tag_dessert)) {
                binding.tagImage.setImageResource(R.drawable.tag_ginger_press)
            }
            if(it.tag == getString(R.string.tag_cloth)) {
                binding.tagImage.setImageResource(R.drawable.tag_cloth_press)
            }
            if(it.tag == getString(R.string.tag_live)) {
                binding.tagImage.setImageResource(R.drawable.tag_live_press)
            }
            if(it.tag == getString(R.string.tag_traffic)) {
                binding.tagImage.setImageResource(R.drawable.tag_traffic_press)
            }
            if(it.tag == getString(R.string.tag_fun)) {
                binding.tagImage.setImageResource(R.drawable.tag_fun_press)
            }
            if(it.tag == getString(R.string.tag_payment)) {
                binding.tagImage.setImageResource(R.drawable.tag_money_press)
            }
            if(it.tag == getString(R.string.tag_lottery)) {
                binding.tagImage.setImageResource(R.drawable.tag_ticket_press)
            }
        })

        backState()

        return binding.root
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

    private fun checkEdit() {
        showCheckDialog()
        binding.imageDelete.setImageResource(R.drawable.delete_press)
        binding.imageDelete.isClickable = false
        warningDialog.show()
        bindingCheck.checkContent.setText(R.string.delete_check)
    }

    private fun successDialog() {
        warningDialog.dismiss()
        bindingCheck.imageSave.setImageResource(R.drawable.yes_press)
        bindingCheck.imageSave.isClickable = false

        GlobalScope.launch(context = Dispatchers.Main) {
            bindingCheck.imageSave.setImageResource(R.drawable.yes)
            bindingCheck.imageSave.isClickable = true
            delay(1000)
            bindingCheck.checkContent.setText(R.string.delete_complete)
            bindingCheck.imageCancel.visibility = View.GONE
            bindingCheck.imageSave.visibility = View.GONE
            warningDialog.show()
            delay(1000)
            warningDialog.dismiss()
            findNavController()
                .navigate(R.id.action_global_homeFragment)
        }
    }

    fun showCheckDialog() {
        warningDialog = Dialog(this.requireContext())
        bindingCheck = DialogCheckBinding.inflate(layoutInflater)
        warningDialog.setContentView(bindingCheck.root)
        warningDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
