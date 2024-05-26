package com.hecklerk.smarthousefa.ui

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hecklerk.smarthousefa.YaAuthApplication
import com.hecklerk.smarthousefa.data.IotInfoUser
import com.hecklerk.smarthousefa.data.YaIotRepository
import kotlinx.coroutines.launch

class YaIotViewModel(private val iotRepository: YaIotRepository) : ViewModel() {
    private val _iotInfoUser = MutableLiveData<IotInfoUser>()
    val userInfo: LiveData<IotInfoUser>
        get() = _iotInfoUser

    fun getIotInformation(authToken: String) {
        viewModelScope.launch {
            try {
                _iotInfoUser.value = iotRepository.getUserInfo(authToken)
            } catch (e: Exception) {
                Log.d("GetIotInfo", "Caught an exception when trying to get information about user: ${e.message}")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as YaAuthApplication)
                val repository = application.container.iotRepository
                YaIotViewModel(repository)
            }
        }
    }
}