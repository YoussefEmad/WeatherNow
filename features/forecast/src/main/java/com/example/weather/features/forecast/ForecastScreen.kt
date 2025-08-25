package com.example.weather.features.forecast

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.weather.core.Constants
import com.example.weather.core.extensions.toDateString
import com.example.weather.weatherlibrary.WeatherFormatter

private fun String.capitalizeFirst(): String {
    return if (this.isNotEmpty()) {
        this[0].uppercase() + this.substring(1)
    } else {
        this
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    onNavigateBack: () -> Unit,
    viewModel: ForecastViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val weatherFormatter = remember { WeatherFormatter() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect: ForecastEffect ->
            when (effect) {
                is ForecastEffect.NavigateBack -> onNavigateBack()
                is ForecastEffect.ShowError -> {
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("7-Day Forecast") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.processIntent(ForecastIntent.RefreshForecast) },
                        enabled = !state.isLoading
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.error != null -> {
                    ErrorContent(
                        error = state.error!!,
                        onRetry = { viewModel.processIntent(ForecastIntent.RetryLoad(state.cityName)) }
                    )
                }
                state.forecastData != null -> {
                    ForecastContent(
                        forecastData = state.forecastData!!,
                        weatherFormatter = weatherFormatter
                    )
                }
            }
        }
    }
}

@Composable
private fun ForecastContent(
    forecastData: com.example.weather.domain.model.Forecast,
    weatherFormatter: WeatherFormatter
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "7-Day Forecast for ${forecastData.city.name}",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(forecastData.list.chunked(8)) { dailyForecast ->
                DailyForecastCard(
                    dailyForecast = dailyForecast,
                    weatherFormatter = weatherFormatter
                )
            }
        }
    }
}

@Composable
private fun DailyForecastCard(
    dailyForecast: List<com.example.weather.domain.model.ForecastItem>,
    weatherFormatter: WeatherFormatter
) {
    val firstItem = dailyForecast.firstOrNull() ?: return
    val date = firstItem.dt.toDateString("EEE, MMM dd")

    val hourlyForecastSectionHeight = 200.dp

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.height(hourlyForecastSectionHeight),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dailyForecast) { forecastItem ->
                    HourlyForecastItem(
                        forecastItem = forecastItem,
                        weatherFormatter = weatherFormatter
                    )
                }
            }
        }
    }
}


@Composable
private fun HourlyForecastItem(
    forecastItem: com.example.weather.domain.model.ForecastItem,
    weatherFormatter: WeatherFormatter
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = forecastItem.dt.toDateString("HH:mm"),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.3f)
        )

        AsyncImage(
            model = "${Constants.WEATHER_ICON_BASE_URL}${forecastItem.weather.firstOrNull()?.icon}@2x.png",
            contentDescription = "Weather Icon",
            modifier = Modifier.size(40.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = weatherFormatter.formatTemperature(forecastItem.main.temp),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(0.4f)
        )

        Text(
            text = forecastItem.weather.firstOrNull()?.description?.capitalizeFirst() ?: "",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.8f),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.weight(0.5f)
        ) {
            Text(
                text = weatherFormatter.formatHumidity(forecastItem.main.humidity),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = weatherFormatter.formatWindSpeed(forecastItem.wind.speed),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = error,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

