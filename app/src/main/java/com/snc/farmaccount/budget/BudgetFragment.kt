package com.snc.farmaccount.budget


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.FragmentBudgetBinding

class BudgetFragment : Fragment() {

    private lateinit var binding: FragmentBudgetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBudgetBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.imageBackState.setOnClickListener {
            findNavController()
                .navigate(BudgetFragmentDirections.actionGlobalHomeFragment())
        }

        // Inflate the layout for this fragment
        return binding.root
    }


}
