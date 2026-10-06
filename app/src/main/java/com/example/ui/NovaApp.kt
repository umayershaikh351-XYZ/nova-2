package com.example.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.NovaBottomNav
import com.example.ui.navigation.NovaNavTarget
import com.example.ui.screens.AgentPanelScreen
import com.example.ui.screens.AmbientScreen
import com.example.ui.screens.BootScreen
import com.example.ui.screens.CommandLineScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DeviceIntegrationScreen
import com.example.ui.screens.DeviceScanScreen
import com.example.ui.screens.KillSwitchScreen
import com.example.ui.screens.LifeGraphScreen
import com.example.ui.screens.PermissionScreen
import com.example.ui.screens.VisionCoreScreen
import com.example.ui.screens.WorldScreen
import com.example.ui.theme.NovaBackgroundBase
import com.example.viewmodel.NovaViewModel

@Composable
fun NovaApp(
    viewModel: NovaViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    // Request permissions gracefully on first launch
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        // Telemetry reader will pick up newly granted sensor/location data on next 2s poll
        viewModel.refreshWorldData()
    }

    LaunchedEffect(Unit) {
        val permissionsToRequest = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionsToRequest.add(Manifest.permission.ACTIVITY_RECOGNITION)
        }
        permissionsToRequest.add(Manifest.permission.BODY_SENSORS)

        permissionLauncher.launch(permissionsToRequest.toTypedArray())
    }

    // Back handling
    BackHandler(enabled = currentScreen != NovaNavTarget.DASHBOARD && currentScreen != NovaNavTarget.BOOT) {
        viewModel.navigateBack()
    }

    val showBottomNav = currentScreen != NovaNavTarget.BOOT && currentScreen != NovaNavTarget.KILL_SWITCH

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(NovaBackgroundBase),
        containerColor = NovaBackgroundBase,
        contentWindowInsets = WindowInsets.statusBars,
        bottomBar = {
            if (showBottomNav) {
                NovaBottomNav(
                    currentTarget = currentScreen,
                    onSelectTarget = { target ->
                        viewModel.navigateTo(target)
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    (fadeIn() + slideInHorizontally { width -> width / 4 })
                        .togetherWith(fadeOut() + slideOutHorizontally { width -> -width / 4 })
                },
                label = "screenTransition"
            ) { target ->
                when (target) {
                    NovaNavTarget.BOOT -> BootScreen(
                        onBootComplete = {
                            viewModel.navigateTo(NovaNavTarget.DASHBOARD)
                        }
                    )
                    NovaNavTarget.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                    NovaNavTarget.LIFEGRAPH -> LifeGraphScreen(viewModel = viewModel)
                    NovaNavTarget.VISIONCORE -> VisionCoreScreen(viewModel = viewModel)
                    NovaNavTarget.COMMAND_LINE -> CommandLineScreen(viewModel = viewModel)
                    NovaNavTarget.AGENT_PANEL -> AgentPanelScreen(viewModel = viewModel)
                    NovaNavTarget.PERMISSIONS -> PermissionScreen(viewModel = viewModel)
                    NovaNavTarget.KILL_SWITCH -> KillSwitchScreen(viewModel = viewModel)
                    NovaNavTarget.AMBIENT -> AmbientScreen(viewModel = viewModel)
                    NovaNavTarget.DEVICES -> DeviceIntegrationScreen(viewModel = viewModel)
                    NovaNavTarget.DEVICE_SCAN -> DeviceScanScreen(viewModel = viewModel)
                    NovaNavTarget.WORLD -> WorldScreen(viewModel = viewModel)
                }
            }
        }
    }
}
