package com.mobai.root.ui.screen.susfs.content

import androidx.compose.runtime.Composable
import com.mobai.root.ui.LocalUiMode
import com.mobai.root.ui.UiMode
import com.mobai.root.ui.screen.susfs.content.miuix.EnabledFeaturesContentMiuix
import com.mobai.root.ui.screen.susfs.content.material.EnabledFeaturesContentMaterial
import com.mobai.root.ui.screen.susfs.util.SuSFSManager

@Composable
fun EnabledFeaturesContent(
    enabledFeatures: List<SuSFSManager.EnabledFeature>,
    onRefresh: () -> Unit
) {
    when (LocalUiMode.current) {
        UiMode.Miuix -> EnabledFeaturesContentMiuix(
            enabledFeatures = enabledFeatures,
            onRefresh = onRefresh
        )
        UiMode.Material -> EnabledFeaturesContentMaterial(
            enabledFeatures = enabledFeatures,
            onRefresh = onRefresh
        )
    }
}
