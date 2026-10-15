package com.mahi.secureflagcheck.secure

import android.app.ActivityManager
import android.graphics.Color
import android.os.Build
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

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
 * The placeholder is a flat color only: it cannot show an icon, and the snapshot is
 * taken before the app is notified it is leaving, so an in-app cover can't replace it.
 *
 * Privacy overlay: when a secure screen is shown and the app is being minimized
 * (Home / Recents / another app on top), [isPrivacyOverlayVisible] turns true so the
 * UI is covered by [PrivacyOverlay] until the activity is the top resumed one again.
 * On API 29+ this follows onTopResumedActivityChanged, which fires before onPause and
 * also when another app takes focus in multi-window; older versions use onPause/onResume.
 */
class SecureWindowController(private val activity: ComponentActivity) : DefaultLifecycleObserver {

    private var secureCount = 0

    /** True while the privacy overlay must cover the UI. */
    var isPrivacyOverlayVisible by mutableStateOf(false)
        private set

    init {
        activity.lifecycle.addObserver(this)
    }

    /** Home / Recents pressed: arrives before onPause, so the cover is drawn sooner. */
    @MainThread
    fun onUserLeaveHint() {
        if (secureCount > 0) cover()
    }

    @MainThread
    fun onTopResumedActivityChanged(isTopResumedActivity: Boolean) {
        if (isTopResumedActivity) uncover() else if (secureCount > 0) cover()
    }

    override fun onPause(owner: LifecycleOwner) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && secureCount > 0) cover()
    }

    override fun onResume(owner: LifecycleOwner) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) uncover()
    }

    /**
     * Shows the overlay, then — once that frame has reached the screen — lifts FLAG_SECURE
     * so a Recents snapshot taken from now on shows the overlay instead of the flat
     * placeholder. Real content is never visible while the flag is off.
     */
    private fun cover() {
        if (isPrivacyOverlayVisible) return
        isPrivacyOverlayVisible = true
        afterOverlayDrawn {
            if (isPrivacyOverlayVisible && secureCount > 0) applySecure(false)
        }
    }

    /** Re-secures the window before the real content is revealed again. */
    private fun uncover() {
        if (secureCount > 0) applySecure(true)
        isPrivacyOverlayVisible = false
    }

    private fun afterOverlayDrawn(action: () -> Unit) {
        val decor = activity.window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // The overlay recomposes on the next frame; act once a later frame is committed.
            decor.postOnAnimation {
                decor.viewTreeObserver.registerFrameCommitCallback { decor.post(action) }
                decor.invalidate()
            }
        } else {
            decor.postOnAnimation { decor.postOnAnimation { decor.post(action) } }
        }
    }

    @MainThread
    fun acquire() {
        secureCount++
        // While covered the flag stays off; uncover() re-applies it.
        if (secureCount == 1 && !isPrivacyOverlayVisible) applySecure(true)
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
