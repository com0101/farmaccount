package com.snc.farmaccount

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.simplemobiletools.commons.extensions.beVisible
import com.snc.farmaccount.databinding.ActivityMainBinding
import com.snc.farmaccount.extensions.config
import com.snc.farmaccount.helper.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
    }


}
