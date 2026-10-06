package com.example.data.model

enum class AgentStatus(val label: String) {
    RUNNING("RUNNING"),
    ACTIVE("ACTIVE"),
    WAITING("WAITING"),
    IDLE("IDLE"),
    DONE("DONE")
}

data class Agent(
    val id: String,
    val name: String,
    val role: String,
    val status: AgentStatus,
    val transcript: String,
    val progress: Float = 0f,
    val iconCategory: String = "core",
    val tasksCompleted: Int = 0,
    val memoryUsageMb: Int = 128
)
