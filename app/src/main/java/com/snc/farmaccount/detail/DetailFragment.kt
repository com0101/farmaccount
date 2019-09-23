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
import com.google.android.material.bottomsheet.BottomSheetDialog

import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.DialogCheckBinding
import com.snc.farmaccount.databinding.FragmentDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application
        binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val product = DetailFragmentArgs.fromBundle(arguments!!).detail

        val viewModelFactory = DetailFactory(product , application)

        val viewModel = ViewModelProviders.of(
            this, viewModelFactory).get(DetailViewModel::class.java)

        binding.viewModel = viewModel

        binding.imageEdit.setOnClickListener {
            findNavController()
                .navigate(DetailFragmentDirections.actionGlobalEditEventFragment(product))
        }

        binding.imageDelete.setOnClickListener {
            var dialog = Dialog(this.requireContext())
            var bindingCheck = DialogCheckBinding.inflate(layoutInflater)
            dialog.setContentView(bindingCheck.root)
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            bindingCheck.checkContent.text = "確定要刪掉嗎?"
            bindingCheck.imageCancel.setOnClickListener {
                dialog.dismiss()
            }
            bindingCheck.imageSave.setOnClickListener {
                viewModel.deleteEvent()
                dialog.dismiss()
                GlobalScope.launch(context = Dispatchers.Main) {
                    delay(1000)
                    bindingCheck.checkContent.text = "刪除完成!"
                    bindingCheck.imageCancel.visibility = View.GONE
                    bindingCheck.imageSave.visibility = View.GONE
                    dialog.show()
                    delay(1000)
                    dialog.dismiss()
                    findNavController()
                        .navigate(DetailFragmentDirections.actionGlobalHomeFragment())
                }
            }
        }

        binding.imageBackState.setOnClickListener {
            findNavController()
                .navigate(DetailFragmentDirections.actionGlobalHomeFragment())
        }

        viewModel.detail.observe(this , Observer {
            Log.i("Sophie_detail","$it")
            if(it.tag == "早餐") {
                binding.tagImage.setImageResource(R.drawable.tag_egg)
            }
            if(it.tag == "午餐") {
                binding.tagImage.setImageResource(R.drawable.tag_pig)
            }
            if(it.tag == "晚餐") {
                binding.tagImage.setImageResource(R.drawable.tag_cow)
            }
            if(it.tag == "點心") {
                binding.tagImage.setImageResource(R.drawable.tag_ginger)
            }
            if(it.tag == "衣服") {
                binding.tagImage.setImageResource(R.drawable.tag_cloth)
            }
            if(it.tag == "住") {
                binding.tagImage.setImageResource(R.drawable.tag_live)
            }
            if(it.tag == "交通") {
                binding.tagImage.setImageResource(R.drawable.tag_traffic)
            }
            if(it.tag == "娛樂") {
                binding.tagImage.setImageResource(R.drawable.tag_fun)
            }
            if(it.tag == "薪水") {
                binding.tagImage.setImageResource(R.drawable.tag_money)
            }
            if(it.tag == "中獎") {
                binding.tagImage.setImageResource(R.drawable.tag_ticket)
            }
        })

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 做你要的事，這邊是跳轉首頁
                findNavController().
                    navigate(R.id.action_global_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        // Inflate the layout for this fragment
        return binding.root
    }




}
