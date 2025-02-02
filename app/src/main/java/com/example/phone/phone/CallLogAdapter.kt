package com.example.phone.phone

import android.content.Intent
import android.net.Uri
import android.provider.CallLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.phone.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CallLogAdapter(
    private val callLogs: List<CallLogItem>,
    private val onCallClick: (CallLogItem) -> Unit
) : RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.call_log_item, parent, false)
        return CallLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        val callLog = callLogs[position]
        holder.bind(callLog)
    }

    override fun getItemCount(): Int = callLogs.size

    inner class CallLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvPhoneNumber: TextView = itemView.findViewById(R.id.tvPhoneNumber)
        private val tvCallTime: TextView = itemView.findViewById(R.id.tvCallTime)
        private val icCallType: ImageView = itemView.findViewById(R.id.icCallType)


        fun bind(callLog: CallLogItem) {
            tvName.text = callLog.name
            tvPhoneNumber.text = callLog.number
            tvCallTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(callLog.date))


            // Set icon based on call type
            when (callLog.type) {
//                CallLog.Calls.INCOMING_TYPE -> icCallType.setImageResource(R.drawable.ic_incoming_call)
//                CallLog.Calls.OUTGOING_TYPE -> icCallType.setImageResource(R.drawable.ic_outgoing_call)
//                CallLog.Calls.MISSED_TYPE -> icCallType.setImageResource(R.drawable.ic_missed_call)
            }

            itemView.setOnClickListener {
                // Initiate call when tapping on the item
                onCallClick(callLog)
            }
        }

        private fun formatDuration(duration: Int): String {
            val minutes = duration / 60
            val seconds = duration % 60
            return String.format("%02d:%02d", minutes, seconds)
        }
    }
}
