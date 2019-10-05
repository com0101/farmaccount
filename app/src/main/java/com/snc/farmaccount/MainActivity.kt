package com.snc.farmaccount


import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.snc.farmaccount.databinding.ActivityMainBinding
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import android.net.ConnectivityManager
import android.util.Log
import com.snc.farmaccount.helper.INTERNET
import com.snc.farmaccount.helper.SOPHIE
import android.content.Intent




class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        statusBar()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.getCycleDay()
        navigationToHome()
        registerConnectReceiver()
        Fabric.with(this, Crashlytics())
    }

    private fun navigationToHome() {
        when (viewModel.isLogIn.value) {
            true -> checkBudgetStatus()

            else -> this.findNavController(R.id.myNavHostFragment)
                .navigate(R.id.action_global_logInFragment)
        }
    }

    //check Budget collection exist
    private fun checkBudgetStatus() {
        viewModel.hasBudget.observe(this, Observer {
            it?.let {
                when (viewModel.hasBudget.value) {
                    false -> this.findNavController(R.id.myNavHostFragment)
                        .navigate(R.id.action_global_homeFragment)

                    else -> this. findNavController(R.id.myNavHostFragment)
                        .navigate(R.id.action_global_chooseFragment)
                }

            }
        })
    }

    @SuppressLint("ObsoleteSdkInt")
    fun statusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 4.4
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 5.0
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) // 確認取消半透明設置。
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 全螢幕顯示，status bar 不隱藏，activity 上方 layout 會被 status bar 覆蓋。
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE // 配合其他 flag 使用，防止 system bar 改變後 layout 的變動。
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)// 表示我們的 UI 是 LIGHT 的 style，icon 就會呈現深色系。

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) // 跟系統表示要渲染 system bar 背景。
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    //check internet connection
    @SuppressLint("ObsoleteSdkInt")
    fun registerConnectReceiver() {
        val intentFilter = IntentFilter(INTERNET)
        registerReceiver(CheckInternet(), intentFilter)
    }


}




