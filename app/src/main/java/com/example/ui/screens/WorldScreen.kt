package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GaugeRing
import com.example.ui.components.GlassPanel
import com.example.ui.components.NovaTopBar
import com.example.ui.theme.NovaAlertDanger
import com.example.ui.theme.NovaBackgroundBase
import com.example.ui.theme.NovaBackgroundElevated
import com.example.ui.theme.NovaPanelBorder
import com.example.ui.theme.NovaPrimaryAccent
import com.example.ui.theme.NovaSecondaryAccent
import com.example.ui.theme.NovaTextPrimary
import com.example.ui.theme.NovaTextSecondary
import com.example.ui.theme.NovaTextTertiary
import com.example.ui.theme.NovaWarning
import com.example.viewmodel.NovaViewModel
import java.util.Locale

@Composable
fun WorldScreen(
    viewModel: NovaViewModel,
    modifier: Modifier = Modifier
) {
    val worldState by viewModel.worldState.collectAsState()
    val context = LocalContext.current

    var selectedNewsCategoryIndex by remember { mutableIntStateOf(0) }
    val newsCategories = listOf("World", "Tech", "India")

    val weather = worldState.weather
    val aqi = worldState.airQuality
    val cryptos = worldState.cryptos
    val stocks = worldState.stocks
    val news = worldState.news
    val worldClocks = worldState.worldClocks

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaBackgroundBase)
            .testTag("world_screen")
    ) {
        // Top Bar
        NovaTopBar(
            title = "World",
            subtitle = "Live global feed & telemetry",
            onBackClick = { viewModel.navigateBack() },
            onRefreshClick = { viewModel.refreshWorldData() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // WEATHER SECTION (Open-Meteo)
            item {
                SectionHeader("LIVE METEOROLOGICAL TELEMETRY", Icons.Default.WbSunny)
                Spacer(modifier = Modifier.height(6.dp))

                GlassPanel(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = weather.cityName.uppercase(),
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = NovaPrimaryAccent,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "${weather.temperatureC.toInt()}°C",
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 32.sp,
                                    color = NovaTextPrimary
                                )
                                Text(
                                    text = "${weather.condition} · Feels ${weather.feelsLikeC.toInt()}°C",
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 12.sp,
                                    color = NovaTextSecondary
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Humidity: ${weather.humidityPercent}%",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = NovaTextSecondary
                                )
                                Text(
                                    text = "Wind: ${weather.windSpeedKmh} km/h",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = NovaTextSecondary
                                )
                                Text(
                                    text = "UV Index: ${weather.uvIndex}",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = NovaWarning
                                )
                                Text(
                                    text = "Sun: ${weather.sunrise} - ${weather.sunset}",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = NovaTextTertiary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "7-Day Orbital Forecast:",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = NovaTextTertiary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // 7-day forecast horizontal scroll
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(weather.forecast) { day ->
                                Box(
                                    modifier = Modifier
                                        .background(NovaBackgroundElevated, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = day.dayName,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 10.sp,
                                            color = NovaPrimaryAccent
                                        )
                                        Text(
                                            text = "${day.maxTempC.toInt()}°",
                                            fontFamily = FontFamily.SansSerif,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = NovaTextPrimary
                                        )
                                        Text(
                                            text = "${day.minTempC.toInt()}°",
                                            fontFamily = FontFamily.SansSerif,
                                            fontSize = 10.sp,
                                            color = NovaTextTertiary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // AIR QUALITY SECTION
            item {
                SectionHeader("ATMOSPHERIC AIR QUALITY (AQI)", Icons.Default.Air)
                Spacer(modifier = Modifier.height(6.dp))

                GlassPanel(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GaugeRing(
                            percentage = (aqi.aqi.toFloat() / 200f * 100f).coerceIn(0f, 100f),
                            size = 80.dp,
                            strokeWidth = 6.dp,
                            customColor = if (aqi.aqi > 100) NovaWarning else NovaPrimaryAccent
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${aqi.aqi}",
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = NovaTextPrimary
                                )
                                Text(
                                    text = "AQI",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 8.sp,
                                    color = NovaPrimaryAccent
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Status: ${aqi.status}",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = NovaTextPrimary
                            )
                            Text(
                                text = "PM2.5: ${aqi.pm25} µg/m³ · PM10: ${aqi.pm10} µg/m³",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = NovaSecondaryAccent
                            )
                            Text(
                                text = "Ground Ozone: ${aqi.ozone} µg/m³",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = NovaTextTertiary
                            )
                        }
                    }
                }
            }

            // CRYPTO SECTION (CoinGecko Live)
            item {
                SectionHeader("CRYPTO & DIGITAL ASSETS", Icons.Default.CurrencyBitcoin)
                Spacer(modifier = Modifier.height(6.dp))

                GlassPanel(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        cryptos.forEach { coin ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "${coin.name} (${coin.symbol})",
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = NovaTextPrimary
                                    )
                                    Text(
                                        text = "₹${String.format(Locale.US, "%,.0f", coin.priceInr)}",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        color = NovaTextTertiary
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "$${String.format(Locale.US, "%,.2f", coin.priceUsd)}",
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = NovaTextPrimary
                                    )
                                    Text(
                                        text = (if (coin.change24h >= 0) "+" else "") + "${coin.change24h}%",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (coin.change24h >= 0) NovaPrimaryAccent else NovaAlertDanger
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // STOCKS SECTION
            item {
                SectionHeader("GLOBAL EQUITY INDICES", Icons.Default.ShowChart)
                Spacer(modifier = Modifier.height(6.dp))

                GlassPanel(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        stocks.forEach { stock ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stock.name,
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = NovaTextPrimary
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = String.format(Locale.US, "%,.2f", stock.price),
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        color = NovaTextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = (if (stock.changePercent >= 0) "+" else "") + "${stock.changePercent}%",
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = if (stock.changePercent >= 0) NovaPrimaryAccent else NovaAlertDanger
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // NEWS SECTION
            item {
                SectionHeader("INTELLIGENCE WIRE & NEWS", Icons.Default.Newspaper)
                Spacer(modifier = Modifier.height(6.dp))

                // Tabs for News
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    newsCategories.forEachIndexed { idx, cat ->
                        val isSelected = selectedNewsCategoryIndex == idx
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSelected) NovaPrimaryAccent.copy(alpha = 0.2f) else NovaBackgroundElevated,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable { selectedNewsCategoryIndex = idx }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = cat.uppercase(),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) NovaPrimaryAccent else NovaTextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val filteredNews = news.filter {
                    when (selectedNewsCategoryIndex) {
                        0 -> it.category == "World" || it.category == "Tech"
                        1 -> it.category == "Tech"
                        else -> it.category == "India" || it.category == "World"
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    filteredNews.take(5).forEach { item ->
                        GlassPanel(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                                        context.startActivity(intent)
                                    } catch (_: Exception) {}
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.title,
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        lineHeight = 18.sp,
                                        color = NovaTextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row {
                                        Text(
                                            text = item.source,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 10.sp,
                                            color = NovaPrimaryAccent
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "· ${item.timeAgo}",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 10.sp,
                                            color = NovaTextTertiary
                                        )
                                    }
                                }

                                Icon(
                                    imageVector = Icons.Default.OpenInBrowser,
                                    contentDescription = "Open",
                                    tint = NovaTextTertiary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // TIME & WORLD CLOCK SECTION
            item {
                SectionHeader("CHRONO TELEMETRY & WORLD CLOCKS", Icons.Default.AccessTime)
                Spacer(modifier = Modifier.height(6.dp))

                GlassPanel(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        worldClocks.forEach { clock ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = clock.cityName,
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = NovaTextPrimary
                                    )
                                    Text(
                                        text = "${clock.date} · ${clock.timeDiff}",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        color = NovaTextTertiary
                                    )
                                }
                                Text(
                                    text = clock.time,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = NovaSecondaryAccent
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}
