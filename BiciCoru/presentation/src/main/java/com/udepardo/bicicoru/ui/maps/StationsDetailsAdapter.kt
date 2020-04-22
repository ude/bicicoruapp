package com.udepardo.bicicoru.ui.maps

import android.content.Context
import android.location.Location
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.udepardo.bicicoru.R
import com.udepardo.bicicoru.extensions.visibleOrGone
import com.udepardo.bicicoru.data.model.StationViewModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.station_detail_layout.*
import java.text.DecimalFormat


/**
 * Created by fernando.ude on 03/04/2018.
 */


class StationsDetailsAdapter(context: Context,
                             val itemListener: (StationViewModel) -> Unit,
                             val navigationListener: (StationViewModel) -> Unit,
                             var hasLocationPermission : Boolean = false) : androidx.recyclerview.widget.RecyclerView.Adapter<StationsDetailsAdapter.StationDetailsViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    var items = mutableListOf<StationViewModel>()
    set(value) {
        items.clear()
        items.addAll(value)
        notifyDataSetChanged()
    }

    private var location: Location? = null


    fun updateModel(updatedItems: List<StationViewModel>) {
        items.clear()
        items.addAll(updatedItems)
        notifyDataSetChanged()
    }

    fun updateModel(newItem: StationViewModel) {
        items.clear()
        items.add(newItem)
        notifyDataSetChanged()
    }

    fun updateLocation(loc: Location) {
        location = loc
        notifyDataSetChanged()

    }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationDetailsViewHolder {
        val view = inflater.inflate(R.layout.station_detail_layout, parent, false)

        return StationDetailsViewHolder(view)
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }
    override fun onBindViewHolder(holder: StationDetailsViewHolder, position: Int) {
        holder.bind(items[position], location)
    }

    override fun getItemCount() = items.size


    inner class StationDetailsViewHolder(private val item: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(item), LayoutContainer {
        override val containerView: View?
            get() = item

        fun bind(model: StationViewModel, currentLocation: Location?) {
            stationGraph.setSlots(model.bikes, model.emptySlots + model.bikes, model.isOnline)
            stationName.text = model.name
            stationBikes.text = "${model.bikes} bicis"
            stationSlots.text = "${model.emptySlots} huecos"

            if (currentLocation == null) {
                stationsDistance.visibility = View.GONE
            } else {
                stationsDistance.visibility = View.VISIBLE
                val locc2 = Location("").apply {
                    latitude = model.pos.latitude
                    longitude = model.pos.longitude
                }

                stationsDistance.text = DecimalFormat("#.#").format(currentLocation.distanceTo(locc2).toInt()) + " m."
            }


            containerView?.setOnClickListener {
                itemListener(model)
            }
            navigateButton.setOnClickListener { navigationListener(model) }

            stationsDistance.visibleOrGone(hasLocationPermission)


        }


    }
}