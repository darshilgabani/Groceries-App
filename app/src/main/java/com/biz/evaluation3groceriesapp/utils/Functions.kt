package com.biz.evaluation3groceriesapp.utils

import android.icu.text.SimpleDateFormat
import java.util.*

fun currentTime(): String {
    val currentDateTime = Calendar.getInstance().time
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val formattedTime = timeFormat.format(currentDateTime)

    return formattedTime
}

fun currentDate(): String {
    val currentDateTime = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(currentDateTime)

    return formattedDate
}

    




