package com.mobai.root.ui.screen.susfs.component

import androidx.compose.runtime.Composable
import com.mobai.root.ui.LocalUiMode
import com.mobai.root.ui.UiMode
import com.mobai.root.ui.screen.susfs.component.miuix.AddKstatStaticallyDialogMiuix
import com.mobai.root.ui.screen.susfs.component.material.AddKstatStaticallyDialogMaterial

@Composable
fun AddKstatStaticallyDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String, String, String, String, String, String, String, String, String) -> Unit,
    isLoading: Boolean,
    initialConfig: String = ""
) {
    when (LocalUiMode.current) {
        UiMode.Miuix -> AddKstatStaticallyDialogMiuix(
            showDialog = showDialog,
            onDismiss = onDismiss,
            onConfirm = onConfirm,
            isLoading = isLoading,
            initialConfig = initialConfig
        )
        UiMode.Material -> AddKstatStaticallyDialogMaterial(
            showDialog = showDialog,
            onDismiss = onDismiss,
            onConfirm = onConfirm,
            isLoading = isLoading,
            initialConfig = initialConfig
        )
    }
}
