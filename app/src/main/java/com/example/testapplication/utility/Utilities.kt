package com.example.testapplication.utility

import android.graphics.Color
import android.view.View
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.text.SimpleDateFormat
import java.util.*

fun initLineChart(chart: LineChart) {
//    tfLight = Typeface.createFromAsset(context.assets, "OpenSans-Light.ttf")
//    tfRegular = Typeface.createFromAsset(context.assets, "OpenSans-Regular.ttf")

    val xAxis = chart.xAxis
    val yAxis = chart.axisLeft

    chart.axisRight.isEnabled = false
    chart.description.isEnabled = false
    chart.legend.isEnabled = false
    chart.setBackgroundColor(Color.TRANSPARENT)
    chart.setDrawGridBackground(false)
    chart.setTouchEnabled(false)

    xAxis.gridColor = Color.GRAY
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    xAxis.setDrawAxisLine(false)
    xAxis.setLabelCount(2, false)
    xAxis.textColor = Color.WHITE
//    xAxis.typeface = tfLight

    yAxis.gridColor = Color.GRAY
    yAxis.setDrawAxisLine(false)
    yAxis.setLabelCount(2, false)
    yAxis.textColor = Color.WHITE
//    yAxis.typeface = tfLight

    chart.invalidate()
}


fun updateLineChart(
    chart: LineChart,
    entryList: List<Entry>,
    label: String,
    locale: Locale,
    referenceTimestamp: Long = 0,
    xAxisValueFormatter: ValueFormatter = TimestampValueFormatter(
        referenceTimestamp,
        locale
    ),
    axisLeftValueFormatter: ValueFormatter = SomeValueFormatter()
) {
    if (entryList.isEmpty()) {
        return
    }

    val lds = LineDataSet(entryList, label)
    lds.color = Color.WHITE
    lds.color = ColorTemplate.VORDIPLOM_COLORS[0]
    lds.fillAlpha = 100
    lds.fillColor = Color.WHITE
    lds.lineWidth = 1.8f
    lds.mode = LineDataSet.Mode.STEPPED
    lds.setDrawCircles(false)
    lds.setDrawFilled(true)
    lds.setDrawHorizontalHighlightIndicator(false)

    val sets: ArrayList<ILineDataSet> = ArrayList()
    sets.add(lds)

    val ld = LineData(sets)
    ld.setDrawValues(false)

    chart.xAxis.valueFormatter = xAxisValueFormatter
    chart.axisLeft.valueFormatter = axisLeftValueFormatter
    chart.data = ld
    chart.invalidate()
}

fun View.clicks(): Flow<Unit> = callbackFlow {
    setOnClickListener {
        offer(Unit)
    }
    awaitClose { setOnClickListener(null) }
}


class TimestampValueFormatter(private val referenceTimestamp: Long, locale: Locale) :
    ValueFormatter() {
    private val dateFormatter =
        SimpleDateFormat("dd.MM.yy", locale)


    override fun getFormattedValue(value: Float): String {
        // "toInt()" required to workaround inaccurate results due to unchangeable float usage
        val recalculatedValue = value.toInt() + referenceTimestamp
        return dateFormatter.format(recalculatedValue)
    }
}

class SomeValueFormatter() : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return value.toString()
    }
}