package com.example.phone.phone

data class CallLogItem(
    val id: Long,
    val name: String,
    val number: String,
    val date: Long,
    val type: Int,
    val duration: Int
)

