package com.snc.farmaccount.detail


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.imageEdit.setOnClickListener {
            findNavController()
                .navigate(DetailFragmentDirections.actionGlobalAddEventFragment())
        }

        binding.imageDelete.setOnClickListener {
            findNavController()
                .navigate(DetailFragmentDirections.actionGlobalHomeFragment())
        }

        binding.imageBackState.setOnClickListener {
            findNavController()
                .navigate(DetailFragmentDirections.actionGlobalHomeFragment())
        }

        // Inflate the layout for this fragment
        return binding.root
    }


}
