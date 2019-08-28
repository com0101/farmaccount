package com.snc.farmaccount.choose


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.FragmentChooseBinding


class ChooseFragment : Fragment() {

    private lateinit var binding: FragmentChooseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChooseBinding.inflate(inflater)
        binding.lifecycleOwner = this

        // Inflate the layout for this fragment
        return binding.root
    }


}