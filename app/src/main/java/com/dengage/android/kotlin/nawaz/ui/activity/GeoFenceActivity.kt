package com.dengage.android.kotlin.nawaz.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.dengage.android.kotlin.nawaz.R
import com.dengage.android.kotlin.nawaz.ui.base.BaseActivity
import com.dengage.sdk.DengageManager

class GeoFenceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geo_fence)

        val btnRequestLocationPermission = findViewById<Button>(R.id.btnRequestLocationPermission)
        btnRequestLocationPermission.setOnClickListener{
            requestPermission()
        }

        val btnStopGeofencing = findViewById<Button>(R.id.btnStopGeofencing)
        btnStopGeofencing.setOnClickListener{
            StopGeoFence()
        }
    }

    fun requestPermission()
    {
        DengageManager.getInstance(this).requestLocationPermissions(this)
    }

    fun StopGeoFence()
    {
        DengageManager.getInstance(this).stopGeofence()
    }
}