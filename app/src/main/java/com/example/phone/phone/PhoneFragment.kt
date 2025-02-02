package com.example.phone.phone

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phone.databinding.FragmentPhoneBinding

class PhoneFragment : Fragment() {
    private lateinit var binding: FragmentPhoneBinding
    private lateinit var callLogAdapter: CallLogAdapter

    private var callTypeFilter: Int? = null // Store selected filter
    private var allCallLogs: List<CallLogItem> = emptyList()

    // Request permissions launcher for both READ_CALL_LOG and CALL_PHONE
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions[android.Manifest.permission.READ_CALL_LOG] == true && permissions[android.Manifest.permission.CALL_PHONE] == true) {
            loadCallLogs() // Permission granted, load call logs
        } else {
            Toast.makeText(requireContext(), "Permissions are required to access call logs and make calls", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check for permissions before performing any actions
        checkPermissionsAndLoadLogs()

        setupSpinner()
        setupRecyclerView()

        // Open dial pad fragment when clicked
        binding.openDailPad.setOnClickListener {
            val dialPad = DialPadFragment()
            dialPad.show(parentFragmentManager, "DialPadFragment")
        }
    }

    private fun setupSpinner() {
        val filters = arrayOf("All Calls", "Incoming Calls", "Missed Calls", "Outgoing Calls")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, filters)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                callTypeFilter = when (position) {
                    1 -> CallLog.Calls.INCOMING_TYPE
                    2 -> CallLog.Calls.MISSED_TYPE
                    3 -> CallLog.Calls.OUTGOING_TYPE
                    else -> null
                }
                checkPermissionsAndLoadLogs() // Refresh the call log list based on the filter
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun checkPermissionsAndLoadLogs() {
        // Check if both permissions are granted before loading call logs
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            loadCallLogs()
        } else {
            // Request permissions if not granted
            requestPermissionLauncher.launch(
                arrayOf(android.Manifest.permission.READ_CALL_LOG, android.Manifest.permission.CALL_PHONE)
            )
        }
    }

    private fun setupRecyclerView() {
        callLogAdapter = CallLogAdapter(emptyList()) { callLog ->
            // Handle call initiation when an item is clicked
            initiateCall(callLog.number)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = callLogAdapter
        }
    }

    private fun loadCallLogs() {
        val cursor = requireContext().contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            arrayOf(
                CallLog.Calls._ID,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE,
                CallLog.Calls.DURATION
            ),
            null,
            null,
            CallLog.Calls.DATE + " DESC"
        )

        cursor?.use {
            allCallLogs = mutableListOf<CallLogItem>().apply {
                while (it.moveToNext()) {
                    val id = getLongSafely(it, CallLog.Calls._ID)
                    val number = getStringSafely(it, CallLog.Calls.NUMBER)
                    val date = getLongSafely(it, CallLog.Calls.DATE)
                    val type = getIntSafely(it, CallLog.Calls.TYPE)
                    val duration = getIntSafely(it, CallLog.Calls.DURATION)

                    // Add contact name if available
                    val contactName = getContactName(number) ?: number
                    add(CallLogItem(id, contactName, number, date, type, duration))
                }
            }
        }

        // Filter the call logs based on the selected filter
        val filteredLogs = if (callTypeFilter == null) {
            allCallLogs
        } else {
            allCallLogs.filter { it.type == callTypeFilter }
        }

        callLogAdapter = CallLogAdapter(filteredLogs) { callLog ->
            initiateCall(callLog.number)
        }
        binding.recyclerView.adapter = callLogAdapter
    }

    // Helper function to safely get Long values
    private fun getLongSafely(cursor: Cursor, columnName: String): Long {
        val columnIndex = cursor.getColumnIndex(columnName)
        return if (columnIndex >= 0) cursor.getLong(columnIndex) else 0L
    }

    // Helper function to safely get String values
    private fun getStringSafely(cursor: Cursor, columnName: String): String {
        val columnIndex = cursor.getColumnIndex(columnName)
        return if (columnIndex >= 0) cursor.getString(columnIndex) else ""
    }

    // Helper function to safely get Int values
    private fun getIntSafely(cursor: Cursor, columnName: String): Int {
        val columnIndex = cursor.getColumnIndex(columnName)
        return if (columnIndex >= 0) cursor.getInt(columnIndex) else 0
    }
    private fun getContactName(phoneNumber: String): String? {
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED) {
            // Handle permission denial (you can request permission or return null)
            return null
        }

        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phoneNumber)
        )

        val cursor = requireContext().contentResolver.query(
            uri,
            arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME),
            null, null, null
        )

        cursor?.use {
            val nameColumnIndex = it.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)
            if (nameColumnIndex >= 0 && it.moveToFirst()) {
                return it.getString(nameColumnIndex)
            }
        }
        return null
    }


    private fun initiateCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
        if (requireContext().checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "Permission to make calls is required", Toast.LENGTH_SHORT).show()
        }
    }
}
