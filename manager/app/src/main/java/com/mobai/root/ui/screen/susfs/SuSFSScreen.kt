package com.mobai.root.ui.screen.susfs

import androidx.compose.runtime.Composable
import com.mobai.root.ui.LocalUiMode
import com.mobai.root.ui.UiMode

@Composable
fun SuSFSScreen() {
    when (LocalUiMode.current) {
        UiMode.Miuix -> SuSFSMiuix()
        UiMode.Material -> SuSFSMaterial()
    }
}
