package com.example.jobthreelocationsharing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.jobthreelocationsharing.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var firestoreViewModel: FirestoreViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)

        firestoreViewModel = ViewModelProvider(this).get(FirestoreViewModel::class.java)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Fetch user locations from Firestore and add markers
        firestoreViewModel.getAllUsers { userList ->
            for (user in userList) {
                val userLocation = user.location
                if (userLocation.isNotEmpty()) {
                    val latLng = parseLocation(userLocation)
                    val markerOptions = MarkerOptions().position(latLng).title(user.displayName)
                    googleMap.addMarker(markerOptions)
                }
            }
        }
    }

    private fun parseLocation(location: String): LatLng {
        val latLngSplit = location.split(", ")
        val latitude = latLngSplit[0].substringAfter("Lat: ").toDouble()
        val longitude = latLngSplit[1].substringAfter("Long: ").toDouble()
        return LatLng(latitude, longitude)
    }
}