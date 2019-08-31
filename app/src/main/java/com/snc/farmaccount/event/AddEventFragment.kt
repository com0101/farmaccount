package com.snc.farmaccount.event


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Tag
import com.snc.farmaccount.databinding.FragmentAddEventBinding
import com.snc.farmaccount.home.DayViewModel

class AddEventFragment : Fragment() {

    private lateinit var binding: FragmentAddEventBinding

    private val viewModel: AddEventViewModel by lazy {
        ViewModelProviders.of(this).get(AddEventViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddEventBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel

        binding.imageSave.setOnClickListener {
            findNavController()
                .navigate(AddEventFragmentDirections.actionGlobalHomeFragment())
        }

        binding.imageBackState.setOnClickListener {
            findNavController()
                .navigate(AddEventFragmentDirections.actionGlobalHomeFragment())
        }
        tagList()
        viewModel.getTag()
        // Inflate the layout for this fragment
        return binding.root
    }


    private fun tagList() {
        val tag = ArrayList<Tag>()
        tag.add(Tag(R.drawable.tag_breakfast,R.drawable.tag_breakfast_press,
            getString(R.string.tag_breakfast),false))
        tag.add(Tag(R.drawable.tag_lunch,R.drawable.tag_lunch_press,
           getString(R.string.tag_lunch),false))
        tag.add(Tag(R.drawable.tag_dinner,R.drawable.tag_dinner_press,
            getString(R.string.tag_dinner),false))
        tag.add(Tag(R.drawable.tag_dessert,R.drawable.tag_dessert_press,
            getString(R.string.tag_dessert),false))
        tag.add(Tag(R.drawable.tag_payment,R.drawable.tag_payment_press,
            getString(R.string.tag_payment),true))
        tag.add(Tag(R.drawable.tag_cloth,R.drawable.tag_cloth_press,
            getString(R.string.tag_cloth),false))
        tag.add(Tag(R.drawable.tag_live,R.drawable.tag_live_press,
            getString(R.string.tag_live),false))
        tag.add(Tag(R.drawable.tag_traffic,R.drawable.tag_traffic_press,
            getString(R.string.tag_traffic),false))
        tag.add(Tag(R.drawable.tag_fun,R.drawable.tag_fun_press,
            getString(R.string.tag_fun),false))
        tag.add(Tag(R.drawable.tag_lottery,R.drawable.tag_lottery_press,
            getString(R.string.tag_lottery),true))
        viewModel.mark.value = tag
    }



}
