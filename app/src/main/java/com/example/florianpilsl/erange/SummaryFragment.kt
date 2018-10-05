package com.example.florianpilsl.erange

import android.support.v4.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_summary.*
import kotlinx.android.synthetic.main.fragment_summary.view.*

/**
 * A placeholder fragment containing a simple view.
 */
class SummaryFragment : Fragment(), View.OnClickListener {



    companion object {
        var duration: String = ""
        var distance: String = ""
        fun newInstance(): SummaryFragment {

            return SummaryFragment()

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_summary, container,
                false)
        val btn: Button = view.findViewById(R.id.summarybutton)
        btn.setOnClickListener(this)

        return view

    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.summarybutton -> {

            }
        }
    }


}