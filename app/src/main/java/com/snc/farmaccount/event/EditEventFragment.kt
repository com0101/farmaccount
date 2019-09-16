package com.snc.farmaccount.event


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Tag
import com.snc.farmaccount.databinding.FragmentEditEventBinding


class EditEventFragment : Fragment() {

    private lateinit var binding: FragmentEditEventBinding
    val tag = ArrayList<Tag>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditEventBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val application = requireNotNull(activity).application

        val product = EditEventFragmentArgs.fromBundle(arguments!!).edit

        val viewModelFactory = EditEventFactory(product , application)

        val viewModel = ViewModelProviders.of(
            this, viewModelFactory).get(EditEventViewModel::class.java)

        binding.viewModel = viewModel

        binding.imageBackState.setOnClickListener {
            findNavController()
                .navigate(AddEventFragmentDirections.actionGlobalHomeFragment())
        }

        binding.tagList.adapter = TagAdapter(TagAdapter.OnClickListener {
            viewModel.chooseTag.value = it
        })

        binding.imageSave.setOnClickListener {
            viewModel.editFirebase()
            findNavController()
                .navigate(EditEventFragmentDirections.actionGlobalHomeFragment())
        }

        tagList()
        viewModel.mark.value = tag
        viewModel.getTag()

        return binding.root
    }

    private fun tagList() {
        tag.add(Tag(R.drawable.tag_egg,R.drawable.tag_egg_press,
            getString(R.string.tag_breakfast),false, getString(R.string.catalog_eat)))
        tag.add(Tag(R.drawable.tag_pig,R.drawable.tag_pig_press,
            getString(R.string.tag_lunch),false, getString(R.string.catalog_eat)))
        tag.add(Tag(R.drawable.tag_cow,R.drawable.tag_cow_press,
            getString(R.string.tag_dinner),false, getString(R.string.catalog_eat)))
        tag.add(Tag(R.drawable.tag_ginger,R.drawable.tag_ginger_press,
            getString(R.string.tag_dessert),false, getString(R.string.catalog_eat)))
        tag.add(Tag(R.drawable.tag_money,R.drawable.tag_money_press,
            getString(R.string.tag_payment),true, getString(R.string.catalog_income)))
        tag.add(Tag(R.drawable.tag_cloth,R.drawable.tag_cloth_press,
            getString(R.string.tag_cloth),false, getString(R.string.catalog_cloth)))
        tag.add(Tag(R.drawable.tag_live,R.drawable.tag_live_press,
            getString(R.string.tag_live),false, getString(R.string.catalog_live)))
        tag.add(Tag(R.drawable.tag_traffic,R.drawable.tag_traffic_press,
            getString(R.string.tag_traffic),false, getString(R.string.catalog_traffic)))
        tag.add(Tag(R.drawable.tag_fun,R.drawable.tag_fun_press,
            getString(R.string.tag_fun),false, getString(R.string.catalog_fun)))
        tag.add(Tag(R.drawable.tag_ticket,R.drawable.tag_ticket_press,
            getString(R.string.tag_lottery),true, getString(R.string.catalog_income)))
    }


}
