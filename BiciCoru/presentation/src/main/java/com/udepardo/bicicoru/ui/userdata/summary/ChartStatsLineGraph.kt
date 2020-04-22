package com.udepardo.bicicoru.ui.userdata.summary

import android.content.Context
import android.util.AttributeSet
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.udepardo.bicicoru.R
import com.udepardo.bicicoru.extensions.color
import com.udepardo.bicicoru.extensions.drawable
import com.udepardo.bicicoru.data.model.db.GraphCoord


class ChartStatsLineGraph @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LineChart(context, attrs, defStyleAttr) {


    fun setData(userStats : List<GraphCoord>){
        val entries = mutableListOf<Entry>()

        userStats.forEachIndexed { index, graphCoord ->
            entries.add(Entry(index.toFloat(), graphCoord.y.toFloat()))
        }

        val formatter = IAxisValueFormatter{ value: Float, _: AxisBase? ->
            userStats[value.toInt()].x.substring(0,3)
        }

        val valueFormatter = IAxisValueFormatter{value, _ ->
            value.toInt().toString()
        }

        with (xAxis){
            granularity = 1f
            setValueFormatter(formatter)
        }

        val dataSet = LineDataSet(entries, "").apply {
            color = resources.color(R.color.colorPrimary) // Line color
            lineWidth = 1f // Width of the graphic line
            setDrawFilled(true)
            fillDrawable = resources.drawable(R.drawable.travel_graph_gradient)
            setDrawHighlightIndicators(false) // Draw + above each value
            isHighlightEnabled = true
            enableDashedHighlightLine(20f, 20f, 10f) // Highlight indicator dashed
            setDrawHorizontalHighlightIndicator(false) // Hide horizontal indicator
            setDrawVerticalHighlightIndicator(false)
            highlightLineWidth = 2f
            highLightColor = resources.color(R.color.colorPrimary)
            setDrawValues(false)
            setDrawCircles(false)
            setCircleColor(resources.color(R.color.colorPrimary))
            circleRadius = 2f
            setDrawCircleHole(false)
            circleHoleRadius = 5f
            axisDependency = YAxis.AxisDependency.LEFT
        }

        val lineData = LineData(dataSet)

        axisLeft.apply {
            axisMinimum = 0f
            setDrawZeroLine(true)
            zeroLineColor = resources.color(R.color.light_grey)
            zeroLineWidth = 1f
        }


            extraTopOffset = 30f
            legend.isEnabled = false
            data = lineData
            animateX(500)
            animateY(250)
            axisLeft.isEnabled = true
            axisLeft.valueFormatter = valueFormatter
            axisLeft.granularity = 1f
            axisLeft.textSize = 8f
            axisLeft.setDrawAxisLine(false)
            axisLeft.setDrawGridLines(false)
            axisRight.isEnabled = false
            axisRight.setDrawAxisLine(false)
            axisRight.setDrawGridLines(false)
            xAxis.setDrawGridLines(true)
            xAxis.gridColor = resources.color(com.udepardo.bicicoru.R.color.light_grey)
            xAxis.labelCount = 8
            xAxis.textSize = 8f
            xAxis.labelRotationAngle = 45f
            xAxis.isEnabled = true
            isScaleYEnabled = false
            isScaleXEnabled = true
            description = com.github.mikephil.charting.components.Description().apply { text = "" }

    invalidate()
    }
}
