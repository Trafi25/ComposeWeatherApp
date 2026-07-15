package com.plcoding.weatherapp.domain.location

import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume

// todo find a way to do it without android
class AndroidLocationNameResolver
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : LocationNameResolver {
        override suspend fun getLocationName(
            latitude: Double,
            longitude: Double,
        ): String? {
            if (!Geocoder.isPresent()) return null
            val geocoder = Geocoder(context, Locale.getDefault())

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getLocationNameNewApi(
                    geocoder,
                    latitude,
                    longitude,
                )
            } else {
                getLocationNameOldApi(
                    geocoder = geocoder,
                    latitude = latitude,
                    longitude = longitude,
                )
            }
        }

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private suspend fun getLocationNameNewApi(
            geocoder: Geocoder,
            latitude: Double,
            longitude: Double,
        ): String? =
            withContext(Dispatchers.IO) {
                try {
                    suspendCancellableCoroutine { continuation ->
                        geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                            val address = addresses.firstOrNull()

                            continuation.resume(address?.toDisplayName())
                        }
                    }
                    val address = geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()
                    address?.toDisplayName()
                } catch (_: Exception) {
                    null
                }
            }

        @Suppress("DEPRECATION")
        private suspend fun getLocationNameOldApi(
            geocoder: Geocoder,
            latitude: Double,
            longitude: Double,
        ): String? =
            withContext(Dispatchers.IO) {
                try {
                    val address = geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()
                    address?.toDisplayName()
                } catch (_: Exception) {
                    null
                }
            }

        private fun android.location.Address.toDisplayName(): String? {
            val city =
                locality
                    ?: subAdminArea
                    ?: adminArea

            return listOfNotNull(
                city,
                countryName,
            ).distinct()
                .joinToString(", ")
                .ifBlank { null }
        }
    }
