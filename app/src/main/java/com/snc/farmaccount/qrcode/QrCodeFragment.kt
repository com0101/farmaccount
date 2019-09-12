package com.snc.farmaccount.qrcode


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Product
import com.snc.farmaccount.`object`.QrCode
import com.snc.farmaccount.databinding.FragmentQrCodeBinding
import com.snc.farmaccount.home.DayCalendarFragmentDirections


class QrCodeFragment : Fragment() {

    private lateinit var binding: FragmentQrCodeBinding
    private lateinit var codeScanner: CodeScanner
    var code = ArrayList<QrCode>()

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
                val time = it.text.substring(17, 23)

                Log.i("Sophie_16", "$price + $time")

                code.add(QrCode(price,time))
//                val values = it.text.substring(2) // Read List from somewhere
//                val lstValues: List<String> = values .split(":").map { it -> it.trim() }
//                lstValues.let {
//                    for (i in 0 until lstValues.size) {
//                        when (i % 3) {
//                            0 -> product?.name == it[i]
//                            1 -> product?.quantity == it[i]
//                            2 -> product?.price == it[i]
//                        }
//                        Log.i("Sophie_listvalue", "$it")
//                    }
//
//                }
//
//                lstValues.forEach { it ->
//                    it
//                    Log.i("Values", "value=$it")
//                    //Do Something
//                }
                if (it.text != null) {
                    this.findNavController()
                        .navigate(QrCodeFragmentDirections.actionGlobalAddEventFragment())
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
