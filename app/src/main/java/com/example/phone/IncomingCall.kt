package com.example.phone

import android.content.Context
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class IncomingCall : AppCompatActivity() {

    private lateinit var callNumberTextView: TextView
    private lateinit var answerButton: Button
    private lateinit var rejectButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_incoming_call)

        // Set up UI elements
        callNumberTextView = findViewById(R.id.tvCallerNumber)
        answerButton = findViewById(R.id.btnAnswer)
        rejectButton = findViewById(R.id.btnReject)

        // Edge-to-edge support
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize TelephonyManager and PhoneStateListener
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val incomingCallListener = IncomingCallListener(
            context = this,
            callNumberTextView = callNumberTextView,
            answerButton = answerButton,
            rejectButton = rejectButton
        )

        telephonyManager.listen(incomingCallListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the listener when the activity is destroyed
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val incomingCallListener = IncomingCallListener(
            context = this,
            callNumberTextView = callNumberTextView,
            answerButton = answerButton,
            rejectButton = rejectButton
        )
        telephonyManager.listen(incomingCallListener, PhoneStateListener.LISTEN_NONE)
    }
}
