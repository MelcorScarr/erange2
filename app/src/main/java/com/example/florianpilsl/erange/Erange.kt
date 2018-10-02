package com.example.florianpilsl.erange

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log

import kotlinx.android.synthetic.main.activity_erange.*
import android.support.annotation.NonNull
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem


class Erange : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_erange)
        checkPermissions()

        val navigationView: BottomNavigationView? = findViewById(R.id.navigationView)
        val parameterFragment = ParameterFragment.newInstance()
        val mapFragment = MapFragment.newInstance()
        val summaryFragment = SummaryFragment.newInstance()
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
