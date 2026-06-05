package com.mobai.root.ui.component.uninstalldialog

import androidx.compose.runtime.Composable
import com.mobai.root.ui.LocalUiMode
import com.mobai.root.ui.UiMode

@Composable
fun UninstallDialog(
    show: Boolean,
    onDismissRequest: () -> Unit
) {
    when (LocalUiMode.current) {
        UiMode.Miuix -> UninstallDialogMiuix(show, onDismissRequest)
        UiMode.Material -> UninstallDialogMaterial(show, onDismissRequest)
    }
}
