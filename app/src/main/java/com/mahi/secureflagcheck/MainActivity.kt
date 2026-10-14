package com.mahi.secureflagcheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.CompositionLocalProvider
import com.mahi.secureflagcheck.navigation.AppNavHost
import com.mahi.secureflagcheck.secure.LocalSecureWindowController
import com.mahi.secureflagcheck.secure.PrivacyOverlay
import com.mahi.secureflagcheck.secure.SecureWindowController
import com.mahi.secureflagcheck.ui.theme.SecureFlagCheckTheme

class MainActivity : ComponentActivity() {

    private val secureWindowController = SecureWindowController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SecureFlagCheckTheme {
                CompositionLocalProvider(LocalSecureWindowController provides secureWindowController) {
                    Box {
                        AppNavHost()
                        if (secureWindowController.isPrivacyOverlayVisible) PrivacyOverlay()
                    }
                }
            }
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        secureWindowController.onUserLeaveHint()
    }

    override fun onTopResumedActivityChanged(isTopResumedActivity: Boolean) {
        super.onTopResumedActivityChanged(isTopResumedActivity)
        secureWindowController.onTopResumedActivityChanged(isTopResumedActivity)
    }
}
