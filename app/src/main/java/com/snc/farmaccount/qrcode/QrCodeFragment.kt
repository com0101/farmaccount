package com.snc.farmaccount.qrcode


import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import android.text.TextUtils.substring
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
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.snc.farmaccount.ApplicationContext
import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.FragmentQrCodeBinding
import com.snc.farmaccount.event.AddEventViewModel
import java.util.*
import java.text.SimpleDateFormat
import android.content.DialogInterface
import android.app.AlertDialog


class QrCodeFragment : Fragment() {

    private lateinit var binding: FragmentQrCodeBinding
    private lateinit var codeScanner: CodeScanner
    var camera = false

    private val viewModel: AddEventViewModel by lazy {
        ViewModelProviders.of(this).get(AddEventViewModel::class.java)
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

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 做你要的事，這邊是跳轉首頁
                findNavController().
                    navigate(R.id.action_global_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

//        if (ContextCompat.checkSelfPermission(ApplicationContext.applicationContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.CAMERA), 50)
//        }
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        codeScanner = CodeScanner(requireActivity(), binding.surfaceView)
        if (ContextCompat.checkSelfPermission(this.requireActivity(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 50)

        } else {
            codeScanner.decodeCallback = DecodeCallback {
                requireActivity().runOnUiThread {
                    Toast.makeText(this.requireContext(), it.text, Toast.LENGTH_LONG).show()
                    Log.i("Sophie_scan","${it.text}")
                    val price = Integer.parseInt( it.text.substring(30, 37), 16 ).toString()
                    val year = it.text.substring(10, 13).toInt()
                    val month = it.text.substring(13, 15).toInt()
                    val day = it.text.substring(15, 17).toInt()
                    Log.i("Sophie_day","$day")
                    val getDate = Date(year+11, month-1, day)
                    val simpledateformat = SimpleDateFormat("yyyy.MM.dd (EEEE)")
                    val dateformat = SimpleDateFormat("yyyyMMdd")
                    val time = simpledateformat.format(getDate)
                    val getMonth = time.substring(5, 7)
                    val date = dateformat.format(getDate)
                    val codeViewModel: AddEventViewModel by lazy {
                        ViewModelProviders.of(activity!!).get(AddEventViewModel::class.java)
                    }
                    if (it.text != null) {
                        this.findNavController()
                            .navigate(QrCodeFragmentDirections.actionGlobalAddEventFragment())
                        codeViewModel.someDay.value = time
                        codeViewModel.getPrice.value = price
                        codeViewModel.getMonth.value = getMonth
                        codeViewModel.getTime.value = date
                    }

                }
            }
            codeScanner.startPreview()
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d("Sophie", "onResume")
        if (ContextCompat.checkSelfPermission(this.requireActivity(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("Sophie", "onPause")
        codeScanner.releaseResources()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            50 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    findNavController()
                        .navigate(R.id.action_global_homeFragment)
                }
                return
            }
            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}
