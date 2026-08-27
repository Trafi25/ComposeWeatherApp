# WeatherApp

![Android CI](https://github.com/Trafi25/ComposeWeatherApp/actions/workflows/android-ci.yml/badge.svg)

A modern, clean, and feature-rich weather application built with **Jetpack Compose**. This app provides real-time weather data, forecasts, and city management with a focus on smooth animations and a premium user experience.

## Screenshots

| Main Screen | City Search | Settings | 7-Day Forecast |
| :---: | :---: | :---: | :---: |
| ![Main Screen](screenshots/main_screen_screenshot.jpg) | ![City Search](screenshots/cities_screenshot.jpg) | ![Settings](screenshots/settings_screenshot.jpg) | ![7-Day Forecast](screenshots/7days_screenshot.jpg) |

## ✨ Features

- **Real-time Weather**: Current temperature, weather conditions, humidity, wind speed, and pressure.
- **7-Day Forecast**: Long-term weather outlook.
- **City Management**: Search for any city globally and save your favorites for quick access.
- **Location Awareness**: Automatic weather detection based on your current GPS location.
- **Home Screen Widget**: Stay updated at a glance with a custom widget built using **Jetpack Glance**, supporting dynamic updates and multiple states (Success, Error, No Location).
- **Resilient Caching**:
  - **Network Fallback**: Smart fallback to **Room** cache during network failures (`IOException`) or server errors (`HttpException`).
  - **Location-based Keys**: Weather data is cached using coordinate-based keys (rounded to 4 decimal places) for high-precision local updates.
- **AI Weather Assistant**: Personalized weather insights and clothing recommendations powered by **Google Gemini (Firebase AI)**.
  - **Cost-efficient Caching**: AI summaries are cached for **2 hours** to optimize performance and reduce API token consumption.
- **Daily Notifications**: Smart background updates that notify you of the weather every morning at **7:00 AM** using **WorkManager**.
- **Highly Customizable**:
  - Toggle between Celsius and Fahrenheit.
  - Choose preferred units for Wind Speed, Pressure, and Precipitation.
  - Support for 12h/24h time formats.
  - **Theming**: Dark/Light mode support and custom accent colors.
- **Premium UX**:
  - **Spring Physics**: Smooth, elastic navigation transitions.
  - **Auto-Scrolling Title**: Long location names automatically scroll (Marquee) in the top bar.
  - **Adaptive UI**: Responsive layouts and graphics that scale beautifully across all screen sizes.
  - **Smart Hourly Forecasts**: Automatic calculation of "Upcoming Hours" starting from the current hour, ensuring the UI always shows relevant future data.
  - **Pull-to-Refresh**: Seamlessly sync the latest data and reset forecast timelines.

## Testing & CI/CD

- **Unit Testing**: Over **29 comprehensive unit tests** covering ViewModels, Use Cases, Mappers, and API parsing.
- **Automated CI**: Full **GitHub Actions** pipeline (`android-ci.yml`) that automatically checks code formatting (Spotless), runs all unit tests, and verifies the build on every push.
- **Mocking**: Robust test environment using **MockK** for dependency behavior and **MockWebServer** for network layer verification.

## Tech Stack

- **UI**: **Jetpack Compose** for a modern declarative UI.
- **Widget**: **Jetpack Glance** for interactive home screen widgets.
- **Architecture**: Clean Architecture with MVVM + MVI pattern.
- **Processing**: **KSP (Kotlin Symbol Processing)** for faster compilation and better Kotlin support.
- **Dependency Injection**: **Hilt** + Hilt-Work for background injection.
- **Networking**: **Retrofit** + **Moshi** for API communication.
- **Background Tasks**: **WorkManager** for scheduled notifications and widget updates.
- **Local Database**: **Room** for caching and storage.
- **Preferences**: **DataStore** for persistent settings.
- **Animations**: Compose Animation APIs with custom Spring physics.

## Project Structure

The project follows a strict package-by-feature organization within clean architecture layers:

```text
com.plcoding.weatherapp
├── data                # Data layer (Repositories, DTOs, DAOs, Mappers)
│   ├── local           # Room database and Local Data Sources
│   ├── remote          # Retrofit API interfaces and DTOs
│   └── repository      # Implementation of Domain Repositories
├── domain              # Domain layer (Models, Repository Interfaces, Use Cases)
│   ├── location        # Location-related entities
│   ├── repository      # Repository definitions
│   ├── usecase         # Business logic (Pure Kotlin Use Cases)
│   └── weather         # Weather domain models
├── presentation        # UI layer (Composables, ViewModels, State)
│   ├── ui.theme        # Color, Type, and Theme definitions
│   ├── weather         # Weather feature screens and components
│   │   ├── current     # Current weather components
│   │   ├── hourly      # Hourly forecast components
│   │   ├── daily       # Daily forecast components
│   │   └── state       # MVI State and Actions
│   └── widget          # Jetpack Glance widget implementation
│       ├── components  # Widget UI components
│       └── worker      # Widget background update logic
├── notification        # WorkManager Workers and Notification logic
└── di                  # Dependency Injection modules
```

## Getting Started

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Trafi25/ComposeWeatherApp.git
   ```
2. **Open in Android Studio**:
   Import the project and wait for Gradle sync.
3. **Run**:
   Select an emulator or physical device and click **Run**.

---
*Built with ❤️ using Jetpack Compose.*
