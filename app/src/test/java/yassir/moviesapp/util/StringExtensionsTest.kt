package yassir.moviesapp.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeParseException

class StringExtensionsTest {

    @Test
    fun `for a date string in yyyy-MM-dd, return LocalDate`() {
        val date = "2018-10-29"
        val expectedValue = LocalDate.of(2018, 10, 29)

        assert(date.getLocalDateFromCustomFormat() == expectedValue)
    }

    @Test
    fun `for a date string not in yyyy-MM-dd, throw exception`() {
        val date = "February 21, 2018"

        try {
            date.getLocalDateFromCustomFormat()
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(DateTimeParseException::class.java)
        }
    }
}