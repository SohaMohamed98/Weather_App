package com.soha.weather_app.weather.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.soha.weather_app.R
import com.soha.weather_app.weather.db.Repository
import com.soha.weather_app.weather.fragments.current.HomeWeather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsFragment : Fragment(R.layout.fragment_maps) {

    private lateinit var mMap: GoogleMap
    private val LOCATION_REQUEST_CODE = 101

    var rep = Repository()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap

        /*   // Add a marker in Sydney and move the camera
           val sydney = LatLng(-34.0, 151.0)
           mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
           mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/

        if (mMap != null) {
            val permission = context?.let {
                ContextCompat.checkSelfPermission(it,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            }

            if (permission == PackageManager.PERMISSION_GRANTED) {
                mMap?.isMyLocationEnabled = true
            } else {
                requestPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    LOCATION_REQUEST_CODE)
            }
        }

        val mapSettings = mMap?.uiSettings
        //zoom controllers
        mapSettings?.isZoomControlsEnabled=true

        mapSettings?.isZoomGesturesEnabled = true

        mMap.setOnMapClickListener { latLon->
            mMap.clear()
          //  mMap.animateCamera(CameraUpdateFactory.newLatLng(lat))
            var getcoordinates =LatLng(latLon.latitude, latLon.longitude)

            CoroutineScope(Dispatchers.IO).launch {
                moveToCurrent(lat = latLon.latitude, lon = latLon.longitude)

            }

            Toast.makeText(context,"${getcoordinates.longitude}", Toast.LENGTH_LONG).show()
            val markerOption= MarkerOptions().position(getcoordinates)
           // val  title=getAddress(getcoordinates)
            //markerOption.title(title)

            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(getcoordinates, 4f))
            mMap.addMarker(MarkerOptions()
                .position(getcoordinates)
                .title(getcoordinates.toString())
                .snippet("LatLon"))


        }

    }

  /*  private fun getAddress(lat: LatLng): String? {
        val geocoder = Geocoder(context)
        val list = geocoder.getFromLocation(lat.latitude, lat.longitude,1)
        return list[0].getAddressLine(0)

    }*/

    suspend fun moveToCurrent(lat:Double, lon:Double){
        rep.retrofitWeatherCall(lat,lon)
    }
    private fun requestPermission(permissionType: String, requestCode: Int) {

        ActivityCompat.requestPermissions(context as Activity, arrayOf(permissionType), requestCode)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            LOCATION_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] !=
                    PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context,
                        "Unable to show location - permission required",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }





}