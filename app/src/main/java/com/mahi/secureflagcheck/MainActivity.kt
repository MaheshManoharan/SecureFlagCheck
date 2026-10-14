package com.mahi.secureflagcheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.mahi.secureflagcheck.navigation.AppNavHost
import com.mahi.secureflagcheck.secure.LocalSecureWindowController
import com.mahi.secureflagcheck.secure.SecureWindowController
import com.mahi.secureflagcheck.ui.theme.SecureFlagCheckTheme

class MainActivity : ComponentActivity() {

    private val secureWindowController by lazy { SecureWindowController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SecureFlagCheckTheme {
                CompositionLocalProvider(LocalSecureWindowController provides secureWindowController) {
                    AppNavHost()
                }
            }
        }
    }
}
