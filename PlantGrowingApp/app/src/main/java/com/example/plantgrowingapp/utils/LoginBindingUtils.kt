package com.example.plantgrowingapp.utils

import android.content.Context
import android.content.res.Resources
import android.text.Layout
import android.view.ContextMenu
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.databinding.BindingAdapter
import com.example.plantgrowingapp.R
import com.example.plantgrowingapp.constant.Authenticated
import com.example.plantgrowingapp.constant.InvalidAuthentication
import com.example.plantgrowingapp.constant.LoginAuthenticationStates
import com.example.plantgrowingapp.constant.Unauthenticated
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("loadViewAnimation")
fun View.animation(@AnimRes resource: Int) {
    val animation = AnimationUtils.loadAnimation(context, resource)
    startAnimation(animation)
}

@BindingAdapter("hasError")
fun TextInputLayout.hasError(error: Boolean) {
    when (error) {
        true -> setError("Mandatory field")
        else -> setError(null)
    }
}

/**
 * This bindiadapter display the login status using [LoginAuthenticationStates]
 */
@BindingAdapter("loginStatus")
fun bindStatus(context: View, status: LoginAuthenticationStates?) {
    when (status) {
        is Authenticated -> {
            Toast.makeText(context.context, "Logged", Toast.LENGTH_SHORT).show()
        }
        is Unauthenticated -> {
            Toast.makeText(context.context, "Not logged", Toast.LENGTH_SHORT).show()
        }
        is InvalidAuthentication -> {
            Toast.makeText(context.context, "Error connection", Toast.LENGTH_SHORT).show()
        }
    }
}
