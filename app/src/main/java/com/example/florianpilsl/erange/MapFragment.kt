package com.example.florianpilsl.erange

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.Polyline
import android.location.Geocoder
import android.util.Log
import java.util.*
import com.google.android.gms.maps.model.MarkerOptions
import android.R.attr.apiKey
import com.cs.googlemaproute.DrawRoute


/**
 * A placeholder fragment containing a simple view.
 */
class MapFragment : Fragment() {


    lateinit var mMapView: MapView
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        var start: LatLng = LatLng(0.0, 0.0)
        var end: LatLng = LatLng(0.0, 0.0)
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }


    @SuppressLint("MissingPermission")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)
        mMapView = rootView.findViewById(R.id.mapView)
        mMapView.onCreate(savedInstanceState)


        mMapView.onResume() // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val red = "#ff0000"

        mMapView.getMapAsync { mMap ->
            googleMap = mMap

            if (end != LatLng(0.0, 0.0) && start != LatLng(0.0, 0.0)) {
                DrawRoute.getInstance(Erange(), rootView.context)
                        .setFromLatLong(start.latitude, start.longitude)
                        .setToLatLong(end.latitude, end.longitude)
                        .setGmapAndKey("AIzaSyBdV2PVvDNMlswG59ZwXIKJS1g7D_2PgYE", googleMap).setColorHash(red)
                        .setZoomLevel(15.0f)
                        .setLoader(true)
                        .setLoaderMsg("Route wird gezeichnet...")
                        .run()
            } else {

            }

            /* setFromLatLong	Need two parameters both are double types, Start latitude and Start longitude
             setToLatLong	Need two parameters both are double types, End latitude and End longitude
             setGmapAndKey	Need two parameters MapObject and string Mapkey
                     setZoomLevel	Need one parameter it should be float like 15.0f
             setColorHash	Need one parameter it should be string hash code "#00ff00"
             setLoader	Need one parameter it should be bolean true or false
             setLoaderMsg	Need one parameter it should be string like "Draw line, please wait..."
             https://github.com/asifshaikh86/googlemaproute
             */

            // For showing a move to my location button
            googleMap!!.isMyLocationEnabled = true

            // For dropping a marker at a point on the Map
            // val sydney = LatLng(-34.0, 151.0)
            // googleMap!!.addMarker(MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"))

            // For zooming automatically to the location of the marker
            // val cameraPosition = CameraPosition.Builder().target(sydney).zoom(12f).build()
            // googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))


            // Position the map's camera near Alice Springs in the center of Australia,
            // and set the zoom factor so most of Australia shows on the screen.
        }
        return rootView
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }


}
