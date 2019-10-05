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
import kotlinx.android.synthetic.main.dialog_check.*
import kotlinx.android.synthetic.main.fragment_add_event.image_save
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddEventFragment : Fragment() {

    private lateinit var binding: FragmentAddEventBinding
    val tag = ArrayList<Tag>()
    var REQUEST_EVALUATE = 0X110
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
            if (it.tag_status) {
                binding.textExpendTitle.setText(R.string.income_title)
            } else {
                binding.textExpendTitle.setText(R.string.expand_title)
            }
        })

        binding.imageSave.setOnClickListener {
            var dialog = Dialog(this.requireContext())
            var bindingCheck = DialogCheckBinding.inflate(layoutInflater)
            isClick = true
            dialog.setContentView(bindingCheck.root)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            when {
                viewModel.priceInput.value == null && viewModel.chooseTag.value == null &&viewModel.infoInput.value == null
                -> {
                    inputCheck = 0
                    warning()
                }
                viewModel.priceInput.value == null -> {
                    inputCheck = 1
                    warning()
                }
                viewModel.chooseTag.value == null -> {
                    inputCheck = 2
                    warning()
                }
                viewModel.infoInput.value == null -> {
                    inputCheck = 3
                    warning()
                }

            }

            if (viewModel.priceInput.value != null &&
                viewModel.chooseTag.value != null &&
                viewModel.infoInput.value != null) {
                viewModel.addFirebase()
                binding.imageSave.setImageResource(R.drawable.save_press)
                binding.imageSave.isClickable = false
                GlobalScope.launch(context = Dispatchers.Main) {
                    bindingCheck.checkContent.text = "新增完成!"
                    bindingCheck.imageCancel.visibility = View.GONE
                    bindingCheck.imageSave.visibility = View.GONE
                    dialog.show()
                    binding.imageSave.setImageResource(R.drawable.save)
                    binding.imageSave.isClickable = true
                    delay(1500)
                    dialog.dismiss()
                    findNavController()
                        .navigate(EditEventFragmentDirections.actionGlobalHomeFragment())
                }
            }
        }

        binding.imageBackState.setOnClickListener {
            when {
                viewModel.priceInput.value != null && viewModel.priceInput.value!!.isEmpty() -> checkEdit()
                viewModel.infoInput.value != null && viewModel.priceInput.value!!.isEmpty() -> checkEdit()
                viewModel.chooseTag.value != null -> checkEdit()
                else -> findNavController()
                    .navigate(AddEventFragmentDirections.actionGlobalHomeFragment())
            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 做你要的事，這邊是跳轉首頁
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

    private fun warning() {
        var warning = Dialog(this.requireContext())
        var bindingCheck = DialogCheckBinding.inflate(layoutInflater)
        warning.setContentView(bindingCheck.root)
        warning.window?.setBackgroundDrawableResource(android.R.color.transparent)
        GlobalScope.launch(context = Dispatchers.Main) {
            when (inputCheck) {
                0 -> bindingCheck.checkContent.text = "資訊沒填喔!"
                1 -> bindingCheck.checkContent.text = "價錢沒填喔!"
                2 -> bindingCheck.checkContent.text = "類別沒選喔!"
                3 -> bindingCheck.checkContent.text = "寫個描述吧!"
            }
            bindingCheck.imageCancel.visibility = View.GONE
            bindingCheck.imageSave.visibility = View.GONE
            warning.show()
            delay(1000)
            warning.dismiss()
        }
    }

    private fun checkEdit() {
        var checkEdit = Dialog(this.requireContext())
        var bindingCheck = DialogCheckBinding.inflate(layoutInflater)
        checkEdit.setContentView(bindingCheck.root)
        checkEdit.window?.setBackgroundDrawableResource(android.R.color.transparent)
        GlobalScope.launch(context = Dispatchers.Main) {
            bindingCheck.checkContent.setText(R.string.check_edit)
            checkEdit.show()
            checkEdit.image_save.setOnClickListener {
                checkEdit.dismiss()
                findNavController().
                    navigate(R.id.action_global_homeFragment)
            }
            checkEdit.image_cancel.setOnClickListener {
                checkEdit.dismiss()
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
