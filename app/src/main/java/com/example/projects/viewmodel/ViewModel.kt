package com.example.projects.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import com.example.projects.model.Program
import com.example.projects.model.getProgramTest
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@ExperimentalTime
class ViewModel : ViewModel() {

    //TIMER

    private var time: Duration = ZERO
    private lateinit var timer: Timer

    var seconds by mutableStateOf("00")
    var minutes by mutableStateOf("00")
    private var isPlaying by mutableStateOf(false)

    fun startExerciseWithDuration(duration: Int) {
        if (time < duration.seconds) {
            timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
                time = time.plus(1.seconds)
                updateTimeStates()
                if (time == duration.seconds.plus(1.seconds)) {
                    stop()
                }
            }
            isPlaying = true
        } else {
            stop()
        }
    }

    fun countdown(duration: Int) {
        time = ZERO
        time = time.plus(4.seconds)
        timer = fixedRateTimer(initialDelay = 0L, period = 1000L) {
            time = time.minus(1.seconds)
            updateTimeStates()
            if (time == ZERO) {
                stop()
                startExerciseWithDuration(duration)
            }
        }
        isPlaying = true
    }

    private fun updateTimeStates() {
        time.toComponents { _, minutes, seconds, _ ->
            this@ViewModel.seconds = seconds.pad()
            this@ViewModel.minutes = minutes.pad()
        }
    }

    private fun Int.pad(): String {
        return this.toString().padStart(2, '0')
    }

    private fun pause() {
        timer.cancel()
        isPlaying = false
    }

    fun stop() {
        pause()
        time = ZERO
        updateTimeStates()
    }

    //PROGRAM

    private val _position = MutableLiveData<Int>()
    val positionLiveData: MutableLiveData<Int> = _position
    private val _exerciseOrRestText = MutableLiveData<String>()
    var exerciseOrRestTextLiveData: MutableLiveData<String> = _exerciseOrRestText

    fun initPosition() {
        _position.value = 0
    }

    private fun upPosition() {
        _position.value = _position.value?.plus(1)
    }

    fun initTitle() {
        _exerciseOrRestText.value = "Start Program"
    }

    fun defineTitle(program: Program) {
        if (exerciseOrRestTextLiveData.value == "Start Program") {
            exerciseOrRestTextLiveData.value =
                "Exercise: " + program.exerciseList[_position.value!!].name
        } else if (exerciseOrRestTextLiveData.value == "Exercise: " + program.exerciseList[_position.value!!].name) {
            exerciseOrRestTextLiveData.value = "Rest"
        } else if (exerciseOrRestTextLiveData.value == "Rest") {
            if (_position.value != getProgramTest().exerciseList.size - 1) {
                upPosition()
                exerciseOrRestTextLiveData.value =
                    "Exercise: " + program.exerciseList[_position.value!!].name
            }
        }
    }
}