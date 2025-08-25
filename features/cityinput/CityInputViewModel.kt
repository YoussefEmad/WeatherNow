package com.example.weather.features.cityinput

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityInputScreen(
    viewModel: CityInputViewModel = hiltViewModel(),
    onNavigateToWeather: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cityInput by viewModel.cityInput.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Enter City") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = cityInput,
                onValueChange = { viewModel.onCityInputChange(it) },
                label = { Text("City Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.searchWeather() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Weather")
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }

                uiState.error != null -> {
                    Text(
                        text = uiState.error ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    if (uiState.shouldNavigate && uiState.weatherData != null) {
        LaunchedEffect(uiState.weatherData) {
            onNavigateToWeather(uiState.weatherData.city.name)
            viewModel.clearWeatherData()
        }
    }
}
