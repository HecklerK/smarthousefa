package com.hecklerk.smarthousefa.ui

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hecklerk.smarthousefa.YaAuthApplication
import com.hecklerk.smarthousefa.data.UserInfo
import com.hecklerk.smarthousefa.data.YaAuthRepository
import kotlinx.coroutines.launch

class YaAuthViewModel(private val authRepository: YaAuthRepository) : ViewModel() {
    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo>
        get() = _userInfo

    fun getUserInformation(authToken: String) {
        viewModelScope.launch {
            try {
                _userInfo.value = authRepository.getUserInfo(authToken)
            } catch (e: Exception) {
                Log.d("Authorization", "Caught an exception when trying to get information about user: ${e.message}")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as YaAuthApplication)
                val repository = application.container.authRepository
                YaAuthViewModel(repository)
            }
        }
    }
}