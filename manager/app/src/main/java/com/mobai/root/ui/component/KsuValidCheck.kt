package com.mobai.root.ui.component

import androidx.compose.runtime.Composable
import com.mobai.root.Natives

@Composable
fun KsuIsValid(
    content: @Composable () -> Unit
) {
    val isManager = runCatching { Natives.isManager }.getOrDefault(false)
    val ksuVersion = if (isManager) runCatching { Natives.version }.getOrNull() else null

    if (ksuVersion != null) {
        content()
    }
}
