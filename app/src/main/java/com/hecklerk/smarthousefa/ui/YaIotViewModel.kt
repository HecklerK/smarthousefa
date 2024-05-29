package com.hecklerk.smarthousefa.ui

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hecklerk.smarthousefa.YaAuthApplication
import com.hecklerk.smarthousefa.data.IotInfoUser
import com.hecklerk.smarthousefa.data.SetStateDeviceRequest
import com.hecklerk.smarthousefa.data.SetStateDeviceResponse
import com.hecklerk.smarthousefa.data.YaIotRepository
import kotlinx.coroutines.launch

class YaIotViewModel(private val iotRepository: YaIotRepository) : ViewModel() {
    private val _iotInfoUser = MutableLiveData<IotInfoUser>()
    val userInfo: LiveData<IotInfoUser>
        get() = _iotInfoUser

    private val _deviceSetState = MutableLiveData<SetStateDeviceResponse>()

    val deviceSetState: LiveData<SetStateDeviceResponse>
        get() = _deviceSetState

    fun getIotInformation(authToken: String) {
        viewModelScope.launch {
            try {
                _iotInfoUser.value = iotRepository.getUserInfo(authToken)
            } catch (e: Exception) {
                Log.d("GetIotInfo", "Caught an exception when trying to get information about user: ${e.message}")
            }
        }
    }

    fun setDeviceState(authToken: String, body: SetStateDeviceRequest) {
        viewModelScope.launch {
            try {
                _deviceSetState.value = iotRepository.setDeviceState(authToken, body)
            } catch (e: Exception) {
                Log.d("SetStateDevice", "Caught an exception when trying to set device state: ${e.message}")
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