package com.example.plantgrowingapp.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.plantgrowingapp.constant.ApiCallStatus
import com.example.plantgrowingapp.constant.Error

@BindingAdapter("visibleIfError")
fun ImageView.bindStatus(status: ApiCallStatus) {
    when(status) {
        is Error -> {
            visibility = View.VISIBLE
        }
        else -> {
            visibility = View.GONE
        }
    }
}