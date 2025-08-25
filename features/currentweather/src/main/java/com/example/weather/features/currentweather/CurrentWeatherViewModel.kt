package com.example.weather.features.currentweather

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.domain.usecase.GetCurrentWeatherUseCase
import com.example.weather.core.Result
import com.example.weather.domain.model.CurrentWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val cityName: String = checkNotNull(savedStateHandle["cityName"])

    private val _uiState = MutableStateFlow(CurrentWeatherUiState())
    val uiState: StateFlow<CurrentWeatherUiState> = _uiState.asStateFlow()

    init {
        loadCurrentWeather()
    }

    private fun loadCurrentWeather() {
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            when (val result = getCurrentWeatherUseCase(cityName)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        weatherData = result.data,
                        error = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exception.message ?: "Unknown error occurred"
                    )
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun refreshWeather() {
        loadCurrentWeather()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class CurrentWeatherUiState(
    val isLoading: Boolean = false,
    val weatherData: CurrentWeather? = null,
    val error: String? = null
)

