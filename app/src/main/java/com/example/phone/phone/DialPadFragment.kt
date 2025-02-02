package com.example.phone.phone


import android.content.Intent

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.phone.OutgoingCall


import com.example.phone.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DialPadFragment : BottomSheetDialogFragment() {

    private lateinit var tvPhoneNumber: TextView
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var btn4: Button
    private lateinit var btn5: Button
    private lateinit var btn6: Button
    private lateinit var btn7: Button
    private lateinit var btn8: Button
    private lateinit var btn9: Button
    private lateinit var btn0: Button
    private lateinit var btnStar: Button
    private lateinit var btnHash: Button
    private lateinit var btnCall: ImageButton
    private lateinit var btnDelete: ImageButton

    private var phoneNumber = StringBuilder()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dial_pad, container, false)

        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber)
        btn1 = view.findViewById(R.id.btn1)
        btn2 = view.findViewById(R.id.btn2)
        btn3 = view.findViewById(R.id.btn3)
        btn4 = view.findViewById(R.id.btn4)
        btn5 = view.findViewById(R.id.btn5)
        btn6 = view.findViewById(R.id.btn6)
        btn7 = view.findViewById(R.id.btn7)
        btn8 = view.findViewById(R.id.btn8)
        btn9 = view.findViewById(R.id.btn9)
        btn0 = view.findViewById(R.id.btn0)
        btnStar = view.findViewById(R.id.btnStar)
        btnHash = view.findViewById(R.id.btnHash)
        btnCall = view.findViewById(R.id.btnCall)
        btnDelete = view.findViewById(R.id.back)

        // Set click listeners for number buttons
        btn1.setOnClickListener { appendNumber("1") }
        btn2.setOnClickListener { appendNumber("2") }
        btn3.setOnClickListener { appendNumber("3") }
        btn4.setOnClickListener { appendNumber("4") }
        btn5.setOnClickListener { appendNumber("5") }
        btn6.setOnClickListener { appendNumber("6") }
        btn7.setOnClickListener { appendNumber("7") }
        btn8.setOnClickListener { appendNumber("8") }
        btn9.setOnClickListener { appendNumber("9") }
        btn0.setOnClickListener { appendNumber("0") }
        btnStar.setOnClickListener { appendNumber("*") }
        btnHash.setOnClickListener { appendNumber("#") }

        // Set click listener for delete button
        btnDelete.setOnClickListener { deleteLastChar() }

        // Set click listener for call button
        btnCall.setOnClickListener { initiateCall() }

        return view
    }

    private fun appendNumber(number: String) {
        phoneNumber.append(number)
        updatePhoneNumberDisplay()
    }

    private fun deleteLastChar() {
        if (phoneNumber.isNotEmpty()) {
            phoneNumber.deleteCharAt(phoneNumber.length - 1)
            updatePhoneNumberDisplay()
        }
    }

    private fun updatePhoneNumberDisplay() {
        tvPhoneNumber.text = phoneNumber.toString()
    }


    private fun initiateCall() {
        val numberToDial = phoneNumber.toString()
        if (numberToDial.isNotEmpty()) {
            // Create an Intent to dial the number
//            val intent = Intent(Intent.ACTION_CALL)
//            intent.data = Uri.parse("tel:$numberToDial")numberToDial


            // Create an Intent to start the OutgoingCall activity
            val intent = Intent(requireContext(), OutgoingCall::class.java)
            intent.putExtra("PHONE_NUMBER", numberToDial)  // Pass the phone number to OutgoingCall activity

            // Check for CALL_PHONE permission
            if (activity?.checkSelfPermission(android.Manifest.permission.CALL_PHONE) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                startActivity(intent)
                dismiss()  // Close the dial pad
            } else {

                Toast.makeText(requireContext(), "Call permission denied", Toast.LENGTH_SHORT).show()
                // Permission not granted, handle it
                // You can show a permission request dialog here
            }
        }
    }

}
