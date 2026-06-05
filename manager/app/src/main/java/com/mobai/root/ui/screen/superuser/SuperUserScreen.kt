package com.mobai.root.ui.screen.superuser

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobai.root.Natives
import com.mobai.root.ui.LocalUiMode
import com.mobai.root.ui.UiMode
import com.mobai.root.ui.navigation3.Navigator
import com.mobai.root.ui.navigation3.Route
import com.mobai.root.ui.util.rootAvailable
import com.mobai.root.ui.viewmodel.SuperUserViewModel

@Composable
fun SuperUserPager(
    navigator: Navigator,
    bottomInnerPadding: Dp,
    isCurrentPage: Boolean = true
) {
    val viewModel = viewModel<SuperUserViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var hasActivated by remember { mutableStateOf(false) }
    if (isCurrentPage) hasActivated = true

    val isRootAvailable = Natives.isManager && !Natives.requireNewKernel() && rootAvailable()

    if (hasActivated) {
        LaunchedEffect(Unit) {
            if (uiState.groupedApps.isEmpty()) {
                viewModel.initializePreferences()
                viewModel.loadAppList().join()
            } else if (viewModel.isNeedRefresh) {
                viewModel.loadAppList(resort = false).join()
            }
        }
    }

    val onSearchTextChange: (String) -> Unit = viewModel::updateSearchText
    val onToggleShowSystemApps: () -> Unit = {
        viewModel.toggleShowSystemApps()
    }
    val onToggleShowOnlyPrimaryUserApps: () -> Unit = {
        viewModel.toggleShowOnlyPrimaryUserApps()
    }
    val onOpenProfile: (GroupedApps) -> Unit = { group ->
        navigator.push(Route.AppProfile(group.uid))
        viewModel.markNeedRefresh()
    }
    val actions = SuperUserActions(
        onRefresh = { viewModel.loadAppList(force = true) },
        onOpenSulog = { navigator.push(Route.Sulog) },
        onSearchTextChange = onSearchTextChange,
        onSearchStatusChange = viewModel::updateSearchStatus,
        onClearSearch = { onSearchTextChange("") },
        onToggleShowSystemApps = onToggleShowSystemApps,
        onToggleShowOnlyPrimaryUserApps = onToggleShowOnlyPrimaryUserApps,
        onUpdateSortOption = { viewModel.updateSortOption(it) },
        onOpenProfile = onOpenProfile,
    )

    val enrichedState = uiState.copy(isRootAvailable = isRootAvailable)

    when (LocalUiMode.current) {
        UiMode.Miuix -> SuperUserPagerMiuix(
            uiState = enrichedState,
            actions = actions,
            bottomInnerPadding = bottomInnerPadding,
        )

        UiMode.Material -> SuperUserPagerMaterial(
            uiState = enrichedState,
            actions = actions,
            bottomInnerPadding = bottomInnerPadding,
        )
    }
}
