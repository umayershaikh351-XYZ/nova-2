package com.example.data.model

enum class PermissionState(val label: String) {
    ALLOWED("Allowed"),
    LIMITED("Limited"),
    BLOCKED("Blocked")
}

data class PermissionItem(
    val id: String,
    val name: String,
    val description: String,
    val state: PermissionState,
    val iconName: String
)

data class GoalItem(
    val id: String,
    val title: String,
    val category: String,
    val progress: Float,
    val targetDate: String
)

data class HabitItem(
    val id: String,
    val name: String,
    val streakDays: Int,
    val completedToday: Boolean
)

data class RelationshipItem(
    val id: String,
    val name: String,
    val role: String,
    val lastContact: String,
    val trustScore: Int
)

data class MemoryItem(
    val id: String,
    val title: String,
    val summary: String,
    val timestamp: String,
    val category: String
)

data class ConnectedDeviceItem(
    val id: String,
    val name: String,
    val platform: String,
    val isConnected: Boolean,
    val batteryPercent: Int?,
    val isCurrentDevice: Boolean = false
)

data class ActivityEvent(
    val id: String,
    val iconType: String,
    val title: String,
    val description: String,
    val timestamp: String
)

data class CommandLog(
    val id: String,
    val command: String,
    val status: String,
    val outputLines: List<String>,
    val routedAgents: List<String>,
    val timestamp: Long
)
