package com.raulfm.netherportal.ui.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.raulfm.netherportal.R

class ServerControlFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_waypoints, container, false)

    companion object {
        fun newInstance(): ServerControlFragment = ServerControlFragment()
    }
}