package com.example.phone

import android.content.Context
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.telecom.TelecomManager

class IncomingCallListener(
    private val context: Context,
    private val callNumberTextView: TextView,
    private val answerButton: Button,
    private val rejectButton: Button
) : PhoneStateListener() {

    override fun onCallStateChanged(state: Int, incomingNumber: String?) {
        super.onCallStateChanged(state, incomingNumber)

        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                // Incoming call detected
                handleIncomingCall(incomingNumber)
            }
            TelephonyManager.CALL_STATE_OFFHOOK -> {
                // Call answered
                handleCallAnswered()
            }
            TelephonyManager.CALL_STATE_IDLE -> {
                // Call ended
                handleCallEnded()
            }
        }
    }

    private fun handleIncomingCall(incomingNumber: String?) {
        // Update the UI to show the incoming call number
        callNumberTextView.text = "Incoming Call: $incomingNumber"

        // Enable the answer and reject buttons
        answerButton.isEnabled = true
        rejectButton.isEnabled = true

        // Handle button clicks
        answerButton.setOnClickListener {
            answerCall()
        }

        rejectButton.setOnClickListener {
            rejectCall()
        }
    }

    private fun handleCallAnswered() {
        // Call answered
        Toast.makeText(context, "Call answered", Toast.LENGTH_SHORT).show()
    }

    private fun handleCallEnded() {
        // Call ended, reset the UI
        Toast.makeText(context, "Call ended", Toast.LENGTH_SHORT).show()
    }

    private fun answerCall() {
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        telecomManager.acceptRingingCall()
        // After answering, transition to an active call screen if necessary
    }

    private fun rejectCall() {
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        telecomManager.endCall()
    }
}
