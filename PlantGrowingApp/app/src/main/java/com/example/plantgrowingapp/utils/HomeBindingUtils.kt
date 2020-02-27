package com.example.plantgrowingapp.utils

import android.os.Build
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.plantgrowingapp.R
import com.example.plantgrowingapp.component.PlantAdapter
import com.example.plantgrowingapp.local.domain.PlantDomain
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*


/**
 * Binding adapter used to hide the spinner once data is available
 */
@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}

@BindingAdapter("listdata")
fun bindRecycleView(recyclerView: RecyclerView, data: List<PlantDomain>?) {
    val adapter = recyclerView.adapter as PlantAdapter
    adapter.submitList(data)
}

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("waterStatus")
fun ImageButton.waterStatus(lastWatering: String) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val timeLastWatering = dateFormat.parse(lastWatering)
    val timeNow = Calendar.getInstance().time
    val diff: Long = timeNow.time - timeLastWatering.time
    setImageResource(
        when(diff / 60) {
            in 0..12 -> R.mipmap.ic_waterok
            in 12..24 -> R.mipmap.ic_waterwarn
            else -> R.mipmap.ic_waterko
        }
    )
}