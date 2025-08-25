package com.example.weather.features.cityinput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.core.Result
import com.example.weather.domain.model.CurrentWeather
import com.example.weather.domain.usecase.GetCurrentWeatherUseCase
import com.example.weather.domain.usecase.GetLastSearchedCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityInputViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getLastSearchedCityUseCase: GetLastSearchedCityUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CityInputUiState())
    val uiState: StateFlow<CityInputUiState> = _uiState.asStateFlow()

    private val _cityInput = MutableStateFlow("")
    val cityInput: StateFlow<String> = _cityInput.asStateFlow()

    private val _navigateToWeatherEvent = MutableStateFlow<String?>(null)
    val navigateToWeatherEvent: StateFlow<String?> = _navigateToWeatherEvent.asStateFlow()

    init {
        loadLastSearchedCityIntoInput()
    }

    fun onCityInputChange(input: String) {
        _cityInput.value = input
        if (_uiState.value.weatherData != null && input != _uiState.value.weatherData?.name) {
            _uiState.update { it.copy(weatherData = null) }
        }
        if (_uiState.value.error != null) {
            clearError()
        }
    }

    fun searchWeather() {
        val cityName = _cityInput.value.trim()
        if (cityName.isBlank()) {
            _uiState.update {
                it.copy(
                    error = "Please enter a city name",
                    weatherData = null
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                error = null,
                weatherData = null
            )
        }

        viewModelScope.launch {
            when (val result = getCurrentWeatherUseCase(cityName)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            weatherData = result.data,
                            error = null
                        )
                    }

                    _navigateToWeatherEvent.value = result.data.name
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            weatherData = null,
                            error = result.exception.message ?: "Unknown error occurred"
                        )
                    }
                }
                is Result.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun loadLastSearchedCityIntoInput() {
        viewModelScope.launch {
            getLastSearchedCityUseCase().collect { cityEntity ->
                cityEntity?.let { city ->
                    _cityInput.value = city.name
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun onNavigationHandled() {
        _navigateToWeatherEvent.value = null
    }
}

data class CityInputUiState(
    val isLoading: Boolean = false,
    val weatherData: CurrentWeather? = null,
    val error: String? = null
)
