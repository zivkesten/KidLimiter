package com.zivk.kidlimiter.viewModel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CountTimeViewModel : ViewModel() {

    private var countDownTimer: CountDownTimer? = null

    val isRunning: StateFlow<Boolean> get() = _isRunning

    private val _seconds = MutableStateFlow(0)
    val seconds: StateFlow<Int> get() = _seconds

    private val _minutes = MutableStateFlow(0)
    val minutes: StateFlow<Int> get() = _minutes

    private val _hours = MutableStateFlow(0)
    val hours: StateFlow<Int> get() = _hours

    private val _isRunning = MutableStateFlow(false)

    private val _progress = MutableStateFlow(1f)
    val progress: StateFlow<Float> get() = _progress

    private val _time = MutableStateFlow("00:00:00")
    val time: StateFlow<String> get() = _time

    var totalTime = 0L

    fun startCountDown() {
        if (countDownTimer != null) {
            cancelTimer()
        }

        totalTime = (getSeconds() * 1000).toLong()

        countDownTimer = object : CountDownTimer(totalTime, 1000) {
            override fun onTick(millisecs: Long) {
                // Seconds
                val secs = (millisecs / MSECS_IN_SEC % SECS_IN_MINUTES).toInt()
                if (secs != seconds.value) {
                    _seconds.value = secs
                }
                // Minutes
                val minutes = (millisecs / MSECS_IN_SEC / SECS_IN_MINUTES % SECS_IN_MINUTES).toInt()
                if (minutes != this@CountTimeViewModel.minutes.value) {
                    _minutes.value = minutes
                }
                // Hours
                val hours = (millisecs / MSECS_IN_SEC / MINUTES_IN_HOUR / SECS_IN_MINUTES).toInt()
                if (hours != this@CountTimeViewModel.hours.value) {
                    _hours.value = hours
                }

                _progress.value = millisecs.toFloat() / totalTime.toFloat()
                _time.value = formatHourMinuteSecond(hours, minutes, secs)
            }

            override fun onFinish() {
                _progress.value = 1.0f
                _isRunning.value = false
            }
        }
        countDownTimer?.start()
        _isRunning.value = true
    }

    fun modifyTime(timeUnit: TimeUnit, timeOperator: TimeOperator) {
        var seconds = seconds.value
        var minutes = minutes.value
        var hours = hours.value

        when (timeUnit) {
            TimeUnit.SEC -> {
                seconds = updateTime(seconds, timeOperator).coerceIn(0, 59)
            }
            TimeUnit.MIN ->{
                minutes = updateTime(minutes, timeOperator).coerceIn(0, 59)
            }
            TimeUnit.HOUR ->{
                hours = updateTime(hours, timeOperator).coerceIn(0, 23)
            }
        }

        // update time
        _seconds.value = seconds
        _minutes.value = minutes
        _hours.value = hours

        _time.value = formatHourMinuteSecond(hours, minutes, seconds)
    }

    private fun formatHourMinuteSecond(hours : Int,minutes : Int,seconds : Int) =
        String.format("%02d:%02d:%02d", hours, minutes, seconds)

    fun cancelTimer() {
        countDownTimer?.cancel()
        _isRunning.value = false
    }

    override fun onCleared() {
        super.onCleared()
        cancelTimer()
    }

    private fun getSeconds(): Int {
        val totalSecondsInHour = (hours.value * MINUTES_IN_HOUR * SECS_IN_MINUTES)
        val totalSecondsInMinutes = (minutes.value * SECS_IN_MINUTES)
        val totalSeconds =  seconds.value
        return totalSecondsInHour + totalSecondsInMinutes + totalSeconds
    }

    private fun updateTime(currentValue: Int, timeOperator: TimeOperator): Int {
        return when (timeOperator) {
            TimeOperator.INCREASE -> currentValue + 1
            TimeOperator.DECREASE -> currentValue - 1
        }
    }

    companion object {
        enum class TimeOperator {
            INCREASE, DECREASE
        }

        enum class TimeUnit {
            SEC, MIN, HOUR
        }

        const val MINUTES_IN_HOUR = 60
        const val SECS_IN_MINUTES = 60
        const val MSECS_IN_SEC = 1000
    }
}