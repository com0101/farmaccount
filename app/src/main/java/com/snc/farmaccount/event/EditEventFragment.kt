package com.snc.farmaccount.event


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView

import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Tag
import com.snc.farmaccount.databinding.DialogCheckBinding
import com.snc.farmaccount.databinding.FragmentEditEventBinding
import com.snc.farmaccount.detail.DetailFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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

        binding.tagList.adapter = TagEditAdapter(TagEditAdapter.OnClickListener {
            viewModel.chooseTag.value = it
            if (viewModel.detail.value?.tag != it.tag_name) {
                (binding.tagList.adapter as TagEditAdapter).select = false
            }

        },viewModel)

        binding.imageSave.setOnClickListener {
            var dialog = Dialog(this.requireContext())
            var bindingCheck = DialogCheckBinding.inflate(layoutInflater)
            dialog.setContentView(bindingCheck.root)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            viewModel.editFirebase()
            GlobalScope.launch(context = Dispatchers.Main) {
                delay(1000)
                bindingCheck.checkContent.text = "編輯完成!"
                bindingCheck.imageCancel.visibility = View.GONE
                bindingCheck.imageSave.visibility = View.GONE
                dialog.show()
                delay(1000)
                dialog.dismiss()
                findNavController()
                    .navigate(EditEventFragmentDirections.actionGlobalHomeFragment())
            }
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

        return binding.root
    }

    private fun tagList() {
        tag.add(Tag(R.drawable.tag_egg_press,R.drawable.tag_egg,
            getString(R.string.tag_breakfast),false, getString(R.string.catalog_eat)))
        tag.add(Tag(R.drawable.tag_pig_press,R.drawable.tag_pig,
            getString(R.string.tag_lunch),false, getString(R.string.catalog_eat)))
        tag.add(Tag(R.drawable.tag_cow_press,R.drawable.tag_cow,
            getString(R.string.tag_dinner),false, getString(R.string.catalog_eat)))
        tag.add(Tag(R.drawable.tag_ginger_press,R.drawable.tag_ginger,
            getString(R.string.tag_dessert),false, getString(R.string.catalog_eat)))
        tag.add(Tag(R.drawable.tag_money_press,R.drawable.tag_money,
            getString(R.string.tag_payment),true, getString(R.string.catalog_income)))
        tag.add(Tag(R.drawable.tag_cloth_press,R.drawable.tag_cloth,
            getString(R.string.tag_cloth),false, getString(R.string.catalog_cloth)))
        tag.add(Tag(R.drawable.tag_live_press,R.drawable.tag_live,
            getString(R.string.tag_live),false, getString(R.string.catalog_live)))
        tag.add(Tag(R.drawable.tag_traffic_press,R.drawable.tag_traffic,
            getString(R.string.tag_traffic),false, getString(R.string.catalog_traffic)))
        tag.add(Tag(R.drawable.tag_fun_press,R.drawable.tag_fun,
            getString(R.string.tag_fun),false, getString(R.string.catalog_fun)))
        tag.add(Tag(R.drawable.tag_ticket_press,R.drawable.tag_ticket,
            getString(R.string.tag_lottery),true, getString(R.string.catalog_income)))
    }


}
