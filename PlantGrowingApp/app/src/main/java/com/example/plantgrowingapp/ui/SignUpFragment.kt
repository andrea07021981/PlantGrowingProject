package com.example.planntgrowingapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.plantgrowingapp.databinding.FragmentSignupBinding
import com.example.plantgrowingapp.viewmodel.SignUpViewModel

class SignUpFragment : Fragment() {

    private val signUpViewModel: SignUpViewModel by lazy {
        ViewModelProviders.of(this).get(SignUpViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = FragmentSignupBinding.inflate(inflater)
        dataBinding.signupViewModel = signUpViewModel
        dataBinding.lifecycleOwner = this

        signUpViewModel.navigateToLoginFragment.observe(this.viewLifecycleOwner, Observer {
            if (it == true) {
                this
                    .findNavController()
                    .popBackStack()
                signUpViewModel.doneNavigating()
            }
        })
        return dataBinding.root
    }
}