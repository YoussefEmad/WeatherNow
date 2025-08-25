package com.example.weather.features.forecast

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.domain.usecase.GetForecastUseCase
import com.example.weather.core.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val cityName: String = checkNotNull(savedStateHandle["cityName"])

    private val _state = MutableStateFlow(ForecastState(cityName = cityName))
    val state: StateFlow<ForecastState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ForecastEffect>()
    val effect = _effect.asSharedFlow()

    init {
        processIntent(ForecastIntent.LoadForecast)
    }

    fun processIntent(intent: ForecastIntent) {
        when (intent) {
            is ForecastIntent.LoadForecast -> loadForecast()
            is ForecastIntent.RefreshForecast -> loadForecast()
            is ForecastIntent.RetryLoad -> loadForecast()
        }
    }

    private fun loadForecast() {
        _state.value = _state.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            when (val result = getForecastUseCase(cityName)) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        forecastData = result.data,
                        error = null
                    )
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.exception.message ?: "Unknown error occurred"
                    )
                    _effect.emit(ForecastEffect.ShowError(result.exception.message ?: "Unknown error occurred"))
                }
                is Result.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }
}

