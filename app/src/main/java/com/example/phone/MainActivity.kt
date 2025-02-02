package com.example.phone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.telecom.TelecomManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.phone.contact.ContactFragment
import com.example.phone.databinding.ActivityMainBinding
import com.example.phone.phone.PhoneFragment





class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val PREFS_NAME = "app_preferences"
    private val PREF_PROMPT_SHOWN = "prompt_shown"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // ✅ Fix this line

        enableEdgeToEdge()

        // Get SharedPreferences to track the prompt status
        val sharedPrefs: SharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val promptShown = sharedPrefs.getBoolean(PREF_PROMPT_SHOWN, false)

        // Check if the app is set as the default dialer
        val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
        val currentDefaultDialerPackage = telecomManager.defaultDialerPackage

        // If your app isn't set as default and the prompt hasn't been shown yet
        if (currentDefaultDialerPackage != packageName && !promptShown) {
            // Open the default apps settings page
            val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
            startActivity(intent)

            // Store that the prompt has been shown
            with(sharedPrefs.edit()) {
                putBoolean(PREF_PROMPT_SHOWN, true)
                apply()
            }
        }

        // Load default fragment (PhoneFragment)
        if (savedInstanceState == null) {
            replaceFragment(PhoneFragment())
        }

        // Set up Bottom Navigation listener
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.phone -> replaceFragment(PhoneFragment()) // ✅ Make sure IDs match
                R.id.contact -> replaceFragment(ContactFragment()) // ✅ Make sure IDs match
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, fragment)
            .commit()
    }
}
