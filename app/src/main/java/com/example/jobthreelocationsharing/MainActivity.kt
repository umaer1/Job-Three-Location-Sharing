package com.example.jobthreelocationsharing


import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.content.pm.PackageManager
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth




class MainActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthenticationViewModel

    private lateinit var firestoreViewModel: FirestoreViewModel
    private lateinit var userAdapter: UserAdapter
    private lateinit var recyclerViewUsers: RecyclerView
    private lateinit var locationBtn: FloatingActionButton
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navview: NavigationView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private lateinit var locationViewModel: LocationViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getLocation()
            } else {
                // Permission denied
            }
        }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)


        navview = findViewById(R.id.navview)
        //assign drawerlayout to actionbardrawertoggole
        actionBarDrawerToggle = ActionBarDrawerToggle(this,drawerLayout, R.string.nav_open, R.string.nav_close)

        //add action listener

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //item click for open new fragment
        navview.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.profile ->{
                    startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                    drawerLayout.closeDrawers()
                }

                R.id.logout ->{
                    Firebase.auth.signOut()
                    startActivity(Intent(this@MainActivity, LogInActivity::class.java))
                    finish()
                    drawerLayout.closeDrawers()
                }

            }
            true
        }

        locationBtn = findViewById(R.id.locationBtn)

        locationBtn.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

        authViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
        firestoreViewModel = ViewModelProvider(this).get(FirestoreViewModel::class.java)

        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationViewModel.initializeFusedLocationClient(fusedLocationClient)

        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Permission is already granted
            getLocation()
        }

        recyclerViewUsers = findViewById(R.id.userRV)

        firestoreViewModel = ViewModelProvider(this).get(FirestoreViewModel::class.java)

        userAdapter = UserAdapter(emptyList())
        recyclerViewUsers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        fetchUsers()
    }

    private fun fetchUsers() {
        firestoreViewModel.getAllUsers { userList ->
            userAdapter.updateData(userList)
        }
    }

    private fun getLocation() {
        locationViewModel.getLastLocation { location ->
            // Save location to Firestore for the current user
            authViewModel.getCurrentUserId()?.let { userId ->
                firestoreViewModel.updateUserLocation(userId, location)
            }
        }
    }
    // drawer open close
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            true
        }
        else super.onOptionsItemSelected(item)
    }
}