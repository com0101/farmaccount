package com.snc.farmaccount.event


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Tag
import com.snc.farmaccount.databinding.DialogCheckBinding
import com.snc.farmaccount.databinding.FragmentAddEventBinding
import kotlinx.android.synthetic.main.dialog_check.*
import kotlinx.android.synthetic.main.fragment_add_event.image_save
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddEventFragment : Fragment() {

    private lateinit var binding: FragmentAddEventBinding
    private lateinit var checkDialog: Dialog
    private lateinit var bindingCheck: DialogCheckBinding
    val tag = ArrayList<Tag>()
    var inputCheck = 0
    var isClick = false

    private val viewModel: AddEventViewModel by lazy {
        ViewModelProviders.of(this).get(AddEventViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddEventBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val codeViewModel: AddEventViewModel by lazy {
            ViewModelProviders.of(activity!!).get(AddEventViewModel::class.java)
        }

        codeViewModel.someDay.observe(viewLifecycleOwner, Observer {
            if (codeViewModel.someDay.value != null) {
                viewModel.today.value = it
                codeViewModel.someDay.value = null
            }
        })

        codeViewModel.getPrice.observe(viewLifecycleOwner, Observer {
            if (codeViewModel.getPrice.value != null) {
                viewModel.priceInput.value = it
                codeViewModel.getPrice.value = null
            }
        })

        codeViewModel.getMonth.observe(viewLifecycleOwner, Observer {
            if (codeViewModel.getMonth.value != null) {
                viewModel.monthly.value = it
                codeViewModel.getMonth.value = null
            }
        })

        codeViewModel.getTime.observe(viewLifecycleOwner, Observer {
            if (codeViewModel.getTime.value != null) {
                viewModel.time.value = it.toLong()
                codeViewModel.getTime.value = null
            }
        })

        binding.tagList.adapter = TagAdapter(TagAdapter.OnClickListener {
            viewModel.chooseTag.value = it
            Log.i("Sophie_tag","$it")
            if (it.tag_status) {
                binding.textExpendTitle.setText(R.string.income_title)
            } else {
                binding.textExpendTitle.setText(R.string.expand_title)
            }
        })

        binding.imageSave.setOnClickListener {
            checkDialog = Dialog(this.requireContext())
            bindingCheck = DialogCheckBinding.inflate(layoutInflater)
            isClick = true
            checkDialog.setContentView(bindingCheck.root)
            checkDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            when {
                viewModel.priceInput.value == null &&
                viewModel.chooseTag.value == null &&
                viewModel.infoInput.value == null -> {
                    inputCheck = 0
                    warningDialog()
                }

                viewModel.priceInput.value == null ||
                viewModel.priceInput.value!!.isEmpty() -> {
                    inputCheck = 1
                    warningDialog()
                }

                viewModel.chooseTag.value == null -> {
                    inputCheck = 2
                    warningDialog()
                }

                viewModel.infoInput.value == null ||
                viewModel.infoInput.value!!.isEmpty() -> {
                    inputCheck = 3
                    warningDialog()
                }

                else -> {
                    viewModel.addEvent()
                    inputCheck = 4
                    binding.imageSave.setImageResource(R.drawable.save_press)
                    binding.imageSave.isClickable = false
                    warningDialog()
                }
            }

        }

        binding.imageBackState.setOnClickListener {
            when {
                viewModel.priceInput.value != null ||
                        viewModel.priceInput.value!!.isNotEmpty() -> checkEdit()

                viewModel.infoInput.value != null ||
                        viewModel.priceInput.value!!.isNotEmpty() -> checkEdit()

                viewModel.chooseTag.value != null -> checkEdit()

                else -> findNavController()
                    .navigate(AddEventFragmentDirections.actionGlobalHomeFragment())
            }
        }

        backState()
        tagList()
        viewModel.mark.value = tag
        viewModel.setTag()
        return binding.root
    }

    private fun backState() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when {
                    viewModel.priceInput.value != null -> checkEdit()
                    viewModel.infoInput.value != null -> checkEdit()
                    viewModel.chooseTag.value != null -> checkEdit()
                    else -> findNavController()
                        .navigate(AddEventFragmentDirections.actionGlobalHomeFragment())
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun warningDialog() {
        checkDialog = Dialog(this.requireContext())
        bindingCheck = DialogCheckBinding.inflate(layoutInflater)
        checkDialog.setContentView(bindingCheck.root)
        checkDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        GlobalScope.launch(context = Dispatchers.Main) {
            bindingCheck.imageCancel.visibility = View.GONE
            bindingCheck.imageSave.visibility = View.GONE
            when (inputCheck) {
                0 -> bindingCheck.checkContent.setText(R.string.event_info)
                1 -> bindingCheck.checkContent.setText(R.string.event_price)
                2 -> bindingCheck.checkContent.setText(R.string.event_tag)
                3 -> bindingCheck.checkContent.setText(R.string.event_description)
                4 -> {
                    bindingCheck.checkContent.setText(R.string.add_complete)
                    binding.imageSave.setImageResource(R.drawable.save)
                    binding.imageSave.isClickable = true
                    delay(1000)
                    findNavController()
                        .navigate(EditEventFragmentDirections.actionGlobalHomeFragment())
                }
            }
            checkDialog.show()
            delay(1000)
            checkDialog.dismiss()
        }
    }

    private fun checkEdit() {
        checkDialog = Dialog(this.requireContext())
        bindingCheck = DialogCheckBinding.inflate(layoutInflater)
        checkDialog.setContentView(bindingCheck.root)
        checkDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        GlobalScope.launch(context = Dispatchers.Main) {
            bindingCheck.checkContent.setText(R.string.check_edit)
            checkDialog.show()

            checkDialog.image_save.setOnClickListener {
                checkDialog.dismiss()
                findNavController().
                    navigate(R.id.action_global_homeFragment)
            }

            checkDialog.image_cancel.setOnClickListener {
                checkDialog.dismiss()
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

}
