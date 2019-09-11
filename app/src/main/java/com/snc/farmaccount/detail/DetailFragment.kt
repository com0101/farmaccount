package com.snc.farmaccount.detail


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.FragmentDetailBinding

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
            viewModel.deleteEvent()
//            findNavController()
//                .navigate(DetailFragmentDirections.actionGlobalCheckDialog())
            findNavController()
                .navigate(DetailFragmentDirections.actionGlobalHomeFragment())
        }

        binding.imageBackState.setOnClickListener {
            findNavController()
                .navigate(DetailFragmentDirections.actionGlobalHomeFragment())
        }

        viewModel.detail.observe(this , Observer {
            Log.i("Sophie_detail","$it")
            if(it.tag == "早餐") {
                binding.tagImage.setImageResource(R.drawable.tag_breakfast)
            }
            if(it.tag == "午餐") {
                binding.tagImage.setImageResource(R.drawable.tag_lunch)
            }
            if(it.tag == "晚餐") {
                binding.tagImage.setImageResource(R.drawable.tag_dinner)
            }
            if(it.tag == "點心") {
                binding.tagImage.setImageResource(R.drawable.tag_dessert)
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
                binding.tagImage.setImageResource(R.drawable.tag_payment)
            }
            if(it.tag == "中獎") {
                binding.tagImage.setImageResource(R.drawable.tag_lottery)
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }


}
