package com.example.mapbox

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mapbox.databinding.ActivityAdicionarPinBinding
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils


class AdicionarPin : AppCompatActivity(), OnMapReadyCallback, MapboxMap.OnMapClickListener {
    private lateinit var map: MapboxMap
    private val sourceId = "SOURCE_ID"
    private val iconId = "ICON_ID"
    private val layerId = "LAYER_ID"
    private var latLng = LatLng(-5.1670937, 119.4796103)
    private lateinit var binding: ActivityAdicionarPinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        binding = ActivityAdicionarPinBinding
            .inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        binding.ibBack.setOnClickListener { voltarOpcoes() }
       // binding.ibCheck.setOnClickListener { confirmarPin() }
    }
    private fun voltarOpcoes(){
        val navegaParaTelaOpcoes = Intent(this, Opcoes::class.java)
        startActivity(navegaParaTelaOpcoes)
    }
    //private fun confirmarPin(){
//
   // }
    override fun onMapReady(mapboxMap: MapboxMap) {
        map = mapboxMap
        map.setStyle(Style.MAPBOX_STREETS) {}
        map.uiSettings.isCompassEnabled = false
        map.uiSettings.isLogoEnabled = false
        map.uiSettings.isAttributionEnabled = false
        map.addOnMapClickListener(this)

        val position = CameraPosition.Builder().target(latLng).zoom(13.0).tilt(10.0).build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(position))
        val symbolLayers = ArrayList<Feature>()
        symbolLayers.add(Feature.fromGeometry(Point.fromLngLat(latLng.longitude, latLng.latitude)))
        map.setStyle(
            Style.Builder().fromUri(Style.MAPBOX_STREETS)
                .withImage(iconId, BitmapUtils.getBitmapFromDrawable
                    (ContextCompat.getDrawable(applicationContext, R.drawable.pin))!!)
                .withSource(GeoJsonSource(sourceId, FeatureCollection.fromFeatures(symbolLayers)))
                .withLayer(SymbolLayer(layerId, sourceId).withProperties(iconImage(iconId),
                    iconSize(1.0f), iconAllowOverlap(true), iconIgnorePlacement(true)))

        )
    }
    override fun onMapClick(point: LatLng): Boolean {
        val destinationPoint = Point.fromLngLat(point.longitude, point.latitude)
        val source = map.style!!.getSourceAs<GeoJsonSource>("destination-source-id")
        source?.setGeoJson(Feature.fromGeometry(destinationPoint))
        //Toast.makeText(this@AdicionarPin, "LongClick1:chega aqui,mas nao marca o pin", Toast.LENGTH_SHORT).show()
        return true
    }
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }
    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }
    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}


