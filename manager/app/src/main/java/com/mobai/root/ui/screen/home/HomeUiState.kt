package com.mobai.root.ui.screen.home

import androidx.compose.runtime.Immutable
import com.mobai.root.KernelVersion
import com.mobai.root.ui.util.module.LatestVersionInfo

@Immutable
data class HomeUiState(
    val kernelVersion: KernelVersion = KernelVersion(0, 0, 0),
    val ksuVersion: Int? = null,
    val lkmMode: Boolean? = null,
    val isManager: Boolean = false,
    val isManagerPrBuild: Boolean = false,
    val isKernelPrBuild: Boolean = false,
    val requiresNewKernel: Boolean = false,
    val isRootAvailable: Boolean = false,
    val isSafeMode: Boolean = false,
    val isLateLoadMode: Boolean = false,
    val checkUpdateEnabled: Boolean = true,
    val latestVersionInfo: LatestVersionInfo = LatestVersionInfo(),
    val currentManagerVersionCode: Long = 0,
    val superuserCount: Int = 0,
    val moduleCount: Int = 0,
    val systemInfo: SystemInfo = SystemInfo(),
    val showFullStatus: Boolean = true,
) {
    val isSELinuxPermissive: Boolean
        get() = systemInfo.selinuxStatus == "Permissive"

    val isFullFeatured: Boolean
        get() = isManager && !requiresNewKernel && isRootAvailable

    val showRequireKernelWarning: Boolean
        get() = isManager && requiresNewKernel && lkmMode == true

    val showRootWarning: Boolean
        get() = ksuVersion != null && !isRootAvailable

    val showManagerPrBuildWarning: Boolean
        get() = isManager && isManagerPrBuild

    val showKernelPrBuildWarning: Boolean
        get() = isManager && !isManagerPrBuild && isKernelPrBuild

    val showVersionMismatchWarning: Boolean
        get() = ksuVersion != null && ksuVersion.toLong() != currentManagerVersionCode

    val hasUpdate: Boolean
        get() = latestVersionInfo.versionCode > currentManagerVersionCode
}

@Immutable
data class HomeActions(
    val onInstallClick: () -> Unit,
    val onSuperuserClick: () -> Unit,
    val onModuleClick: () -> Unit,
    val onOpenUrl: (String) -> Unit,
    val onJailbreakClick: () -> Unit = {},
)
