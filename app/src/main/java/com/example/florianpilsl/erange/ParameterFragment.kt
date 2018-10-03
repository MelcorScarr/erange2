package com.example.florianpilsl.erange

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView

/**
 * A placeholder fragment containing a simple view.
 */
class ParameterFragment : Fragment() {
    lateinit var autoCompleteTextViewStart: AutoCompleteTextView
    lateinit var autoCompleteTextViewEnd: AutoCompleteTextView
    lateinit var placeAutoCompleteAdapter: PlaceAutoCompleteAdapter
    companion object {
        fun newInstance(adapter: PlaceAutoCompleteAdapter): ParameterFragment {
            return ParameterFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_parameter, container, false)

        autoCompleteTextViewStart = rootView.findViewById(R.id.EditTextStart)
        autoCompleteTextViewEnd = rootView.findViewById(R.id.EditTextEnd)
        Log.d("Fragments", placeAutoCompleteAdapter.toString())
        this.autoCompleteTextViewStart.setAdapter(placeAutoCompleteAdapter)
        this.autoCompleteTextViewEnd.setAdapter(placeAutoCompleteAdapter)
        return inflater.inflate(R.layout.fragment_parameter, container, false)
    }
}