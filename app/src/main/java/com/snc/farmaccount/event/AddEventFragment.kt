package com.snc.farmaccount.event


import android.app.Dialog
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
import com.snc.farmaccount.databinding.DialogCheckBinding
import com.snc.farmaccount.databinding.FragmentAddEventBinding
import com.snc.farmaccount.qrcode.QrCodeFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        QRviewModel.getMonth.observe(viewLifecycleOwner, Observer {
            Log.i("Sophie_qr","$it")
            if (QRviewModel.getMonth.value != null) {
                viewModel.monthFormat.value = it
                QRviewModel.getMonth.value = null
            }
        })

        QRviewModel.getTime.observe(viewLifecycleOwner, Observer {
            Log.i("Sophie_qr","$it")
            if (QRviewModel.getTime.value != null) {
                viewModel.time.value = it
                QRviewModel.getTime.value = null
            }
        })

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.tagList.adapter = TagAdapter(TagAdapter.OnClickListener {
            viewModel.chooseTag.value = it
            Log.i("Sophie_tag","$it")
        })

        binding.imageSave.setOnClickListener {
            var dialog = Dialog(this.requireContext())
            var bindingCheck = DialogCheckBinding.inflate(layoutInflater)
            dialog.setContentView(bindingCheck.root)
            viewModel.addFirebase()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            GlobalScope.launch(context = Dispatchers.Main) {
                bindingCheck.checkContent.text = "新增完成!"
                bindingCheck.imageCancel.visibility = View.GONE
                bindingCheck.imageSave.visibility = View.GONE
                dialog.show()
                delay(1500)
                dialog.dismiss()
                findNavController()
                    .navigate(EditEventFragmentDirections.actionGlobalHomeFragment())
            }
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
