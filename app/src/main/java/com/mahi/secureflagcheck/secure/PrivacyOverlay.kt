package com.mahi.secureflagcheck.secure

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mahi.secureflagcheck.R

/** Blank cover with a "visibility off" icon, shown over secure screens while the app is minimized. */
@Composable
fun PrivacyOverlay(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize().background(
            color = Color(0xff000000)
           // MaterialTheme.colorScheme.surface
        ),
        contentAlignment = Alignment.Center,
    ) {
//        Icon(
//            painter = painterResource(R.drawable.ic_visibility_off),
//            contentDescription = "Content hidden",
//            tint = MaterialTheme.colorScheme.onSurfaceVariant,
//            modifier = Modifier.size(96.dp),
//        )
    }
}
