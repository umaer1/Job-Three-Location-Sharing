package com.example.jobthreelocationsharing

import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient

import com.google.android.gms.tasks.OnCompleteListener


class LocationViewModel : ViewModel() {

    private var fusedLocationClient: FusedLocationProviderClient? = null

    fun getLastLocation(callback: (String) -> Unit) {
        fusedLocationClient?.lastLocation
            ?.addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val lastLocation = task.result
                    val latitude = lastLocation.latitude
                    val longitude = lastLocation.longitude
                    val location = "Lat: $latitude, Long: $longitude"
                    callback(location)
                } else {
                    // Handle failure or missing permissions
                    callback("Location not available")
                }
            })
    }

    fun initializeFusedLocationClient(client: FusedLocationProviderClient) {
        fusedLocationClient = client
    }
}