package com.example.planntgrowingapp.ui

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.plantgrowingapp.R
import com.example.plantgrowingapp.databinding.FragmentLoginBinding
import com.example.plantgrowingapp.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private val TAG = LoginFragment::class.java.name

    private val loginViewModel: LoginViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProviders.of(this, LoginViewModel.Factory(app = requireNotNull(activity).application)).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.custommove)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = FragmentLoginBinding.inflate(inflater)
        dataBinding.loginViewModel = loginViewModel
        dataBinding.lifecycleOwner = this

        loginViewModel.navigateToSignUpFragment.observe(this.viewLifecycleOwner, Observer {
            if (it == true) {
                this
                    .findNavController()
                    .navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
                loginViewModel.doneNavigationSignUp()
            }
        })

        loginViewModel.userLogged.observe(this.viewLifecycleOwner, Observer {
            if (it != null) {
                Log.d(TAG, "User logged with ${it.userEmail} and ${it.userPassword} ")
                this
                    .findNavController()
                    .navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment(it))
                loginViewModel.doneNavigationHome()
            }

        })
        return dataBinding.root
    }
}