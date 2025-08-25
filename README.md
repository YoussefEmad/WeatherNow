# Weather Now & Later

A comprehensive Android weather application that fetches and displays current weather as well as a 7-day forecast for any given city.

## Features

- **City Input Screen**: Search for weather by city name with MVVM architecture
- **Current Weather Display**: View detailed current weather information with MVVM architecture
- **7-Day Forecast**: Browse extended weather forecast using MVI architecture
- **Local Storage**: Automatically saves and recalls the last searched city
- **Dark Mode Support**: Automatic theme switching based on system preferences
- **Offline Support**: Caches weather data locally for better user experience

## Architecture

The app follows **Clean Architecture** principles with multiple architectural patterns:

- **MVVM**: Used for city input and current weather screens
- **MVI**: Used for the 7-day forecast list
- **Clean Architecture**: Structured into Presentation, Domain, and Data layers
- **SOLID Principles**: Following Object-Oriented Programming best practices

## Project Structure

```
app/
├── src/main/java/com/example/weather/
│   ├── MainActivity.kt
│   ├── WeatherApplication.kt
│   └── di/
│       ├── NetworkModule.kt
│       ├── DatabaseModule.kt
│       └── RepositoryModule.kt

core/
├── src/main/java/com/example/weather/core/
│   ├── Result.kt
│   ├── Constants.kt
│   └── extensions/
│       └── Extensions.kt

data/
├── src/main/java/com/example/weather/data/
│   ├── api/
│   │   └── WeatherApi.kt
│   ├── local/
│   │   ├── WeatherDatabase.kt
│   │   ├── entity/
│   │   │   └── CityEntity.kt
│   │   └── dao/
│   │       └── CityDao.kt
│   ├── model/
│   │   └── WeatherModels.kt
│   └── repository/
│       └── WeatherRepositoryImpl.kt

domain/
├── src/main/java/com/example/weather/domain/
│   ├── repository/
│   │   └── WeatherRepository.kt
│   └── usecase/
│       ├── GetCurrentWeatherUseCase.kt
│       ├── GetForecastUseCase.kt
│       └── GetLastSearchedCityUseCase.kt

features/
├── cityinput/
│   ├── CityInputViewModel.kt
│   └── CityInputScreen.kt
├── currentweather/
│   ├── CurrentWeatherViewModel.kt
│   └── CurrentWeatherScreen.kt
└── forecast/
    ├── ForecastIntent.kt
    ├── ForecastState.kt
    ├── ForecastEffect.kt
    ├── ForecastViewModel.kt
    └── ForecastScreen.kt

weatherlibrary/
├── src/main/java/com/example/weather/weatherlibrary/
│   ├── WeatherFormatter.kt
│   └── WeatherIconHelper.kt
```

## Technologies Used

- **Kotlin**: Primary programming language
- **Jetpack Compose**: Modern UI toolkit
- **Dagger Hilt**: Dependency injection
- **Retrofit**: HTTP client for API calls
- **Room**: Local database
- **Coroutines**: Asynchronous programming
- **Flow**: Reactive streams
- **Navigation Compose**: Screen navigation
- **Coil**: Image loading
- **Material 3**: Design system

## API Integration

The app integrates with [OpenWeatherMap API](https://openweathermap.org/api) to fetch:
- Current weather data
- 7-day weather forecast
- Weather icons and descriptions

## Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd vodafoneTask
   ```

2. **Get API Key**
   - Sign up at [OpenWeatherMap](https://openweathermap.org/api)
   - Get your free API key
   - Replace `YOUR_API_KEY_HERE` in `core/src/main/java/com/example/weather/core/Constants.kt`

3. **Build and Run**
   ```bash
   ./gradlew build
   ./gradlew installDebug
   ```

## Testing

The app includes comprehensive testing:

- **Unit Tests**: Covering business logic and data layers
- **Instrumented Tests**: Testing UI components
- **Test Coverage**: Aiming for 80%+ code coverage

Run tests with:
```bash
./gradlew test                    # Unit tests
./gradlew connectedAndroidTest    # Instrumented tests
```

## CI/CD Pipeline

The project includes GitHub Actions workflow that:
- Runs linting
- Executes unit tests
- Runs instrumented tests
- Builds APK
- Publishes the weather library to Maven local

## Custom Weather Library

The `weatherlibrary` module provides:
- **WeatherFormatter**: Temperature, humidity, pressure, and wind speed formatting
- **WeatherIconHelper**: Weather icon URL generation and condition mapping

This library can be published to Maven local or artifactory for reuse in other projects.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Acknowledgments

- [OpenWeatherMap](https://openweathermap.org/) for providing the weather API
- [Jetpack Compose](https://developer.android.com/jetpack/compose) for the modern UI toolkit
- [Material Design](https://material.io/) for design guidelines





