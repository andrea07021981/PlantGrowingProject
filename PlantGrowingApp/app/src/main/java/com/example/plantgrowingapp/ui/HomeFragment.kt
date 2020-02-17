package com.example.plantgrowingapp.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plantgrowingapp.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import java.util.*

class HomeFragment : Fragment() {

    private val TAG: String = HomeFragment::class.java.name
    private lateinit var chart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        chart = view.findViewById(R.id.chart1)
        chart.setViewPortOffsets(0f, 0f, 0f, 0f)
        chart.setBackgroundColor(Color.rgb(104, 241, 175))

        // no description text
        // no description text
        chart.getDescription().setEnabled(false)

        // enable touch gestures
        // enable touch gestures
        chart.setTouchEnabled(true)

        // enable scaling and dragging
        // enable scaling and dragging
        chart.setDragEnabled(true)
        chart.setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawGridBackground(false)
        chart.setMaxHighlightDistance(300f)

        val x: XAxis = chart.getXAxis()
        x.isEnabled = false

        val y: YAxis = chart.getAxisLeft()
        //y.typeface = Typeface.createFromAsset(requireNotNull(activity).assets, "OpenSans-Light.ttf");
        y.setLabelCount(6, false)
        y.textColor = Color.WHITE
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        y.setDrawGridLines(false)
        y.axisLineColor = Color.WHITE

        chart.getAxisRight().setEnabled(false)

        chart.getLegend().setEnabled(false)

        chart.animateXY(2000, 2000)

        // don't forget to refresh the drawing
        // don't forget to refresh the drawing
        chart.invalidate()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData(20, 100.0F)
    }

    private fun setData(count: Int, range: Float) {
        val values =
            ArrayList<Entry>()
        for (i in 0 until count) {
            val `val` = (Math.random() * (range + 1)).toFloat() + 20
            values.add(Entry(i.toFloat(), `val`))
        }
        val set1: LineDataSet
        if (chart.getData() != null &&
            chart.getData().getDataSetCount() > 0
        ) {
            set1 = chart.getData().getDataSetByIndex(0) as LineDataSet
            set1.values = values
            chart.getData().notifyDataChanged()
            chart.notifyDataSetChanged()
        } else { // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")
            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set1.cubicIntensity = 0.2f
            set1.setDrawFilled(true)
            set1.setDrawCircles(false)
            set1.lineWidth = 1.8f
            set1.circleRadius = 4f
            set1.setCircleColor(Color.WHITE)
            set1.highLightColor = Color.rgb(244, 117, 117)
            set1.color = Color.WHITE
            set1.fillColor = Color.WHITE
            set1.fillAlpha = 100
            set1.setDrawHorizontalHighlightIndicator(false)
            set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider -> chart.getAxisLeft().getAxisMinimum() }
            // create a data object with the data sets
            val data = LineData(set1)
            //data.setValueTypeface(Typeface.createFromAsset(requireNotNull(activity).assets, "OpenSans-Light.ttf"))
            data.setValueTextSize(9f)
            data.setDrawValues(false)
            // set data
            chart.setData(data)
        }
    }
}