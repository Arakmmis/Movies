package yassir.moviesapp.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String.getLocalDateFromCustomFormat(): LocalDate {
    val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return LocalDate.parse(this, pattern)
}