package com.example.plantgrowingapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.plantgrowingapp.R
import com.example.plantgrowingapp.local.domain.DataCollectionDomain
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
            if (it.isNotEmpty()) {
                setData(it);
            }
        })
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
        xAxis.textColor = ContextCompat.getColor(activity!!.applicationContext, R.color.colorPrimary)
        xAxis.setCenterAxisLabels(true)
        xAxis.granularity = 1f // one hour


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
        leftAxis.axisMaximum = 100F
        leftAxis.yOffset = -9f
        leftAxis.textColor = ContextCompat.getColor(activity!!.applicationContext, R.color.colorPrimary)

        val rightAxis = chart.axisRight
        rightAxis.isEnabled = false
    }

    private fun setData(dataCollection: List<DataCollectionDomain?>) { // now in hours
        val temperature: ArrayList<Entry> = ArrayList()
        val humidity: ArrayList<Entry> = ArrayList()
        for (data in dataCollection) {
            val y: Float = data!!.dataCollectionTemperature.toFloat()
            val hum: Float = data.dataCollectionHumidity.toFloat()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val timeCollection = dateFormat.parse(data!!.dataCollectionExecTime)
            val temp: Long = timeCollection.time / (1000*60*60)
            temperature.add(Entry(temp.toFloat(), y)) // add one entry per hour
            humidity.add(Entry(temp.toFloat(), hum)) // add one entry per hour for humidity
        }

        // create a data object with the data sets
        val chartData = LineData();
        chartData.setValueTextColor(Color.WHITE)
        chartData.setValueTextSize(9f)

        setTempData(temperature, chartData)

        setHumData(humidity, chartData)
    }

    private fun setTempData(
        temperature: ArrayList<Entry>,
        chartData: LineData
    ) {
        // create a dataset temp and give it a type
        val setTemp = LineDataSet(temperature, "DataSet temp")
        setTemp.axisDependency = AxisDependency.LEFT
        setTemp.color = ColorTemplate.getHoloBlue()
        setTemp.valueTextColor = ColorTemplate.getHoloBlue()
        setTemp.lineWidth = 1.5f
        setTemp.setDrawCircles(false)
        setTemp.setDrawValues(false)
        setTemp.fillAlpha = 65
        setTemp.fillColor = ColorTemplate.getHoloBlue()
        setTemp.highLightColor = Color.rgb(244, 117, 117)
        setTemp.setDrawCircleHole(false)
        chartData.addDataSet(setTemp);
    }

    private fun setHumData(
        humidity: ArrayList<Entry>,
        chartData: LineData
    ) {
        // create a dataset temp and give it a type
        val setHumidity = LineDataSet(humidity, "DataSet hum")
        setHumidity.axisDependency = AxisDependency.LEFT
        setHumidity.color = Color.rgb(0, 50, 50)
        setHumidity.valueTextColor = ColorTemplate.getHoloBlue()
        setHumidity.lineWidth = 1.5f
        setHumidity.setDrawCircles(false)
        setHumidity.setDrawValues(false)
        setHumidity.fillAlpha = 65
        setHumidity.fillColor = Color.rgb(0, 50, 50)
        setHumidity.highLightColor = Color.rgb(0, 50, 50)
        setHumidity.setDrawCircleHole(false)
        // create a data object with the data sets
        chartData.addDataSet(setHumidity);

        // set data
        chart.data = chartData
        chart.animateXY(2000, 2000)
    }
}

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