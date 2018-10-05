package com.example.florianpilsl.erange

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.cs.googlemaproute.DrawRoute
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng


class Erange : AppCompatActivity(), DrawRoute.onDrawRoute, OnMapReadyCallback, ParameterFragment.OnParameterInteractionListener {
    //Call this when the starting parameters have been set in the parameter fragment.

    override fun onParameterStart(start: LatLng) {
        MapFragment.start = start
    }

    //Call this when the ending parameters have been set in the parameter fragment.
    override fun onParameterEnd(end: LatLng) {
        MapFragment.end = end
    }

    override fun onMapReady(p0: GoogleMap?) {

    }

    override fun afterDraw(result: String?) {
        Log.d("AfterDraw", "string " + result)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_erange)
        checkPermissions() // Check for permissions. If not available, ask for them.

        val summaryFragment = SummaryFragment.newInstance()
        val navigationView: BottomNavigationView? = findViewById(R.id.navigationView)
        val parameterFragment = ParameterFragment()
        val mapfragment = MapFragment.newInstance()
        // Set the fragments to the proper tab
        navigationView!!.setOnNavigationItemSelectedListener { item ->
            Log.d("menu", "Chosen:$item")
            if (item.toString() == "Planning") {
                supportFragmentManager.beginTransaction().replace(R.id.eRange, parameterFragment, "ParameterFragment").commit()
            } else if (item.toString() == "Map") {
                supportFragmentManager.beginTransaction().replace(R.id.eRange, mapfragment, "MapFragment").commit()
            } else if (item.toString() == "Summary") {
                supportFragmentManager.beginTransaction().replace(R.id.eRange, summaryFragment, "SummaryFragment").commit()
            }

            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            101 -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.d("permissions", "Permission has been denied by user")
                } else {
                    Log.d("permissions", "Permission has been granted by user")
                    setupFragment()
                }
            }
        }
    }

    private fun checkPermissions() {
        val permissionFine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionFine != PackageManager.PERMISSION_GRANTED) {
            Log.d("permissions", "Permission to fine locations denied")
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    101)
        } else {
            setupFragment()
        }
    }

    private fun setupFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.eRange, MapFragment.newInstance(), "MapFragment").commit()
    }
}
