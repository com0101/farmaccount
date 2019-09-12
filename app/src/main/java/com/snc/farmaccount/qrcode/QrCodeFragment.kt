package com.snc.farmaccount.qrcode


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.QrCode
import com.snc.farmaccount.databinding.FragmentQrCodeBinding
import com.snc.farmaccount.event.AddEventViewModel
import java.text.SimpleDateFormat
import java.util.*


class QrCodeFragment : Fragment() {

    private lateinit var binding: FragmentQrCodeBinding
    private lateinit var codeScanner: CodeScanner

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
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        codeScanner = CodeScanner(requireActivity(), binding.surfaceView)
        codeScanner.decodeCallback = DecodeCallback {
            requireActivity().runOnUiThread {
                Toast.makeText(this.requireContext(), it.text, Toast.LENGTH_LONG).show()
                Log.i("Sophie_scan","${it.text}")
                val price = Integer.parseInt( it.text.substring(30, 37), 16 ).toString()
                val year = it.text.substring(10, 13).toInt()
                val month = it.text.substring(14, 15).toInt()
                val day = it.text.substring(16, 17).toInt()
                val getDate = Date(year+11, month-1, day+10)
                val simpledateformat = SimpleDateFormat("yyyy.MM.dd (EEEE)")
                val time = simpledateformat.format(getDate)
                val codeViewModel: AddEventViewModel by lazy {
                    ViewModelProviders.of(activity!!).get(AddEventViewModel::class.java)
                }
                if (it.text != null) {
                    this.findNavController()
                        .navigate(QrCodeFragmentDirections.actionGlobalAddEventFragment())
                    codeViewModel.someDay.value = time
                    codeViewModel.getPrice.value = price
                }

            }
        }
        binding.surfaceView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}
