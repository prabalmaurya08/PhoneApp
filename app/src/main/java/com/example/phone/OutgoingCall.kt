package com.example.phone

import android.Manifest

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.telecom.TelecomManager
import android.net.Uri
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.phone.databinding.ActivityOutgoingCallBinding

class OutgoingCall : AppCompatActivity() {

    private lateinit var binding: ActivityOutgoingCallBinding
    private var phoneNumber: String? = null
    private var timer: CountDownTimer? = null
    private lateinit var telecomManager: TelecomManager

    // Permission request codes
    private val REQUEST_PERMISSION_CALL = 1
    private val REQUEST_PERMISSION_ANSWER_CALL = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using ViewBinding
        binding = ActivityOutgoingCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the phone number passed from DialFragment
        phoneNumber = intent.getStringExtra("PHONE_NUMBER")

        // Display the phone number in the UI
        binding.phoneNumberText.text = phoneNumber

        // Initialize TelecomManager
        telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager

        // Request permissions if not granted
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_PERMISSION_CALL
            )
        } else {
            // Start the outgoing call and timer if permissions are granted
            initiateCall()
            startCallTimer()
        }

        // Set the end call button action
        binding.endCallButton.setOnClickListener {
            endCall()
        }
    }

    private fun initiateCall() {
        if (phoneNumber != null && phoneNumber!!.isNotEmpty()) {
            val uri = Uri.parse("tel:$phoneNumber")

            // Initiate the call
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            telecomManager.placeCall(uri, null) // Initiating the call
            startListeningForCallState()
        }
    }

    private fun startListeningForCallState() {
        val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                super.onCallStateChanged(state, phoneNumber)
                when (state) {
                    TelephonyManager.CALL_STATE_RINGING -> {
                        // The phone is ringing, you can update the UI to show "Ringing"
                        binding.timerText.text = "Ringing..."
                    }
                    TelephonyManager.CALL_STATE_OFFHOOK -> {
                        // Call is answered, start the timer
                        binding.timerText.text = "Call in Progress"
                        startCallTimer()
                    }
                    TelephonyManager.CALL_STATE_IDLE -> {
                        // Call is ended, stop the timer and show the call duration
                        binding.timerText.text = "Call Ended"
                        timer?.cancel()
                    }
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE)
    }

    private fun startCallTimer() {
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000) % 60
                val minutes = (millisUntilFinished / 1000) / 60
                binding.timerText.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.timerText.text = "00:00"
            }
        }.start()
    }

    private fun endCall() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ANSWER_PHONE_CALLS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ANSWER_PHONE_CALLS),
                REQUEST_PERMISSION_ANSWER_CALL
            )
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            telecomManager.endCall() // End the call (requires permission)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_PERMISSION_CALL -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permissions granted, initiate call
                    initiateCall()
                    startCallTimer()
                } else {
                    // Permissions denied, show message
                    Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            REQUEST_PERMISSION_ANSWER_CALL -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, end call
                    endCall()
                } else {
                    // Permissions denied, show message
                    Toast.makeText(this, "Answer phone call permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop the timer if the activity is destroyed
        timer?.cancel()
    }
}
