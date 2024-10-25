package com.example.inventarisapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
        timeZone = java.util.TimeZone.getTimeZone("UTC") // Sesuaikan ke UTC
    }

    fun parseDate(dateString: String): Date? {
        return try {
            apiDateFormat.parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun formatDateString(dateString: String): String {
        val desiredFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale("id", "ID")) // Tampilkan dengan waktu
        return try {
            val date = apiDateFormat.parse(dateString)
            desiredFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            dateString
        }
    }
}