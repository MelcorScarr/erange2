package com.example.florianpilsl.erange

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds


class Erange : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_erange)
        checkPermissions()

        val summaryFragment = SummaryFragment.newInstance()
        val placeAutoCompleteAdapter: PlaceAutoCompleteAdapter
        val latLangBounds: LatLngBounds = LatLngBounds(LatLng(-40.0, -168.0), LatLng(71.0, 136.0))
        val mGoogleApiClient: GoogleApiClient = GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).enableAutoManage(this, this).build()

        placeAutoCompleteAdapter = PlaceAutoCompleteAdapter(this, mGoogleApiClient, latLangBounds, null);

        val navigationView: BottomNavigationView? = findViewById(R.id.navigationView)
        val parameterFragment = ParameterFragment.newInstance(placeAutoCompleteAdapter)
        val mapFragment = MapFragment.newInstance()

        parameterFragment.placeAutoCompleteAdapter = placeAutoCompleteAdapter

        navigationView!!.setOnNavigationItemSelectedListener { item ->
            Log.d("menu", "Chosen:$item")
            if (item.toString() == "Planning") {
                supportFragmentManager.beginTransaction().replace(R.id.eRange, parameterFragment, "ParameterFragment").commit()
            } else if (item.toString() == "Map") {
                supportFragmentManager.beginTransaction().replace(R.id.eRange, mapFragment, "MapFragment").commit()
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
