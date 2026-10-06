package com.example.ui.components

import android.view.HapticFeedbackConstants
import android.view.SoundEffectConstants
import android.view.View

object AudioFeedback {
    fun playClick(view: View?) {
        try {
            view?.playSoundEffect(SoundEffectConstants.CLICK)
            view?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        } catch (_: Exception) {}
    }

    fun playConfirm(view: View?) {
        try {
            view?.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
        } catch (_: Exception) {}
    }

    fun playAlert(view: View?) {
        try {
            view?.performHapticFeedback(HapticFeedbackConstants.REJECT)
        } catch (_: Exception) {}
    }
}
