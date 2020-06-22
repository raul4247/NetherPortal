package com.raulfm.netherportal.backend.singletons

import android.os.CountDownTimer

object CooldownManager {
    const val durationTime = 20000L
    const val tick = 50L

    fun initCooldown(onStart: () -> Unit, onFinish: () -> Unit, onTick: (percentage: Int) -> Unit) {
        var percentage: Double

        onStart()
        val timer = object : CountDownTimer(durationTime, tick) {
            override fun onTick(millisUntilFinished: Long) {
                percentage = ((durationTime - millisUntilFinished).toDouble() / durationTime) * 100
                onTick(percentage.toInt())
            }

            override fun onFinish() = onFinish()
        }
        timer.start()
    }
}