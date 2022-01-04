package com.example.mapbox

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mapbox.databinding.ActivityVisualizarPinsBinding
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
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils

class VisualizarPins : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: MapboxMap
    private val sourceId = "SOURCE_ID"
    private val iconId = "ICON_ID"
    private val layerId = "LAYER_ID"
    private var latLng = LatLng( -5.555559, 119.823655)
    private lateinit var binding: ActivityVisualizarPinsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        binding = ActivityVisualizarPinsBinding
            .inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        binding.ibVoltar.setOnClickListener { voltarOpcoes() }
    }
    private fun voltarOpcoes(){
        val navegaParaTelaOpcoes = Intent(this, Opcoes::class.java)
        startActivity(navegaParaTelaOpcoes)
    }
    override fun onMapReady(mapboxMap: MapboxMap) {
        map = mapboxMap
        map.setStyle(Style.MAPBOX_STREETS) {}
        map.uiSettings.isCompassEnabled = false
        map.uiSettings.isLogoEnabled = false
        map.uiSettings.isAttributionEnabled = false

        val position = CameraPosition.Builder().target(latLng).zoom(10.0).tilt(12.0).build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(position))
        val symbolLayers = ArrayList<Feature>()
        symbolLayers.add(Feature.fromGeometry(Point.fromLngLat(119.4796103,  -5.1670937)))
        symbolLayers.add(Feature.fromGeometry(Point.fromLngLat( 119.823655,  -5.555559)))
        symbolLayers.add(Feature.fromGeometry(Point.fromLngLat( 119.731021,  -5.568312)))
        symbolLayers.add(Feature.fromGeometry(Point.fromLngLat( 119.673094,  -5.554579)))
        map.setStyle(
            Style.Builder().fromUri(Style.MAPBOX_STREETS)
                .withImage(iconId, BitmapUtils.getBitmapFromDrawable
                    (ContextCompat.getDrawable(applicationContext, R.drawable.group))!!)
                .withSource(GeoJsonSource(sourceId, FeatureCollection.fromFeatures(symbolLayers)))
                .withLayer(SymbolLayer(layerId, sourceId).withProperties(
                    PropertyFactory.iconImage(iconId),
                    PropertyFactory.iconSize(1.0f),
                    PropertyFactory.iconAllowOverlap(true),
                    PropertyFactory.iconIgnorePlacement(true)
                ))
        )
    }
}