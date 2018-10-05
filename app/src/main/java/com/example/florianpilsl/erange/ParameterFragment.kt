package com.example.florianpilsl.erange

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import android.widget.AdapterView.OnItemClickListener
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.PlaceBuffer
import kotlinx.android.synthetic.main.fragment_parameter.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import java.lang.StringBuilder
import java.net.URL


/**
 * A placeholder fragment containing a simple view.
 */
class ParameterFragment : Fragment(), GoogleApiClient.OnConnectionFailedListener {
    lateinit var autoCompleteTextViewStart: AutoCompleteTextView
    lateinit var autoCompleteTextViewEnd: AutoCompleteTextView
    lateinit var placeAutoCompleteAdapter: PlaceAutoCompleteAdapter
    val latLangBounds: LatLngBounds = LatLngBounds(LatLng(-40.0, -168.0), LatLng(71.0, 136.0))
    private var mListener: OnParameterInteractionListener? = null
    var start: LatLng = LatLng(0.0, 0.0)
    var end: LatLng = LatLng(0.0, 0.0)
    var distance: String = ""
    var duration: String = ""
    var mGoogleApiClient: GoogleApiClient? = null

    companion object {
        fun newInstance(start: String, end: String): ParameterFragment {
            return ParameterFragment()
        }
    }

    // Function to be called when the connection to the Google API fails.
    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d("GoogleApiClient", "Connection to Google API not successfull. Suggestions will not be available.")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_parameter, container,
                false)
        val btn: Button = view.findViewById(R.id.planroute)
        val spinner: Spinner = view.findViewById(R.id.bikemodels_spinner)
        /*
         * Button Setting
         */
        btn.setOnClickListener {
            if (end != LatLng(0.0, 0.0) && start != LatLng(0.0, 0.0)) {
                mListener!!.onParameterEnd(end)
                mListener!!.onParameterStart(start)
                getDistance(start, end)
            } else {
                Toast.makeText(activity, "Bitte wählen Sie Start- und  Zielort aus.", Toast.LENGTH_LONG).show()
            }
        }


// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                activity,
                R.array.bikemodels,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                // Do nothing (just yet, because this is a Demo)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        /*
         * Auto Complete Handling
         */

        mGoogleApiClient =
                GoogleApiClient.Builder(activity!!.applicationContext)
                        .addApi(Places.GEO_DATA_API)
                        .addApi(Places.PLACE_DETECTION_API)
                        .enableAutoManage(activity!!, this)
                        .build()

        placeAutoCompleteAdapter = PlaceAutoCompleteAdapter(activity, mGoogleApiClient, latLangBounds, null)

        autoCompleteTextViewStart = view.findViewById(R.id.EditTextStart)
        autoCompleteTextViewEnd = view.findViewById(R.id.EditTextEnd)

        this.autoCompleteTextViewStart.setAdapter(placeAutoCompleteAdapter)
        this.autoCompleteTextViewEnd.setAdapter(placeAutoCompleteAdapter)

        autoCompleteTextViewStart.onItemClickListener = OnItemClickListener { adapterView, view, i, length ->
            var item: AutocompletePrediction = placeAutoCompleteAdapter.getItem(i)
            var placeId: String = item.placeId!!

            var placeResult: PendingResult<PlaceBuffer> = Places.GeoDataApi.getPlaceById(mGoogleApiClient!!, placeId)
            placeResult.setResultCallback(mUpdatePlaceDetailsCallbackStart)
        }

        autoCompleteTextViewEnd.onItemClickListener = OnItemClickListener { adapterView, view, i, length ->
            var item: AutocompletePrediction = placeAutoCompleteAdapter.getItem(i)
            var placeId: String = item.placeId!!

            var placeResult: PendingResult<PlaceBuffer> = Places.GeoDataApi.getPlaceById(mGoogleApiClient!!, placeId)
            placeResult.setResultCallback(mUpdatePlaceDetailsCallbackEnd)
        }

        return view
    }

    //  See https://stackoverflow.com/questions/6456090/android-google-map-finding-distance/6456161#6456161
    private fun getDistance(start: LatLng, end: LatLng) {
        doAsync {
            var urlString: StringBuilder = StringBuilder();
            urlString.append("https://maps.googleapis.com/maps/api/directions/json?");
            urlString.append("origin=");//from
            urlString.append(start.toString()
                    .replace("lat/lng:", "")
                    .replace(" ", "")
                    .replace("(", "")
                    .replace(")", ""));
            urlString.append("&destination=");//to
            urlString.append(end.toString()
                    .replace("lat/lng:", "")
                    .replace(" ", "")
                    .replace("(", "")
                    .replace(")", ""));
            urlString.append("&key=AIzaSyD1bXSrBRlgglyp8Bn4ZYjOGWh0EyVyNGQ")
            Log.d("GoogleApiClient", "URL=" + urlString.toString());


            val apiResponse = URL(urlString.toString()).readText()
            Log.d("GoogleClientApi", apiResponse)
            val jsonArray = JSONObject(apiResponse)

            distance = jsonArray
                    .getJSONArray("routes")
                    .getJSONObject(0)
                    .getJSONArray("legs")
                    .getJSONObject(0)
                    .getJSONObject("distance")
                    .getInt("value")
                    .toString()
            duration = jsonArray
                    .getJSONArray("routes")
                    .getJSONObject(0)
                    .getJSONArray("legs")
                    .getJSONObject(0)
                    .getJSONObject("distance")
                    .getInt("value")
                    .toString()
            uiThread { // Do this once the API is questioned and values are determined.
                SummaryFragment.distance = distance
                SummaryFragment.duration = duration
                progressbar.visibility = View.VISIBLE
                progressexplanation0.visibility = View.VISIBLE
                progressexplanation1.visibility = View.VISIBLE
                progressexplanation2.visibility = View.VISIBLE
                var text = (distance.toInt()/1000).toString() + "%"
                progressexplanation0.text = text
                progressbar.progress = distance.toInt()/1000
            }
        }
    }


    var mUpdatePlaceDetailsCallbackStart: ResultCallback<PlaceBuffer> = object : ResultCallback<PlaceBuffer> {
        override fun onResult(p0: PlaceBuffer) {
            if (!p0.getStatus().isSuccess) {
                Log.d("GoogleApiClient", "Could not find place.")
                p0.release()
                return
            }
            var place: Place = p0.get(0)
            start = place.latLng
            Log.d("GoogleApiClient", "Start: " + place.latLng)
            p0.release()
        }
    }

    var mUpdatePlaceDetailsCallbackEnd: ResultCallback<PlaceBuffer> = object : ResultCallback<PlaceBuffer> {
        override fun onResult(p0: PlaceBuffer) {
            if (!p0.getStatus().isSuccess) {
                Log.d("GoogleApiClient", "Could not find place.")
                p0.release()
                return
            }
            var place: Place = p0.get(0)
            end = place.latLng
            Log.d("GoogleApiClient", "End: " + place.latLng)
            p0.release()
        }
    }

    override fun onPause() {
        super.onPause()
        mGoogleApiClient!!.stopAutoManage(activity as FragmentActivity)
        mGoogleApiClient!!.disconnect()
        progressbar.visibility = View.GONE
        progressexplanation0.visibility = View.GONE
        progressexplanation1.visibility = View.GONE
        progressexplanation2.visibility = View.GONE
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnParameterInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    interface OnParameterInteractionListener {
        fun onParameterStart(start: LatLng)
        fun onParameterEnd(end: LatLng)
    }
}