package com.snc.farmaccount.login


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.DialogLoadingBinding


class LoadingFragment : DialogFragment() {

    private lateinit var binding: DialogLoadingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = DialogLoadingBinding.inflate(inflater)
       binding.lifecycleOwner = this

       return binding.root
    }


}
