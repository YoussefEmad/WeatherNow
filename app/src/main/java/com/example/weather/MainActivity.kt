package com.example.weather

import CityInputScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weather.features.currentweather.CurrentWeatherScreen
import com.example.weather.features.forecast.ForecastScreen
import com.example.weather.ui.theme.WeatherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            WeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherApp()
                }
            }
        }
    }
}

@Composable
fun WeatherApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "cityInput"
    ) {
        composable("cityInput") {
            CityInputScreen(
                onNavigateToCurrentWeather = { cityName ->
                    navController.navigate("currentWeather/$cityName")
                },
                onNavigateToForecast = { cityName ->
                    navController.navigate("forecast/$cityName")
                }
            )
        }
        
        composable(
            route = "currentWeather/{cityName}",
            arguments = listOf(
                navArgument("cityName") { type = NavType.StringType }
            )
        ) {
            CurrentWeatherScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = "forecast/{cityName}",
            arguments = listOf(
                navArgument("cityName") { type = NavType.StringType }
            )
        ) {
            ForecastScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}


