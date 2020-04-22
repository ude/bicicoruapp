package com.udepardo.bicicoru.data.repositories.remote.userdata.parsers

import com.udepardo.bicicoru.data.model.db.GraphCoord
import org.json.JSONException
import org.json.JSONObject


class UserStatsParserKt(val html: String) : BaseParserKt(html) {


    @Throws(JSONException::class)
    fun parse(): List<GraphCoord> {
        val coords = mutableListOf<GraphCoord>()
        try {
            val json = JSONObject(html)
            val xAxis = json.getJSONObject("x_axis")
            val xAxisLabelObjet = xAxis.getJSONObject("labels")

            val xAxisValues = xAxisLabelObjet.getJSONArray("labels")

            val elements = json.getJSONArray("elements")
            val yAxisValues = elements.getJSONObject(0).getJSONArray("values")

            if (xAxisValues.length() != yAxisValues.length()) {
                throw JSONException("Parsing correct, but x axis lenght differs from y axis values")
            }

            for (i in 0 until xAxisValues.length()) {
                coords.add(GraphCoord(i, xAxisValues.getString(i), yAxisValues.getInt(i)))
            }

            return coords
        } catch (ex: NullPointerException) {
            throw JSONException("Error parsing: " + ex.localizedMessage)
        }


    }
}