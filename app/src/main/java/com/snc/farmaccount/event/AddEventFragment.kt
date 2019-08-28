package com.snc.farmaccount.event


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.FragmentAddEventBinding

class AddEventFragment : Fragment() {

    private lateinit var binding: FragmentAddEventBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddEventBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.imageSave.setOnClickListener {
            findNavController()
                .navigate(AddEventFragmentDirections.actionGlobalHomeFragment())
        }

        binding.imageBackState.setOnClickListener {
            findNavController()
                .navigate(AddEventFragmentDirections.actionGlobalHomeFragment())
        }

        // Inflate the layout for this fragment
        return binding.root
    }


}
