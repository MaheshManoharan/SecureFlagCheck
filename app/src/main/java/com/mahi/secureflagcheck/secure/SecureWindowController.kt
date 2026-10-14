package com.mahi.secureflagcheck.secure

import android.app.Activity
import android.app.ActivityManager
import android.graphics.Color
import android.os.Build
import android.view.WindowManager
import androidx.annotation.MainThread

/**
 * Toggles FLAG_SECURE on the activity window based on how many secure screens
 * are currently composed. A reference count is needed because NavHost keeps
 * both the outgoing and incoming destinations composed during transitions.
 *
 * While secure:
 *  - screenshots are blocked,
 *  - screen recording / casting shows a black surface,
 *  - the Recent Apps thumbnail is black (plus setRecentsScreenshotEnabled(false) on API 33+).
 *
 * Android does not screenshot a secure window for Recents; it draws a placeholder
 * filled with the task's background color, which defaults to the theme's
 * windowBackground (white in a light theme). That color is forced to black here.
 */
class SecureWindowController(private val activity: Activity) {

    private var secureCount = 0

    @MainThread
    fun acquire() {
        secureCount++
        if (secureCount == 1) applySecure(true)
    }

    @MainThread
    fun release() {
        if (secureCount == 0) return
        secureCount--
        if (secureCount == 0) applySecure(false)
    }

    private fun applySecure(secure: Boolean) {
        if (secure) {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.setRecentsScreenshotEnabled(!secure)
            if (secure) {
                activity.setTaskDescription(
                    ActivityManager.TaskDescription.Builder()
                        .setBackgroundColor(Color.BLACK)
                        .setStatusBarColor(Color.BLACK)
                        .setNavigationBarColor(Color.BLACK)
                        .build()
                )
            }
        }
    }
}
