package com.gordon.stackoverflow.ui

import org.jsoup.Jsoup
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

fun formatDate(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val prettyTime = PrettyTime(Locale.getDefault())
    return prettyTime.format(date)
}

fun dateFormated(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val format = java.text.SimpleDateFormat("MMM dd yyyy 'at' HH:mm", Locale.getDefault())
    return format.format(date)
}

fun stripHtml(html: String): String {
    return Jsoup.parse(html).text()
}