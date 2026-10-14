package com.mahi.secureflagcheck.secure

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.staticCompositionLocalOf

val LocalSecureWindowController = staticCompositionLocalOf<SecureWindowController?> { null }

/**
 * Marks the enclosing screen as sensitive: FLAG_SECURE stays on the window
 * for as long as this composable is in the composition.
 */
@Composable
fun SecureScreen(content: @Composable () -> Unit) {
    val controller = LocalSecureWindowController.current
    DisposableEffect(controller) {
        controller?.acquire()
        onDispose { controller?.release() }
    }
    content()
}
