package com.example.plantgrowingapp.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.plantgrowingapp.constant.ApiCallStatus
import com.example.plantgrowingapp.constant.Error

@BindingAdapter("networkResponse")
fun networkResponse(view: View, status: ApiCallStatus) {
    view.visibility = when(status) {
        is Error -> {
            View.VISIBLE
        }
        else -> {
            View.GONE
        }
    }
}