package com.example.plantgrowingapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.plantgrowingapp.R
import com.example.plantgrowingapp.local.domain.PlantDomain
import com.example.plantgrowingapp.viewmodel.ChartViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import androidx.lifecycle.Observer

class ChartFragment : Fragment() {

    lateinit var chart: LineChart
    lateinit var plant: PlantDomain
    private val chartViewModel: ChartViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProviders.of(this, ChartViewModel.Factory(activity.application, plant)).get(ChartViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chart, container, false)
        plant = ChartFragmentArgs.fromBundle(arguments!!).plant
        chart = view.findViewById(R.id.data_chart)
        //TODO refactor chart and tie up with vm
        buildChart()

        chartViewModel.infoData.observe(this.viewLifecycleOwner, Observer {
            if (it.size != 0) {
                //TODO set data with x time and y values
            }
        })
        setData(10, 50F);
        return view.rootView
    }

    private fun buildChart() {
        chart.animateX(1500)
        // no description text
        // no description text
        chart.description.isEnabled = false

        // enable touch gestures
        // enable touch gestures
        chart.setTouchEnabled(true)

        chart.dragDecelerationFrictionCoef = 0.9f

        // enable scaling and dragging
        // enable scaling and dragging
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setDrawGridBackground(false)
        chart.isHighlightPerDragEnabled = true

        // set an alternative background color
        // set an alternative background color
        chart.setBackgroundColor(Color.WHITE)
        chart.setViewPortOffsets(0f, 0f, 0f, 0f)

        // get the legend (only possible after setting data)
        // get the legend (only possible after setting data)
        val l = chart.legend
        l.isEnabled = false

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.TOP_INSIDE
        xAxis.textSize = 10f
        xAxis.textColor = Color.WHITE
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(true)
        xAxis.textColor = Color.rgb(255, 192, 56)
        xAxis.setCenterAxisLabels(true)
        xAxis.granularity = 1f // one hour

        /**
         * example object
         * They are used if you need to create an object of a slight modification of some class or interface without declaring a subclass for it. For example ,
         *
         * open class Person() {
                fun eat() = println("Eating food.")

                fun talk() = println("Talking with people.")

                open fun pray() = println("Praying god.")
                }

                fun main(args: Array<String>) {
                    val atheist = object : Person() {
                    override fun pray() = println("I don't pray. I am an atheist.")
                    }

                    atheist.eat()
                    atheist.talk()
                    atheist.pray()
                }
         */
        xAxis.valueFormatter = object : ValueFormatter() {
            private val mFormat: SimpleDateFormat = SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH)
            override fun getFormattedValue(value: Float): String {
                val millis: Long = TimeUnit.HOURS.toMillis(value.toLong())
                return mFormat.format(Date(millis))
            }
        }

        val leftAxis = chart.axisLeft
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        leftAxis.textColor = ColorTemplate.getHoloBlue()
        leftAxis.setDrawGridLines(true)
        leftAxis.isGranularityEnabled = true
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = 170f
        leftAxis.yOffset = -9f
        leftAxis.textColor = Color.rgb(255, 192, 56)

        val rightAxis = chart.axisRight
        rightAxis.isEnabled = false
    }

    private fun setData(count: Int, range: Float) { // now in hours
        val now =
            TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis())
        val values: ArrayList<Entry> = ArrayList()
        // count = hours
        val to = now + count.toFloat()
        // increment by 1 hour
        var x = now.toFloat()
        while (x < to) {
            val y: Float = getRandom(range, 50F)
            values.add(Entry(x, y)) // add one entry per hour
            x++
        }
        // create a dataset and give it a type
        val set1 = LineDataSet(values, "DataSet 1")
        set1.axisDependency = AxisDependency.LEFT
        set1.color = ColorTemplate.getHoloBlue()
        set1.valueTextColor = ColorTemplate.getHoloBlue()
        set1.lineWidth = 1.5f
        set1.setDrawCircles(false)
        set1.setDrawValues(false)
        set1.fillAlpha = 65
        set1.fillColor = ColorTemplate.getHoloBlue()
        set1.highLightColor = Color.rgb(244, 117, 117)
        set1.setDrawCircleHole(false)
        // create a data object with the data sets
        val data = LineData(set1)
        data.setValueTextColor(Color.WHITE)
        data.setValueTextSize(9f)
        // set data
        chart.data = data
    }

    private fun getRandom(range: Float, start: Float): Float {
        return (Math.random() * range).toFloat() + start
    }
}