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

import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.DialogCheckBinding
import com.snc.farmaccount.databinding.FragmentDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var checkDialog: Dialog
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
                checkDialog.dismiss()
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
                .navigate(DetailFragmentDirections.actionGlobalHomeFragment())
        }

        viewModel.detail.observe(this , Observer {
            Log.i("Sophie_detail","$it")
            if(it.tag == "早餐") {
                binding.tagImage.setImageResource(R.drawable.tag_egg_press)
            }
            if(it.tag == "午餐") {
                binding.tagImage.setImageResource(R.drawable.tag_pig_press)
            }
            if(it.tag == "晚餐") {
                binding.tagImage.setImageResource(R.drawable.tag_cow_press)
            }
            if(it.tag == "點心") {
                binding.tagImage.setImageResource(R.drawable.tag_ginger_press)
            }
            if(it.tag == "衣服") {
                binding.tagImage.setImageResource(R.drawable.tag_cloth_press)
            }
            if(it.tag == "生活") {
                binding.tagImage.setImageResource(R.drawable.tag_live_press)
            }
            if(it.tag == "交通") {
                binding.tagImage.setImageResource(R.drawable.tag_traffic_press)
            }
            if(it.tag == "娛樂") {
                binding.tagImage.setImageResource(R.drawable.tag_fun_press)
            }
            if(it.tag == "薪水") {
                binding.tagImage.setImageResource(R.drawable.tag_money_press)
            }
            if(it.tag == "中獎") {
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
        checkDialog = Dialog(this.requireContext())
        bindingCheck = DialogCheckBinding.inflate(layoutInflater)
        checkDialog.setContentView(bindingCheck.root)
        binding.imageDelete.setImageResource(R.drawable.delete_press)
        binding.imageDelete.isClickable = false
        checkDialog.show()
        checkDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        bindingCheck.checkContent.text = "確定要刪掉嗎?"
    }

    private fun successDialog() {
        checkDialog.dismiss()
        bindingCheck.imageSave.setImageResource(R.drawable.yes_press)
        bindingCheck.imageSave.isClickable = false
        GlobalScope.launch(context = Dispatchers.Main) {
            bindingCheck.imageSave.setImageResource(R.drawable.yes)
            bindingCheck.imageSave.isClickable = true
            delay(1000)
            bindingCheck.checkContent.text = "刪除完成!"
            bindingCheck.imageCancel.visibility = View.GONE
            bindingCheck.imageSave.visibility = View.GONE
            checkDialog.show()
            delay(1000)
            checkDialog.dismiss()
            findNavController()
                .navigate(DetailFragmentDirections.actionGlobalHomeFragment())
        }
    }
}
