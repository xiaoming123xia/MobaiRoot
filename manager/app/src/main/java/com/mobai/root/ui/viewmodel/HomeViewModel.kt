package com.mobai.root.ui.viewmodel

import android.content.Context
import android.os.Build
import android.system.Os
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.mobai.root.BuildConfig
import com.mobai.root.Natives
import com.mobai.root.getKernelVersion
import com.mobai.root.ksuApp
import com.mobai.root.ui.screen.home.HomeUiState
import com.mobai.root.ui.screen.home.SystemInfo
import com.mobai.root.ui.screen.home.getManagerVersion
import com.mobai.root.ui.util.checkNewVersion
import com.mobai.root.ui.util.getModuleCount
import com.mobai.root.ui.util.getSuperuserCount
import com.mobai.root.ui.util.getSELinuxStatusRaw
import com.mobai.root.ui.util.module.LatestVersionInfo
import com.mobai.root.ui.util.resolveDeviceName
import com.mobai.root.ui.util.rootAvailable

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                val baseState = withContext(Dispatchers.IO) { buildState() }
                _uiState.update { baseState }
                if (baseState.checkUpdateEnabled) {
                    val latestVersionInfo = withContext(Dispatchers.IO) { 
                        runCatching { checkNewVersion() }.getOrElse { LatestVersionInfo() }
                    }
                    _uiState.update { it.copy(latestVersionInfo = latestVersionInfo) }
                }
            } catch (e: Exception) {
                // If buildState fails, show a safe state
                _uiState.update { 
                    it.copy(
                        isRootAvailable = false,
                        isManager = false,
                        requiresNewKernel = false
                    )
                }
            }
        }
    }

    private fun buildState(): HomeUiState {
        val kernelVersion = getKernelVersion()
        val isManager = runCatching { Natives.isManager }.getOrDefault(false)
        val ksuVersion = if (isManager) runCatching { Natives.version }.getOrNull() else null
        val lkmMode = ksuVersion?.let { runCatching { Natives.isLkmMode }.getOrNull() }
        val isRootAvailable = runCatching { rootAvailable() }.getOrDefault(false)
        val managerVersion = getManagerVersion(ksuApp)
        val kernelFullVersion = if (isManager) runCatching { Natives.getFullVersion() }.getOrNull() else null

        return HomeUiState(
            kernelVersion = kernelVersion,
            ksuVersion = ksuVersion,
            lkmMode = lkmMode,
            isManager = isManager,
            isManagerPrBuild = BuildConfig.IS_PR_BUILD,
            isKernelPrBuild = runCatching { Natives.isPrBuild }.getOrDefault(false),
            requiresNewKernel = isManager && runCatching { Natives.requireNewKernel() }.getOrDefault(false),
            isRootAvailable = isRootAvailable,
            isSafeMode = runCatching { Natives.isSafeMode }.getOrDefault(false),
            isLateLoadMode = runCatching { Natives.isLateLoadMode }.getOrDefault(false),
            checkUpdateEnabled = ksuApp.getSharedPreferences("settings", Context.MODE_PRIVATE)
                .getBoolean("check_update", true),
            showFullStatus = ksuApp.getSharedPreferences("settings", Context.MODE_PRIVATE)
                .getBoolean("show_fingerprint", true),
            latestVersionInfo = LatestVersionInfo(),
            currentManagerVersionCode = managerVersion.versionCode,
            superuserCount = if (isRootAvailable) runCatching { getSuperuserCount() }.getOrDefault(0) else 0,
            moduleCount = if (isRootAvailable) runCatching { getModuleCount() }.getOrDefault(0) else 0,
            systemInfo = SystemInfo(
                kernelVersion = Os.uname().release,
                managerVersion = "${managerVersion.versionName} (${managerVersion.versionCode})",
                deviceModel = resolveDeviceName(),
                kernelFullVersion = kernelFullVersion,
                fingerprint = Build.FINGERPRINT,
                selinuxStatus = runCatching { getSELinuxStatusRaw() }.getOrDefault("Unknown"),
                seccompStatus = runCatching {
                    Os.prctl(21 /* PR_GET_SECCOMP */, 0, 0, 0, 0)
                }.getOrDefault(-1),
            ),
        )
    }
}
