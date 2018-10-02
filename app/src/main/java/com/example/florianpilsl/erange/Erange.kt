package com.example.florianpilsl.erange

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log

import kotlinx.android.synthetic.main.activity_erange.*

class Erange : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_erange)
        checkPermissions()

        goToMapAct.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.eRange, ParameterFragment.newInstance(), "ParameterFragment").commit()


            //supportFragmentManager
            //      .beginTransaction()
            //    .add(R.id.eRange, MapFragment.newInstance(), "MapFragment")
            //  .commit()


            Log.d("fragments", "" + supportFragmentManager.fragments[0].tag)


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
                    supportFragmentManager.beginTransaction().replace(R.id.eRange, MapFragment.newInstance(), "MapFragment").commit()
                }
            }
        }
    }

    private fun checkPermissions() {
        val permissionCoarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (permissionCoarse != PackageManager.PERMISSION_GRANTED) {
            Log.d("permissions", "Permission to coarse locations denied")
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    101)
        }
    }


}
