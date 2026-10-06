package com.example.data.model

data class BatteryStats(
    val levelPercent: Int = 100,
    val health: String = "GOOD",
    val temperatureC: Float = 28.5f,
    val voltageMv: Int = 4120,
    val technology: String = "Li-ion",
    val isCharging: Boolean = false,
    val chargingStatus: String = "DISCHARGING",
    val chargingSource: String = "NONE",
    val currentMa: Int = -350,
    val capacityMah: Int = 4500
)

data class CpuStats(
    val model: String = "ARM64-v8a",
    val cores: Int = 8,
    val usagePercent: Int = 18,
    val temperatureC: Float = 34.0f,
    val coreFrequenciesMhz: List<Int> = listOf(1800, 1800, 1800, 1800, 2200, 2200, 2800, 2800)
)

data class MemoryStats(
    val totalMb: Long = 8192,
    val usedMb: Long = 4200,
    val availableMb: Long = 3992,
    val usagePercent: Int = 51
)

data class StorageStats(
    val totalGb: Float = 128f,
    val usedGb: Float = 54.2f,
    val freeGb: Float = 73.8f,
    val usagePercent: Int = 42,
    val hasExternal: Boolean = false
)

data class NetworkStats(
    val isConnected: Boolean = true,
    val type: String = "WiFi",
    val ssid: String = "NOVA-SECURE-MESH",
    val signalDbm: Int = -58,
    val localIp: String = "192.168.1.104",
    val downloadSpeedMbps: Float = 48.6f,
    val uploadSpeedMbps: Float = 18.2f
)

data class SensorTelemetry(
    val ambientLightLux: Float = 240f,
    val proximityNear: Boolean = false,
    val accelX: Float = 0.05f,
    val accelY: Float = 9.81f,
    val accelZ: Float = 0.12f,
    val gyroX: Float = 0.01f,
    val gyroY: Float = -0.02f,
    val gyroZ: Float = 0.01f,
    val compassHeadingDeg: Float = 135f,
    val barometerHpa: Float = 1013.2f,
    val altitudeM: Float = 42f,
    val stepCount: Int = 6420,
    val heartRateBpm: Int? = null,
    val ambientTempC: Float? = null
)

data class DeviceHardwareInfo(
    val model: String = "Device",
    val manufacturer: String = "Android",
    val androidVersion: String = "14",
    val apiLevel: Int = 34,
    val screenResolution: String = "1080 x 2400",
    val screenDensityDpi: Int = 420,
    val uptimeFormatted: String = "1d 4h 12m",
    val bootTimeFormatted: String = "05:12 AM"
)

data class FullDeviceState(
    val battery: BatteryStats = BatteryStats(),
    val cpu: CpuStats = CpuStats(),
    val memory: MemoryStats = MemoryStats(),
    val storage: StorageStats = StorageStats(),
    val network: NetworkStats = NetworkStats(),
    val sensors: SensorTelemetry = SensorTelemetry(),
    val hardware: DeviceHardwareInfo = DeviceHardwareInfo(),
    val lastScanTimestamp: Long = System.currentTimeMillis()
)
