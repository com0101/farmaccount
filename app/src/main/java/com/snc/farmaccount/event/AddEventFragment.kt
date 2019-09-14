package com.snc.farmaccount.event


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Tag
import com.snc.farmaccount.databinding.FragmentAddEventBinding
import com.snc.farmaccount.detail.DetailFactory
import com.snc.farmaccount.detail.DetailFragmentArgs
import com.snc.farmaccount.detail.DetailViewModel
import com.snc.farmaccount.home.DayViewModel
import com.snc.farmaccount.qrcode.QrCodeFragment
import com.snc.farmaccount.qrcode.QrCodeFragmentDirections

class AddEventFragment : Fragment() {

    private lateinit var binding: FragmentAddEventBinding
    val tag = ArrayList<Tag>()
    var REQUEST_EVALUATE = 0X110

    private val viewModel: AddEventViewModel by lazy {
        ViewModelProviders.of(this).get(AddEventViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddEventBinding.inflate(inflater)

        val QRviewModel: AddEventViewModel by lazy {
            ViewModelProviders.of(activity!!).get(AddEventViewModel::class.java)
        }
        QRviewModel.someDay.observe(viewLifecycleOwner, Observer {
            Log.i("Sophie_qr","$it")
            if (QRviewModel.someDay.value != null) {
                viewModel.today.value = it
                QRviewModel.someDay.value = null
            }
        })

        QRviewModel.getPrice.observe(viewLifecycleOwner, Observer {
            Log.i("Sophie_qr","$it")
            if (QRviewModel.getPrice.value != null) {
                viewModel.priceInput.value = it
                QRviewModel.getPrice.value = null
            }
        })

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.tagList.adapter = TagAdapter(TagAdapter.OnClickListener {
            viewModel.chooseTag.value = it
            Log.i("Sophie_tag","$it")
        })

        binding.imageSave.setOnClickListener {
            findNavController()
                .navigate(AddEventFragmentDirections.actionGlobalHomeFragment())
            viewModel.addFirebase()
        }

        binding.imageBackState.setOnClickListener {
            findNavController()
                .navigate(AddEventFragmentDirections.actionGlobalHomeFragment())
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 做你要的事，這邊是跳轉首頁
                findNavController().
                    navigate(R.id.action_global_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        tagList()
        viewModel.mark.value = tag
        viewModel.getTag()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val qrCodeFragment = QrCodeFragment()
        qrCodeFragment.setTargetFragment(this, REQUEST_EVALUATE)



    }

    private fun tagList() {
        tag.add(Tag(R.drawable.tag_breakfast,R.drawable.tag_breakfast_press,
            getString(R.string.tag_breakfast),false, getString(R.string.catalog_eat)))
        tag.add(Tag(R.drawable.tag_lunch,R.drawable.tag_lunch_press,
           getString(R.string.tag_lunch),false, getString(R.string.catalog_eat)))
        tag.add(Tag(R.drawable.tag_dinner,R.drawable.tag_dinner_press,
            getString(R.string.tag_dinner),false, getString(R.string.catalog_eat)))
        tag.add(Tag(R.drawable.tag_dessert,R.drawable.tag_dessert_press,
            getString(R.string.tag_dessert),false, getString(R.string.catalog_eat)))
        tag.add(Tag(R.drawable.tag_payment,R.drawable.tag_payment_press,
            getString(R.string.tag_payment),true, getString(R.string.catalog_income)))
        tag.add(Tag(R.drawable.tag_cloth,R.drawable.tag_cloth_press,
            getString(R.string.tag_cloth),false, getString(R.string.catalog_cloth)))
        tag.add(Tag(R.drawable.tag_live,R.drawable.tag_live_press,
            getString(R.string.tag_live),false, getString(R.string.catalog_live)))
        tag.add(Tag(R.drawable.tag_traffic,R.drawable.tag_traffic_press,
            getString(R.string.tag_traffic),false, getString(R.string.catalog_traffic)))
        tag.add(Tag(R.drawable.tag_fun,R.drawable.tag_fun_press,
            getString(R.string.tag_fun),false, getString(R.string.catalog_fun)))
        tag.add(Tag(R.drawable.tag_lottery,R.drawable.tag_lottery_press,
            getString(R.string.tag_lottery),true, getString(R.string.catalog_income)))
    }

}
