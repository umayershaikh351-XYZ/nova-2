package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.WorldTelemetryService
import com.example.data.device.DeviceTelemetryReader
import com.example.data.model.ActivityEvent
import com.example.data.model.Agent
import com.example.data.model.AgentStatus
import com.example.data.model.CommandLog
import com.example.data.model.ConnectedDeviceItem
import com.example.data.model.FullDeviceState
import com.example.data.model.FullWorldState
import com.example.data.model.GoalItem
import com.example.data.model.HabitItem
import com.example.data.model.MemoryItem
import com.example.data.model.PermissionItem
import com.example.data.model.PermissionState
import com.example.data.model.RelationshipItem
import com.example.ui.navigation.NovaNavTarget
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class NovaViewModel(application: Application) : AndroidViewModel(application) {

    private val deviceReader = DeviceTelemetryReader(application.applicationContext)
    private val worldService = WorldTelemetryService()

    // Navigation State
    private val _currentScreen = MutableStateFlow(NovaNavTarget.BOOT)
    val currentScreen: StateFlow<NovaNavTarget> = _currentScreen.asStateFlow()

    private val navBackStack = mutableListOf<NovaNavTarget>()

    // Device Telemetry (real-time, updates every 2 seconds)
    val deviceState: StateFlow<FullDeviceState> = deviceReader.deviceState

    // World Telemetry (real-time, updates every 15 minutes or on demand)
    private val _worldState = MutableStateFlow(FullWorldState())
    val worldState: StateFlow<FullWorldState> = _worldState.asStateFlow()

    // Agents
    private val _agents = MutableStateFlow<List<Agent>>(emptyList())
    val agents: StateFlow<List<Agent>> = _agents.asStateFlow()

    // Permissions
    private val _permissions = MutableStateFlow<List<PermissionItem>>(emptyList())
    val permissions: StateFlow<List<PermissionItem>> = _permissions.asStateFlow()

    // CommandLine
    private val _currentCommandInput = MutableStateFlow("")
    val currentCommandInput: StateFlow<String> = _currentCommandInput.asStateFlow()

    private val _commandLogs = MutableStateFlow<List<CommandLog>>(emptyList())
    val commandLogs: StateFlow<List<CommandLog>> = _commandLogs.asStateFlow()

    private val _isCommandProcessing = MutableStateFlow(false)
    val isCommandProcessing: StateFlow<Boolean> = _isCommandProcessing.asStateFlow()

    private val _commandProgress = MutableStateFlow(0f)
    val commandProgress: StateFlow<Float> = _commandProgress.asStateFlow()

    private val _liveCommandOutputs = MutableStateFlow<List<String>>(emptyList())
    val liveCommandOutputs: StateFlow<List<String>> = _liveCommandOutputs.asStateFlow()

    private val _routedAgents = MutableStateFlow<List<String>>(emptyList())
    val routedAgents: StateFlow<List<String>> = _routedAgents.asStateFlow()

    // LifeGraph
    private val _goals = MutableStateFlow<List<GoalItem>>(emptyList())
    val goals: StateFlow<List<GoalItem>> = _goals.asStateFlow()

    private val _habits = MutableStateFlow<List<HabitItem>>(emptyList())
    val habits: StateFlow<List<HabitItem>> = _habits.asStateFlow()

    private val _relationships = MutableStateFlow<List<RelationshipItem>>(emptyList())
    val relationships: StateFlow<List<RelationshipItem>> = _relationships.asStateFlow()

    private val _memories = MutableStateFlow<List<MemoryItem>>(emptyList())
    val memories: StateFlow<List<MemoryItem>> = _memories.asStateFlow()

    // Devices
    private val _connectedDevices = MutableStateFlow<List<ConnectedDeviceItem>>(emptyList())
    val connectedDevices: StateFlow<List<ConnectedDeviceItem>> = _connectedDevices.asStateFlow()

    private val _isMultiDeviceSyncEnabled = MutableStateFlow(true)
    val isMultiDeviceSyncEnabled: StateFlow<Boolean> = _isMultiDeviceSyncEnabled.asStateFlow()

    // Activity Events
    private val _recentActivities = MutableStateFlow<List<ActivityEvent>>(emptyList())
    val recentActivities: StateFlow<List<ActivityEvent>> = _recentActivities.asStateFlow()

    // Trust level
    private val _trustLevel = MutableStateFlow(82)
    val trustLevel: StateFlow<Int> = _trustLevel.asStateFlow()

    // VisionCore
    private val _visionConfidence = MutableStateFlow(98)
    val visionConfidence: StateFlow<Int> = _visionConfidence.asStateFlow()

    private var deviceScannerJob: Job? = null
    private var worldRefresherJob: Job? = null
    private var agentSimulationJob: Job? = null

    init {
        initInitialData()
        startDevicePolling()
        startWorldPolling()
        startAgentSimulation()
    }

    private fun initInitialData() {
        // Initial agents
        _agents.value = listOf(
            Agent(
                id = "agent_1",
                name = "Research Agent",
                role = "Deep Web & Sensor Intelligence",
                status = AgentStatus.RUNNING,
                transcript = "Scanning latest AI developments… 3/8 sources analyzed",
                progress = 0.38f,
                iconCategory = "research"
            ),
            Agent(
                id = "agent_2",
                name = "Code Agent",
                role = "Compiler & System Optimization",
                status = AgentStatus.RUNNING,
                transcript = "Building NOVA module… 12% complete",
                progress = 0.12f,
                iconCategory = "code"
            ),
            Agent(
                id = "agent_3",
                name = "Travel Agent",
                role = "Logistics & Route Planning",
                status = AgentStatus.ACTIVE,
                transcript = "Searching best flight options… 4 found",
                progress = 0.70f,
                iconCategory = "travel"
            ),
            Agent(
                id = "agent_4",
                name = "Health Agent",
                role = "Biometrics & Sleep Circadian",
                status = AgentStatus.WAITING,
                transcript = "Awaiting your input (sleep schedule)",
                progress = 0.50f,
                iconCategory = "health"
            ),
            Agent(
                id = "agent_5",
                name = "Finance Agent",
                role = "Crypto & Market Monitoring",
                status = AgentStatus.IDLE,
                transcript = "Monitoring market trends (BTC/ETH steady)",
                progress = 0.0f,
                iconCategory = "finance"
            ),
            Agent(
                id = "agent_6",
                name = "Creative Agent",
                role = "Generative Media & Assets",
                status = AgentStatus.DONE,
                transcript = "Designs ready for review (6 vectors generated)",
                progress = 1.0f,
                iconCategory = "creative"
            ),
            Agent(
                id = "agent_7",
                name = "Security Agent",
                role = "Kernel & Zero-Trust Perimeter",
                status = AgentStatus.ACTIVE,
                transcript = "Encrypted tunnels verified • 0 anomalies detected",
                progress = 0.95f,
                iconCategory = "security"
            ),
            Agent(
                id = "agent_8",
                name = "Automation Agent",
                role = "Device Event Orchestration",
                status = AgentStatus.RUNNING,
                transcript = "Telemetry pipeline active • 2s polling sync",
                progress = 0.65f,
                iconCategory = "automation"
            )
        )

        // Initial permissions
        _permissions.value = listOf(
            PermissionItem("perm_cam", "Camera", "Screen & visual surroundings awareness", PermissionState.ALLOWED, "Camera"),
            PermissionItem("perm_mic", "Microphone", "Voice terminal input & audio spectrum", PermissionState.ALLOWED, "Mic"),
            PermissionItem("perm_loc", "Location", "Precise GPS telemetry & weather sync", PermissionState.ALLOWED, "Location"),
            PermissionItem("perm_files", "Files & Storage", "Document reading & local agent memory", PermissionState.ALLOWED, "Folder"),
            PermissionItem("perm_cal", "Calendar", "Schedule awareness & smart agenda", PermissionState.ALLOWED, "Calendar"),
            PermissionItem("perm_contacts", "Contacts", "Communication routing & contacts graph", PermissionState.ALLOWED, "Contacts"),
            PermissionItem("perm_email", "Email", "Draft triage & inbox classification", PermissionState.LIMITED, "Email"),
            PermissionItem("perm_payments", "Payments", "Financial transactions & transfers", PermissionState.BLOCKED, "Payment")
        )

        // Goals
        _goals.value = listOf(
            GoalItem("g1", "Learn Cybersecurity", "Knowledge", 0.82f, "Dec 2026"),
            GoalItem("g2", "Build NOVA", "Engineering", 0.61f, "Nov 2026"),
            GoalItem("g3", "Fitness & Health", "Wellness", 0.47f, "Ongoing"),
            GoalItem("g4", "Financial Freedom", "Finance", 0.38f, "2027"),
            GoalItem("g5", "Travel the World", "Life", 0.25f, "2028")
        )

        // Habits
        _habits.value = listOf(
            HabitItem("h1", "Morning Telemetry Review", 18, true),
            HabitItem("h2", "10,000 Daily Steps", 14, false),
            HabitItem("h3", "Deep Focus (4 Hours)", 9, true),
            HabitItem("h4", "Evening Offline Hour", 5, false)
        )

        // Relationships
        _relationships.value = listOf(
            RelationshipItem("r1", "Core Development Squad", "Engineering", "Today, 10:14", 95),
            RelationshipItem("r2", "Sarah Jenkins", "Design Lead", "Yesterday", 88),
            RelationshipItem("r3", "Alex Rivera", "Security Auditor", "2 days ago", 82)
        )

        // Memories
        _memories.value = listOf(
            MemoryItem("m1", "Completed Kernel Telemetry Bridge", "Real-time hardware sensors integrated with 2s interval loop", "Today 08:30", "System"),
            MemoryItem("m2", "Synced Global Live Weather Telemetry", "Open-Meteo & Air Quality streams verified", "Yesterday 21:15", "World"),
            MemoryItem("m3", "Agent Neural Orchestrator Calibrated", "8 multi-modal agents provisioned with zero-trust credentials", "2 days ago", "Neural")
        )

        // Connected devices
        _connectedDevices.value = listOf(
            ConnectedDeviceItem("d1", "This Phone", "NOVA Mobile App", true, 84, isCurrentDevice = true),
            ConnectedDeviceItem("d2", "Laptop Station", "Windows 11 Pro", true, 92),
            ConnectedDeviceItem("d3", "Smartwatch", "Galaxy Watch 6", true, 67),
            ConnectedDeviceItem("d4", "Earbuds", "Galaxy Buds Pro", true, 100),
            ConnectedDeviceItem("d5", "AR Glasses", "Spectra Vision", false, null),
            ConnectedDeviceItem("d6", "Smart Home Hub", "Home Automation Core", false, null),
            ConnectedDeviceItem("d7", "Autonomous Vehicle", "Tesla Model 3", false, null)
        )

        // Activities
        _recentActivities.value = listOf(
            ActivityEvent("a1", "sensor", "Device Scan Completed", "All 8 hardware sensors online and reporting nominal data", "Just now"),
            ActivityEvent("a2", "cloud", "World Telemetry Synced", "Open-Meteo weather & CoinGecko crypto feeds updated", "3m ago"),
            ActivityEvent("a3", "agent", "Research Agent Dispatched", "Gathering latest autonomous AI papers and indexing findings", "12m ago")
        )

        // Initial default command log
        _liveCommandOutputs.value = listOf(
            "Kernel initialized at [0.000412s]",
            "Hardware sensor bus: light, accel, gyro, mag, baro nominal",
            "Agents: 3 running, 2 active, 1 waiting, 1 idle, 1 done",
            "Awaiting operator command..."
        )
        _routedAgents.value = listOf("Research Agent", "Code Agent", "Automation Agent")
    }

    // ─────────────────────────── NAVIGATION ───────────────────────────

    fun navigateTo(target: NovaNavTarget) {
        if (_currentScreen.value != target) {
            navBackStack.add(_currentScreen.value)
            _currentScreen.value = target
        }
    }

    fun navigateBack(): Boolean {
        return if (navBackStack.isNotEmpty()) {
            _currentScreen.value = navBackStack.removeAt(navBackStack.size - 1)
            true
        } else if (_currentScreen.value != NovaNavTarget.DASHBOARD) {
            _currentScreen.value = NovaNavTarget.DASHBOARD
            true
        } else {
            false
        }
    }

    // ─────────────────────────── TELEMETRY POLLING ───────────────────────────

    private fun startDevicePolling() {
        deviceScannerJob?.cancel()
        deviceScannerJob = viewModelScope.launch {
            while (isActive) {
                deviceReader.refreshAll()
                delay(2000) // Spec: "All device stats update LIVE every 2 seconds"
            }
        }
    }

    private fun startWorldPolling() {
        worldRefresherJob?.cancel()
        worldRefresherJob = viewModelScope.launch {
            // Initial fetch
            refreshWorldData()
            while (isActive) {
                delay(15 * 60 * 1000) // Spec: "All data refreshes every 15 minutes in background"
                refreshWorldData()
            }
        }
    }

    fun refreshWorldData() {
        viewModelScope.launch {
            _worldState.update { it.copy(isFetching = true) }
            try {
                val newState = worldService.fetchAllWorldData()
                _worldState.value = newState
            } catch (_: Exception) {
                _worldState.update { it.copy(isFetching = false, errorMessage = "Offline mode active") }
            }
        }
    }

    // ─────────────────────────── AGENTS & SIMULATION ───────────────────────────

    private fun startAgentSimulation() {
        agentSimulationJob?.cancel()
        agentSimulationJob = viewModelScope.launch {
            var step = 0
            while (isActive) {
                delay(2500) // Spec: "Transcripts update every 2–3s"
                step++

                _agents.update { currentList ->
                    currentList.map { agent ->
                        if (agent.status == AgentStatus.RUNNING) {
                            val newProgress = ((agent.progress + 0.04f) % 1.0f)
                            val newTranscript = when (agent.id) {
                                "agent_1" -> "Scanning latest AI developments… ${((step % 8) + 1)}/8 sources analyzed"
                                "agent_2" -> "Building NOVA module… ${(newProgress * 100).toInt()}% complete"
                                "agent_8" -> "Telemetry pipeline active • live stream #${step * 3} processed"
                                else -> agent.transcript
                            }
                            agent.copy(
                                progress = newProgress,
                                transcript = newTranscript
                            )
                        } else {
                            agent
                        }
                    }
                }
            }
        }
    }

    fun pauseAllAgents() {
        _agents.update { list ->
            list.map {
                if (it.status == AgentStatus.RUNNING) it.copy(status = AgentStatus.WAITING) else it
            }
        }
    }

    fun resumeAllAgents() {
        _agents.update { list ->
            list.map {
                if (it.status == AgentStatus.WAITING) it.copy(status = AgentStatus.RUNNING) else it
            }
        }
    }

    fun toggleAgentStatus(agentId: String) {
        _agents.update { list ->
            list.map {
                if (it.id == agentId) {
                    val nextStatus = when (it.status) {
                        AgentStatus.RUNNING -> AgentStatus.WAITING
                        AgentStatus.WAITING -> AgentStatus.IDLE
                        AgentStatus.IDLE -> AgentStatus.RUNNING
                        AgentStatus.ACTIVE -> AgentStatus.WAITING
                        AgentStatus.DONE -> AgentStatus.RUNNING
                    }
                    it.copy(status = nextStatus)
                } else it
            }
        }
    }

    // ─────────────────────────── PERMISSIONS ───────────────────────────

    fun cyclePermission(permissionId: String) {
        _permissions.update { list ->
            list.map {
                if (it.id == permissionId) {
                    val nextState = when (it.state) {
                        PermissionState.ALLOWED -> PermissionState.LIMITED
                        PermissionState.LIMITED -> PermissionState.BLOCKED
                        PermissionState.BLOCKED -> PermissionState.ALLOWED
                    }
                    it.copy(state = nextState)
                } else it
            }
        }
    }

    // ─────────────────────────── COMMAND LINE ───────────────────────────

    fun setCommandInput(cmd: String) {
        _currentCommandInput.value = cmd
    }

    fun executeCommand(commandStr: String) {
        val cmd = commandStr.trim().ifEmpty { "status all" }
        _currentCommandInput.value = ""
        _isCommandProcessing.value = true
        _commandProgress.value = 0.1f

        viewModelScope.launch {
            // Pick routing agents based on command
            val agentsToRoute = when {
                cmd.contains("scan", ignoreCase = true) || cmd.contains("device", ignoreCase = true) ->
                    listOf("Automation Agent", "Security Agent", "Code Agent")
                cmd.contains("health", ignoreCase = true) || cmd.contains("bio", ignoreCase = true) ->
                    listOf("Health Agent", "Research Agent")
                cmd.contains("news", ignoreCase = true) || cmd.contains("world", ignoreCase = true) ->
                    listOf("Research Agent", "Finance Agent")
                else ->
                    listOf("Health Agent", "Research Agent", "Calendar Agent")
            }
            _routedAgents.value = agentsToRoute

            val outputs = mutableListOf(
                "Command received: '$cmd'",
                "Resolving dependency graph across neural agents...",
                "Allocated 128MB shared cache memory pool"
            )
            _liveCommandOutputs.value = outputs
            _commandProgress.value = 0.35f

            delay(600)
            outputs.add("Dispatched execution task to: ${agentsToRoute.joinToString(", ")}")
            _liveCommandOutputs.value = outputs.toList()
            _commandProgress.value = 0.62f

            delay(700)
            outputs.add("Telemetry validation: 100% verified nominal")
            outputs.add("Completed execution of '$cmd' with exit code 0")
            _liveCommandOutputs.value = outputs.toList()
            _commandProgress.value = 1.0f

            _commandLogs.update {
                listOf(
                    CommandLog(
                        id = "cmd_${System.currentTimeMillis()}",
                        command = cmd,
                        status = "SUCCESS",
                        outputLines = outputs.toList(),
                        routedAgents = agentsToRoute,
                        timestamp = System.currentTimeMillis()
                    )
                ) + it
            }

            delay(400)
            _isCommandProcessing.value = false
        }
    }

    // ─────────────────────────── KILL SWITCH ───────────────────────────

    fun confirmKillSwitch() {
        // Stop all agents immediately
        _agents.update { list ->
            list.map { it.copy(status = AgentStatus.IDLE, transcript = "TERMINATED BY KILL SWITCH", progress = 0f) }
        }
        _isCommandProcessing.value = false
        // Return to boot screen
        navBackStack.clear()
        _currentScreen.value = NovaNavTarget.BOOT
    }

    // ─────────────────────────── DEVICES & MULTI-SYNC ───────────────────────────

    fun toggleMultiDeviceSync() {
        _isMultiDeviceSyncEnabled.update { !it }
    }

    override fun onCleared() {
        super.onCleared()
        deviceReader.stop()
    }
}
