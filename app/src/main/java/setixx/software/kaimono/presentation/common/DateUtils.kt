package setixx.software.kaimono.presentation.common

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    fun formatTimestamp(timestamp: String): String {
        return try {
            val parsedDate = ZonedDateTime.parse(timestamp)
            val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
            parsedDate.format(formatter)
        } catch (e: Exception) {
            timestamp
        }
    }
}
