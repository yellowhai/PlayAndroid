package com.hh.common.util

import android.annotation.SuppressLint
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES.O
import com.hh.common.R
import com.hh.common.base.YshhApplication.Companion.context
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

/**
 * @ProjectName: playandroid
 * @Package: com.hh.common.util
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/12/31  9:25
 */

/**
 * Date to String
 * You can use LocalDateTime if your minSdkVersion is 26 (Android 8.0) or higher 1. Otherwise, you can use core library desugaring.
 */

const val timeType = "yyyy-MM-dd HH:mm"

@SuppressLint("SimpleDateFormat")
fun formatDate(formatStyle: String = timeType, time: Long = System.currentTimeMillis()): String {
    return if (VERSION.SDK_INT >= O) {
        val df: DateTimeFormatter = DateTimeFormatter.ofPattern(formatStyle)
        val now = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
            .toLocalDate()
        df.format(now)
    } else {
        val sdf = SimpleDateFormat(formatStyle)
        sdf.format(Date(time))
    }
}

/**
 * "12-13" Nanjing Massacre Memorial Day
 * "5-12" Wenchuan Earthquake Memorial Day
 * @Author: yshh
 */
@SuppressLint("SimpleDateFormat")
fun isMourningDay(): Boolean {
    return if (VERSION.SDK_INT >= O) {
        val date = LocalDate.now()
        val wenchuanDay = LocalDate.parse(
            "${LocalDate.now().year}-12-13",
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        )
        val nanjingDay = LocalDate.parse(
            "${LocalDate.now().year}-05-12",
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        )
        date.isEqual(wenchuanDay) || date.isEqual(nanjingDay)
    } else {
        val sdf = SimpleDateFormat("MM-dd")
        val nanjingDay = "12-13"
        val wenchuanDay = "05-12"
        val now = sdf.format(Date(System.currentTimeMillis()))
        now == nanjingDay || now == wenchuanDay
    }
}

private const val SECOND_MILLIS = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS

private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS


/**
 * 按照毫秒来存储
 *
 * @param time
 * @return
 */
fun getTimeAgo(time: Long): String {
    var mutableTime = time
    if (time < 1000000000000L) {
        // if timestamp given in seconds, convert to millis
        mutableTime *= 1000
    }
    val now = System.currentTimeMillis()
    if (mutableTime > now || mutableTime <= 0) {
        return context.getString(R.string.the_unknown_time)
    }
    val diff = now - mutableTime
    return when {
        diff < MINUTE_MILLIS -> {
            String.format(
                context.getString(R.string.seconds_ago),
                (diff / SECOND_MILLIS).toString()
            )
        }
        diff < 2 * MINUTE_MILLIS -> {
            String.format(context.getString(R.string.minutes_ago), "1")
        }
        diff < 50 * MINUTE_MILLIS -> {
            String.format(
                context.getString(R.string.minutes_ago),
                (diff / MINUTE_MILLIS).toString()
            )
        }
        diff < 90 * MINUTE_MILLIS -> {
            String.format(context.getString(R.string.hours_ago), "1")
        }
        diff < 24 * HOUR_MILLIS -> {
            String.format(context.getString(R.string.hours_ago), (diff / HOUR_MILLIS).toString())
        }
        diff < 48 * HOUR_MILLIS -> {
            context.getString(R.string.yesterday)
        }
        diff < 72 * HOUR_MILLIS -> {
            context.getString(R.string.day_before_yesterday)
        }
        diff < 14 * DAY_MILLIS -> {
            String.format(context.getString(R.string.days_ago), (diff / DAY_MILLIS).toString())
        }
        else -> {
            return if (VERSION.SDK_INT >= O) {
                val localTime = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(mutableTime),
                    ZoneId.systemDefault()
                )
                val df: DateTimeFormatter = DateTimeFormatter.ofPattern(timeType)
                df.format(localTime)
            } else {
                val sdf = SimpleDateFormat(timeType)
                sdf.format(time)
            }
        }
    }
}
