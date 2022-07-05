package app.spring.common.util

import java.time.LocalDateTime
import java.time.Month
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

object DateTimeUtil {

    fun getStart(dateTime: LocalDateTime, unit: ChronoUnit = ChronoUnit.DAYS): LocalDateTime {
        return when (unit) {
            ChronoUnit.YEARS -> {
                LocalDateTime.of(dateTime.year, Month.JANUARY, 1, 0, 0, 0)
            }
            ChronoUnit.MONTHS -> {
                LocalDateTime.of(dateTime.year, dateTime.month, 1, 0, 0, 0)
            }
            ChronoUnit.DAYS -> {
                LocalDateTime.of(dateTime.year, dateTime.month, dateTime.dayOfMonth, 0, 0, 0)
            }
            ChronoUnit.HOURS -> {
                LocalDateTime.of(dateTime.year, dateTime.month, dateTime.dayOfMonth, dateTime.hour, 0, 0)
            }
            ChronoUnit.MINUTES -> {
                LocalDateTime.of(dateTime.year, dateTime.month, dateTime.dayOfMonth, dateTime.hour, dateTime.minute, 0)
            }
            else -> {
                dateTime
            }
        }
    }

    fun getEnd(dateTime: LocalDateTime, unit: ChronoUnit = ChronoUnit.DAYS): LocalDateTime {
        return when (unit) {
            ChronoUnit.YEARS -> {
                LocalDateTime.of(dateTime.year, Month.DECEMBER, 31, 23, 59, 59)
            }
            ChronoUnit.MONTHS -> {
                LocalDateTime.of(dateTime.year, dateTime.month, dateTime.with(TemporalAdjusters.lastDayOfMonth()).dayOfMonth, 23, 59, 59)
            }
            ChronoUnit.DAYS -> {
                LocalDateTime.of(dateTime.year, dateTime.month, dateTime.dayOfMonth, 23, 59, 59)
            }
            ChronoUnit.HOURS -> {
                LocalDateTime.of(dateTime.year, dateTime.month, dateTime.dayOfMonth, dateTime.hour, 59, 59)
            }
            ChronoUnit.MINUTES -> {
                LocalDateTime.of(dateTime.year, dateTime.month, dateTime.dayOfMonth, dateTime.hour, dateTime.minute, 59)
            }
            else -> {
                dateTime
            }
        }
    }
}