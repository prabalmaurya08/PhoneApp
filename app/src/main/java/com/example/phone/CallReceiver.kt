package com.example.phone



import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            when (state) {
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    // Incoming call is ringing
                    incomingNumber?.let {
                        showIncomingCallUI(context, it)
                    }
                }
                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    // Call is answered (active call)
                }
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    // Call ended
                }
            }
        }
    }

    private fun showIncomingCallUI(context: Context?, phoneNumber: String) {
        val intent = Intent(context, IncomingCall::class.java).apply {
            putExtra("PHONE_NUMBER", phoneNumber)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context?.startActivity(intent)
    }
}
