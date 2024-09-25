package com.testapp.coding.moviesearch

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    companion object {
        fun formatReleaseDate(dateValue: String): String {
            // Some of the release dates are empty so we make sure we do not get any parse exceptions
            return try {
                DateFormat.getDateInstance()
                    .format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateValue))
            } catch (e: Exception) {
                ""
            }
        }
    }
}