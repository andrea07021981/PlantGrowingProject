package com.example.plantgrowingapp.utils

import android.view.View
import androidx.databinding.BindingAdapter
import com.example.plantgrowingapp.constant.ApiCallStatus
import com.example.plantgrowingapp.constant.Error

@BindingAdapter("visibileIfError")
fun visibileIfError(view: View, status: ApiCallStatus) {
    when(status) {
        is Error -> {
            view.visibility = View.VISIBLE
        }
        else -> {
            view.visibility = View.GONE
        }
    }
}