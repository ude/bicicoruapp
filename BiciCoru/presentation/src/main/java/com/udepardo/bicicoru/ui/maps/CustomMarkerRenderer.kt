package com.udepardo.bicicoru.ui.maps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.udepardo.bicicoru.R
import com.udepardo.bicicoru.bicicorucommon.OtterGraph
import com.udepardo.bicicoru.data.model.StationViewModel

class CustomMarkerRenderer(context: Context?, map: GoogleMap?, clusterManager: ClusterManager<StationViewModel>?) :
    DefaultClusterRenderer<StationViewModel>(context, map, clusterManager) {

    private val inflater = (context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
    private val markerView = (inflater).inflate(R.layout.custom_map_marker, null) as OtterGraph

    init {
        markerView.apply {
            setSlots(100, 100, true)
            measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            layout(0, 0, markerView.measuredWidth, markerView.measuredHeight)
        }

    }

    override fun onBeforeClusterItemRendered(item: StationViewModel?, markerOptions: MarkerOptions?) {
        super.onBeforeClusterItemRendered(item, markerOptions)
        markerOptions?.icon(createMarkerBitmap(item?.bikes?:0, item?.emptySlots?:0, item?.isOnline?:false))
    }

    override fun shouldRenderAsCluster(cluster: Cluster<StationViewModel>?)  = cluster?.size ?: 0 > 1

    override fun onBeforeClusterRendered(cluster: Cluster<StationViewModel>?, markerOptions: MarkerOptions?) {
        super.onBeforeClusterRendered(cluster, markerOptions)
        var free = 0
        var bikes = 0
        var isOnline = false

        cluster?.items?.forEach {
            free += it.emptySlots
            bikes += it.bikes
            isOnline = isOnline || it.isOnline
        }

        markerOptions?.icon(createMarkerBitmap(bikes, free, isOnline))
    }

    private fun createMarkerBitmap(free: Int, bikes: Int, isOnline: Boolean): BitmapDescriptor {
        with(markerView) {
            setSlots(free, free + bikes, isOnline)
            measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            layout(0, 0, markerView.measuredWidth, markerView.measuredHeight)

            val returnedBitmap = Bitmap.createBitmap(
                measuredWidth, measuredHeight,
                Bitmap.Config.ARGB_8888
            )

            val canvas = Canvas(returnedBitmap)

            canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
            markerView.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(returnedBitmap)
        }
    }
}