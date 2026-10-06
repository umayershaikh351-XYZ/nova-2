package com.example.data.model

data class DailyForecast(
    val dayName: String,
    val maxTempC: Float,
    val minTempC: Float,
    val condition: String
)

data class WeatherInfo(
    val cityName: String = "Mumbai",
    val temperatureC: Float = 29.4f,
    val feelsLikeC: Float = 32.1f,
    val condition: String = "Clear Sky",
    val weatherCode: Int = 0,
    val humidityPercent: Int = 68,
    val windSpeedKmh: Float = 14.2f,
    val uvIndex: Float = 6.4f,
    val sunrise: String = "06:28",
    val sunset: String = "18:24",
    val forecast: List<DailyForecast> = listOf(
        DailyForecast("Mon", 31f, 24f, "Sunny"),
        DailyForecast("Tue", 32f, 25f, "Partly Cloudy"),
        DailyForecast("Wed", 30f, 24f, "Clear"),
        DailyForecast("Thu", 29f, 23f, "Breezy"),
        DailyForecast("Fri", 31f, 24f, "Sunny"),
        DailyForecast("Sat", 32f, 25f, "Clear"),
        DailyForecast("Sun", 30f, 24f, "Partly Cloudy")
    )
)

data class AirQualityInfo(
    val aqi: Int = 42,
    val status: String = "Good",
    val pm25: Float = 12.4f,
    val pm10: Float = 24.1f,
    val ozone: Float = 38.6f
)

data class CryptoItem(
    val id: String,
    val symbol: String,
    val name: String,
    val priceUsd: Double,
    val priceInr: Double,
    val change24h: Double
)

data class StockIndex(
    val symbol: String,
    val name: String,
    val price: Double,
    val changePercent: Double
)

data class NewsItem(
    val title: String,
    val source: String,
    val timeAgo: String,
    val url: String,
    val category: String,
    val imageUrl: String? = null
)

data class WorldClockItem(
    val cityName: String,
    val time: String,
    val date: String,
    val timeDiff: String
)

data class FullWorldState(
    val weather: WeatherInfo = WeatherInfo(),
    val airQuality: AirQualityInfo = AirQualityInfo(),
    val cryptos: List<CryptoItem> = emptyList(),
    val stocks: List<StockIndex> = emptyList(),
    val news: List<NewsItem> = emptyList(),
    val worldClocks: List<WorldClockItem> = emptyList(),
    val lastUpdatedMinutesAgo: Int = 0,
    val isFetching: Boolean = false,
    val errorMessage: String? = null
)
