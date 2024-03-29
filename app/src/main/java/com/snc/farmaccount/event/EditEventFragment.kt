package com.snc.farmaccount.event


import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.snc.farmaccount.MainActivity
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Tag
import com.snc.farmaccount.databinding.DialogCheckBinding
import com.snc.farmaccount.databinding.FragmentEditEventBinding
import kotlinx.android.synthetic.main.dialog_check.*
import kotlinx.android.synthetic.main.fragment_add_event.image_save
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class EditEventFragment : Fragment() {

    private lateinit var binding: FragmentEditEventBinding
    private lateinit var warningDialog: Dialog
    private lateinit var bindingCheck: DialogCheckBinding
    val tag = ArrayList<Tag>()
    var inputCheck = 0

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
            when {
                viewModel.priceInput.value != viewModel.detail.value?.price -> checkEdit()

                viewModel.infoInput.value != viewModel.detail.value?.description -> checkEdit()

                viewModel.chooseTag.value?.tag_name != viewModel.detail.value?.tag -> checkEdit()

                else -> findNavController()
                        .navigate(R.id.action_global_homeFragment)
            }
        }

        binding.tagList.adapter = TagEditAdapter(TagEditAdapter.OnClickListener {
            viewModel.chooseTag.value = it

            if (viewModel.detail.value?.tag != it.tag_name) {
                (binding.tagList.adapter as TagEditAdapter).select = false
            }

            if (it.tag_status) {
                binding.textExpendTitle.setText(R.string.income_title)
            } else {
                binding.textExpendTitle.setText(R.string.expand_title)
            }

        },viewModel)

        binding.imageSave.setOnClickListener {

            when {
                viewModel.priceInput.value == null &&
                viewModel.chooseTag.value == null &&
                viewModel.infoInput.value == null -> {
                    inputCheck = 0
                    warningDialog()
                }

                viewModel.priceInput.value!!.isEmpty() -> {
                    inputCheck = 1
                    warningDialog()
                }

                viewModel.tagName.value == null -> {
                    inputCheck = 2
                    warningDialog()
                }

                viewModel.infoInput.value!!.isEmpty() -> {
                    inputCheck = 3
                    warningDialog()
                }

                else ->{
                    viewModel.editEvent()
                    binding.imageSave.setImageResource(R.drawable.save_press)
                    binding.imageSave.isClickable = false
                    inputCheck = 4
                    warningDialog()
                }

            }

        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 做你要的事，這邊是跳轉首頁
                when {
                    viewModel.priceInput.value != viewModel.detail.value?.price -> checkEdit()
                    viewModel.infoInput.value != viewModel.detail.value?.description -> checkEdit()
                    viewModel.chooseTag.value?.tag_name != viewModel.detail.value?.tag -> checkEdit()
                    viewModel.priceInput.value == viewModel.detail.value?.price &&
                    viewModel.infoInput.value == viewModel.detail.value?.description &&
                    viewModel.tagName.value == viewModel.detail.value?.tag ->
                        findNavController()
                            .navigate(AddEventFragmentDirections.actionGlobalHomeFragment())
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        tagList()
        viewModel.mark.value = tag
        viewModel.setTag()

        return binding.root
    }

    private fun warningDialog() {
        showCheckDialog()
        bindingCheck.imageCancel.visibility = View.GONE
        bindingCheck.imageSave.visibility = View.GONE

        GlobalScope.launch(context = Dispatchers.Main) {
            when (inputCheck) {
                0 -> bindingCheck.checkContent.setText(R.string.event_info)

                1 -> bindingCheck.checkContent.setText(R.string.event_price)

                2 -> bindingCheck.checkContent.setText(R.string.event_tag)

                3 -> bindingCheck.checkContent.setText(R.string.event_description)

                4 -> {
                    bindingCheck.checkContent.setText(R.string.edit_complete)
                    binding.imageSave.setImageResource(R.drawable.save)
                    binding.imageSave.isClickable = true
                    delay(500)
                    findNavController()
                        .navigate(R.id.action_global_homeFragment)
                }
            }
            warningDialog.show()
            delay(1000)
            warningDialog.dismiss()
        }
    }

    private fun checkEdit() {
        showCheckDialog()

        GlobalScope.launch(context = Dispatchers.Main) {
            bindingCheck.checkContent.setText(R.string.check_edit)
            warningDialog.show()
            warningDialog.image_save.setOnClickListener {
                warningDialog.dismiss()
                findNavController().
                    navigate(R.id.action_global_homeFragment)
            }

            warningDialog.image_cancel.setOnClickListener {
                warningDialog.dismiss()
            }
        }
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
    fun showCheckDialog() {
        warningDialog = Dialog(this.requireContext())
        bindingCheck = DialogCheckBinding.inflate(layoutInflater)
        warningDialog.setContentView(bindingCheck.root)
        warningDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

}
