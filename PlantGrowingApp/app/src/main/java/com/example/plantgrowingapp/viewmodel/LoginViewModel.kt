package com.example.plantgrowingapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.plantgrowingapp.R
import com.example.plantgrowingapp.constant.Authenticated
import com.example.plantgrowingapp.constant.InvalidAuthentication
import com.example.plantgrowingapp.constant.LoginAuthenticationStates
import com.example.plantgrowingapp.constant.Unauthenticated
import com.example.plantgrowingapp.local.database.PlantGrowingDatabase
import com.example.plantgrowingapp.local.domain.UserDomain
import com.example.plantgrowingapp.repository.UserRepository
import kotlinx.coroutines.*
import java.net.ConnectException


class LoginViewModel(
    application: Application
) : ViewModel() {

    val animationResourceView = R.anim.fade_in
    val animationResourceButton = R.anim.bounce

    var emailValue = MutableLiveData<String>()
    var passwordValue = MutableLiveData<String>()
    var errorPassword = MutableLiveData<Boolean>()
    var errorEmail = MutableLiveData<Boolean>()

    private val _userLogged = MediatorLiveData<UserDomain>()
    val userLogged: LiveData<UserDomain>
        get() = _userLogged

    // The internal MutableLiveData that stores the status of the login
    private val _loginStatus = MutableLiveData<LoginAuthenticationStates>()

    // The external immutable LiveData for the login status
    val loginStatus: LiveData<LoginAuthenticationStates>
        get() = _loginStatus

    private val _navigateToSignUpFragment = MutableLiveData<Boolean>()
    val navigateToSignUpFragment: LiveData<Boolean>
        get() = _navigateToSignUpFragment

    private val database = PlantGrowingDatabase.getInstance(application)
    private val userRepository = UserRepository(database)
    private var viewModelJob = Job()
    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this scope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        emailValue.value = "a@a.com"
        passwordValue.value = "a"
    }

    fun onSignUpClick() {
        errorEmail.value = emailValue.value.isNullOrEmpty()
        errorPassword.value = passwordValue.value.isNullOrEmpty()
        if (errorEmail.value == false && errorPassword.value == false) {
            doLogin()
        }
    }

    private fun doLogin() = uiScope.launch {
        val userToSave = UserDomain().apply {
            userEmail = emailValue.value.toString()
            userPassword = passwordValue.value.toString()
        }
        try {
            _userLogged.value = userRepository.getNetworkUser(user = userToSave)
            if (_userLogged.value != null) {
                _loginStatus.value = Authenticated()
            } else {
                _loginStatus.value = Unauthenticated()
            }
        } catch (e: ConnectException) {
            e.printStackTrace()
            _loginStatus.value = InvalidAuthentication()
        }
    }

    fun doneNavigationSignUp() {
        _navigateToSignUpFragment.value = null
    }

    fun moveToSignUp() {
        _navigateToSignUpFragment.value = true
    }

    fun doneNavigationHome() {
        _userLogged.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Factory for constructing LoginViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}