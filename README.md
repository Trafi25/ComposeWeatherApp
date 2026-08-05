# WeatherApp

A modern, clean, and feature-rich weather application built with **Jetpack Compose**. This app provides real-time weather data, forecasts, and city management with a focus on smooth animations and a premium user experience.

## Screenshots

|                 Main Screen                  | City Search | Settings | 7-Day Forecast |
|:--------------------------------------------:| :---: | :---: | :---: |
| ![Main Scree n](screenshots/main_screen_screenshot.jpg) | ![City Search](screenshots/cities_screenshot.jpg) | ![Settings](screenshots/settings_screenshot.jpg) | ![7-Day Forecast](screenshots/7days_screenshot.jpg) |

## Features

- **Real-time Weather**: Current temperature, weather conditions, humidity, wind speed, and pressure.
- **7-Day Forecast**: Long-term weather outlook.
- **City Management**: Search for any city globally and save your favorites for quick access.
- **Location Awareness**: Automatic weather detection based on your current GPS location.
- **Highly Customizable**:
  - Toggle between Celsius and Fahrenheit.
  - Choose preferred units for Wind Speed, Pressure, and Precipitation.
  - Support for 12h/24h time formats.
  - **Theming**: Dark/Light mode support and custom accent colors.
- **Premium UX**:
  - **Spring Physics**: Smooth, elastic navigation transitions.
  - **Auto-Scrolling Title**: Long location names automatically scroll (Marquee) in the top bar.
  - **Pull-to-Refresh**: Easily sync the latest data and reset forecast timelines.

## Tech Stack

- **UI**: Jetpack Compose for a fully declarative UI.
- **Architecture**: Clean Architecture with MVVM.
- **Dependency Injection**:Hilt for robust DI.
- **Networking**: Retrofit Moshi for API communication.
- **Local Database**: Room for caching weather data and saving cities.
- **Preferences**: DataStore for persistent settings.
- **Location**: Google Play Services Location API.
- **Animations**: Compose Animation APIs with custom Spring specs.

## Project Structure

The project follows a modular package-by-feature structure within clean architecture layers:

```text
com.plcoding.weatherapp
├── data                # Data layer (Repositories, DTOs, DAOs, Mappers)
│   ├── local           # Room database and Local Data Sources
│   ├── remote          # Retrofit API interfaces and DTOs
│   └── repository      # Implementation of Domain Repositories
├── domain              # Domain layer (Models, Repository Interfaces, Use Cases)
│   ├── location        # Location-related entities
│   ├── repository      # Repository definitions
│   ├── usecase         # Business logic (Use Cases)
│   └── weather         # Weather domain models
├── presentation        # UI layer (Composables, ViewModels, State)
│   ├── ui.theme        # Color, Type, and Theme definitions
│   └── weather         # Weather feature screens and components
│       ├── current     # Current weather components
│       ├── hourly      # Hourly forecast components
│       ├── daily       # Daily forecast components
│       └── state       # UI State and Actions
└── di                  # Dependency Injection modules
```

## Getting Started

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/WeatherApp.git
   ```
2. **Open in Android Studio**:
   Import the project and wait for Gradle sync.
3. **Run**:
   Select an emulator or physical device and click **Run**.

---
*Built with ❤️ using Jetpack Compose.*
