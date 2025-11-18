package com.example.template.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationProvider(context: Context) {

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getLastKnownLocation(): Location? =
        suspendCancellableCoroutine { cont ->
            val cancellationTokenSource = CancellationTokenSource()

            fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token)
                .addOnSuccessListener { location: Location? ->
                    if (cont.isActive) {
                        cont.resume(location)
                    }
                }
                .addOnFailureListener {
                    // If it fails, just return null.
                    // The view model or screen will have to handle this case.
                    if (cont.isActive) {
                        cont.resume(null)
                    }
                }

            cont.invokeOnCancellation {
                cancellationTokenSource.cancel()
            }
        }
}
