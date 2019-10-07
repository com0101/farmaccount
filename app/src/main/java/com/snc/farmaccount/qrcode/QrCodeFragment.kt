package com.snc.farmaccount.qrcode


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.FragmentQrCodeBinding
import com.snc.farmaccount.event.AddEventViewModel
import java.util.*
import java.text.SimpleDateFormat
import android.app.Dialog
import android.os.Build
import com.snc.farmaccount.databinding.DialogCheckBinding
import com.snc.farmaccount.helper.Format
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class QrCodeFragment : Fragment() {

    private lateinit var binding: FragmentQrCodeBinding
    private lateinit var codeScanner: CodeScanner
    private lateinit var warningDialog: Dialog
    private lateinit var bindingCheck: DialogCheckBinding
    var camera = 0

    private val codeViewModel: AddEventViewModel by lazy {
        ViewModelProviders.of(activity!!).get(AddEventViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentQrCodeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.imageBackState.setOnClickListener {
            findNavController()
                .navigate(QrCodeFragmentDirections.actionGlobalHomeFragment())
        }
        backState()

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        codeScanner = CodeScanner(requireActivity(), binding.surfaceView)

        if (ContextCompat.checkSelfPermission(this.requireActivity(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 50)
        } else {
            codeScanner.decodeCallback = DecodeCallback {
                requireActivity().runOnUiThread {
                    Toast.makeText(this.requireContext(), it.text, Toast.LENGTH_LONG).show()

                    if (it.text.trim().length < 44 || it.text.trim().isEmpty()) {
                        camera = 3
                        warning()
                    } else {
                        val price = Integer.parseInt( it.text.substring(30, 37),
                            16 ).toString()
                        val year = it.text.substring(10, 13).toInt()
                        val month = it.text.substring(13, 15).toInt()
                        val day = it.text.substring(15, 17).toInt()
                        val getDate = Date(year+11, month-1, day)
                        val time = Format.getSimpleDateFormat(getDate)
                        val getMonth = time.substring(5, 7)
                        val date = Format.getDateFormat(getDate)

                        if (it.text != null) {
                            this.findNavController()
                                .navigate(R.id.action_global_addEventFragment)
                            codeViewModel.getDate.value = time
                            codeViewModel.getPrice.value = price
                            codeViewModel.getMonth.value = getMonth
                            codeViewModel.getTime.value = date
                        }
                    }
                }
            }
            codeScanner.startPreview()
        }

        binding.surfaceView.setOnClickListener {
            codeScanner.startPreview()

        }

    }

    override fun onResume() {
        super.onResume()
        Log.d("Sophie", "onResume")

        if (ContextCompat.checkSelfPermission(this.requireActivity(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("Sophie", "onPause")
        codeScanner.releaseResources()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            50 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED)) {
                    restart()
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this.requireActivity(),
                            Manifest.permission.CAMERA)) {
                        camera = 1
                        warning()
                        // now, user has denied permission (but not permanently!)
                    }else {
                        camera = 2
                        warning()
                        // now, user has denied permission permanently!
                    }
                }
                return
            }

        }
    }

    private fun restart(){
        val fragment = fragmentManager?.beginTransaction()
        if (Build.VERSION.SDK_INT >= 26) {
            fragment?.setReorderingAllowed(false)
        }
        fragment?.detach(this)?.attach(this)?.commit()
    }

    private fun warning() {
        showCheckDialog()

        GlobalScope.launch(context = Dispatchers.Main) {
            when (camera) {
                1 -> {
                    bindingCheck.checkContent.setText(R.string.camera_permission_advice)
                    warningDialog.show()

                    bindingCheck.imageSave.setOnClickListener {
                        warningDialog.dismiss()
                        requestPermissions(arrayOf(Manifest.permission.CAMERA), 50)
                    }

                    bindingCheck.imageCancel.setOnClickListener {
                        warningDialog.dismiss()
                        findNavController()
                            .navigate(R.id.action_global_homeFragment)
                    }
                }

                2 -> {
                    bindingCheck.checkContent.setText(R.string.camera_permission_set)
                    bindingCheck.imageCancel.visibility = View.GONE
                    bindingCheck.imageSave.visibility = View.GONE
                    warningDialog.show()
                    delay(1500)
                    warningDialog.dismiss()
                    delay(1000)
                    findNavController()
                        .navigate(R.id.action_global_homeFragment)
                }

                3 -> {
                    bindingCheck.checkContent.text = "掃錯邊嘍~"
                    bindingCheck.imageCancel.visibility = View.GONE
                    bindingCheck.imageSave.visibility = View.GONE
                    warningDialog.show()
                    delay(1500)
                    warningDialog.dismiss()
                }
            }
        }
    }

    private fun backState() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().
                    navigate(R.id.action_global_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun showCheckDialog() {
        warningDialog = Dialog(this.requireContext())
        bindingCheck = DialogCheckBinding.inflate(layoutInflater)
        warningDialog.setContentView(bindingCheck.root)
        warningDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
