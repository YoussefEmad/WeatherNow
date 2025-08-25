package com.example.weather.features.currentweather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.util.Locale
import kotlin.math.roundToInt

private fun kelvinToCelsius(kelvin: Double): Double = kelvin - 273.15

private fun String.capitalizeFirstLetterOnly(): String {
    if (isEmpty()) return this
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentWeatherScreen(
    onNavigateBack: () -> Unit,
    viewModel: CurrentWeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Current Weather",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E3A8A)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E3A8A),
                            Color(0xFF3B82F6),
                            Color(0xFF60A5FA)
                        )
                    )
                )
        ) {
            when {
                uiState.isLoading && uiState.weatherData == null -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }
                uiState.error != null && uiState.weatherData == null -> {
                    ErrorState(
                        errorMessage = uiState.error ?: "An unknown error occurred.",
                        onRetry = { viewModel.refreshWeather() }
                    )
                }
                uiState.weatherData != null -> {
                    WeatherContent(
                        weatherData = uiState.weatherData!!,
                        isLoading = uiState.isLoading
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Loading weather data...",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.refreshWeather() }) {
                            Text("Try Refresh")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WeatherContent(
    weatherData: com.example.weather.domain.model.CurrentWeather,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${weatherData.name}, ${weatherData.sys.country}",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                )
            )
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .padding(vertical = 8.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.90f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = weatherData.weather.firstOrNull()?.description ?: "Weather condition",
                    tint = Color(0xFFFF9800),
                    modifier = Modifier.size(88.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))

                val tempInCelsius = kelvinToCelsius(weatherData.main.temp)
                Text(
                    text = "${tempInCelsius.roundToInt()}°C",
                    style = MaterialTheme.typography.displayLarge.copy(
                        color = Color(0xFF0D47A1),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 80.sp
                    )
                )

                Text(
                    text = weatherData.weather.firstOrNull()?.description?.capitalizeFirstLetterOnly() ?: "",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color(0xFF424242),
                        fontWeight = FontWeight.Medium,
                        fontSize = 22.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val tempMaxInCelsius = kelvinToCelsius(weatherData.main.tempMax)
                    val tempMinInCelsius = kelvinToCelsius(weatherData.main.tempMin)

                    TempMinMaxColumn("High", "${tempMaxInCelsius.roundToInt()}°C", Color(0xFFD32F2F))
                    TempMinMaxColumn("Low", "${tempMinInCelsius.roundToInt()}°C", Color(0xFF1976D2))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.90f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Weather Details",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = Color(0xFF0D47A1),
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                val feelsLikeInCelsius = kelvinToCelsius(weatherData.main.feelsLike)
                WeatherDetailRow(
                    icon = Icons.Default.Thermostat,
                    label = "Feels Like",
                    value = "${feelsLikeInCelsius.roundToInt()}°C",
                    iconColor = Color(0xFFF4511E)
                )
                CustomDivider()
                WeatherDetailRow(
                    icon = Icons.Default.Air,
                    label = "Wind",
                    value = "${(weatherData.wind.speed * 3.6).roundToInt()} km/h",
                    iconColor = Color(0xFF1E88E5)
                )
                CustomDivider()
                WeatherDetailRow(
                    icon = Icons.Default.Opacity,
                    label = "Humidity",
                    value = "${weatherData.main.humidity}%",
                    iconColor = Color(0xFF00ACC1)
                )
                CustomDivider()
                WeatherDetailRow(
                    icon = Icons.Default.Visibility,
                    label = "Pressure",
                    value = "${weatherData.main.pressure.roundToInt()} hPa",
                    iconColor = Color(0xFF5E35B1)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun TempMinMaxColumn(label: String, value: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF757575),
                fontSize = 14.sp
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                color = valueColor,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
    }
}

@Composable
private fun WeatherDetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    iconColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF212121),
                fontWeight = FontWeight.Medium,
                fontSize = 17.sp
            ),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF0D47A1),
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp
            )
        )
    }
}

@Composable
private fun CustomDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 8.dp),
        thickness = 0.5.dp,
        color = Color.LightGray.copy(alpha = 0.6f)
    )
}

@Composable
private fun ErrorState(errorMessage: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Oops! Something went wrong.",
            style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.8f)),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("Try Again", color = Color(0xFF1E3A8A))
        }
    }
}
