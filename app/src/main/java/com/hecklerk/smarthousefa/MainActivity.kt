package com.hecklerk.smarthousefa

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hecklerk.smarthousefa.adapters.DeviceAdapter
import com.hecklerk.smarthousefa.data.Device
import com.hecklerk.smarthousefa.data.SetStateDevice
import com.hecklerk.smarthousefa.data.SetStateDeviceRequest
import com.hecklerk.smarthousefa.databinding.ActivityMainBinding
import com.hecklerk.smarthousefa.ui.YaAuthViewModel
import com.hecklerk.smarthousefa.ui.YaIotViewModel
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    val binding: ActivityMainBinding
        get() = _binding

    private lateinit var loginSdk: YandexAuthSdk
    private lateinit var loginOptionsBuilder: YandexAuthLoginOptions.Builder
    private lateinit var loginIntent: Intent

    private val REQUEST_LOGIN_SDK: Int = 1
    private var token: String = ""

    private val authViewModel: YaAuthViewModel by viewModels(factoryProducer = { YaAuthViewModel.Factory })
    private val iotViewModel: YaIotViewModel by viewModels(factoryProducer = {YaIotViewModel.Factory})

    private lateinit var sP: SharedPreferences

    private lateinit var devicesList: List<Device>
    private lateinit var adapter: DeviceAdapter
    private var selectDevice: Device? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setLoginButton()
        hideInfoFields()
        devicesList = emptyList()

        sP = getPreferences(Context.MODE_PRIVATE)
        token = sP.getString("token", "").toString()

        adapter = DeviceAdapter(devicesList)
        _binding.devicesList.layoutManager = LinearLayoutManager(_binding.root.context)
        _binding.devicesList.adapter = adapter

        authViewModel.userInfo.observe(this@MainActivity) {
            _binding.loginButton.text = getString(R.string.logout)
            showInfoFields()

            it.let {
                _binding.fullNameTextView.text = it.realName
            }
        }

        iotViewModel.userInfo.observe(this@MainActivity) {
            it.let {
                devicesList = it.devices
                adapter.setData(devicesList)
                adapter.notifyDataSetChanged()
            }
        }

        iotViewModel.deviceSetState.observe(this@MainActivity) {
            it.let {
                if (it.payload.last().capabilities.last().state.actionResult.status == "DONE" && selectDevice != null){
                    iotViewModel.getIotInformation(token)
                }
            }
        }

        adapter.setOnClickListener(object : DeviceAdapter.OnClickListener{
            override fun onClick(position: Int, model: Device) {
                if (model != null){
                    selectDevice = model
                    iotViewModel.getIotInformation(token)
                }
            }
        })

        if (token.isEmpty())
            setLoginButton()
        else {
            authViewModel.getUserInformation(token)
            iotViewModel.getIotInformation(token)
        }
    }

    private fun hideInfoFields() {
        _binding.fullNameTextView.visibility = View.GONE
    }

    private fun showInfoFields() {
        _binding.fullNameTextView.visibility = View.VISIBLE
    }

    private fun setStateButton() {
        if (selectDevice != null){
            _binding.stateButton.visibility = View.VISIBLE
            var text = ""
            if (selectDevice!!.capabilities.last { it.state.instance == "on" }.state.value == "true")
                text = getString(R.string.state_off)
            else
                text = getString(R.string.state_on)

            _binding.stateButton.text = text
            _binding.stateButton.setOnClickListener {
                var cap = selectDevice!!.capabilities.last { it.state.instance == "on" }
                if (selectDevice!!.capabilities.last { it.state.instance == "on" }.state.value == "true")
                    cap!!.state.value = "false"
                else
                    cap!!.state.value = "true"
                var listCap = listOf(cap)
                var listDevice = listOf(SetStateDevice(selectDevice!!.id, listCap))
                val device = SetStateDeviceRequest(listDevice)
                iotViewModel.setDeviceState(token, device)
            }
        }
    }

    private fun setLoginButton() {
        _binding.loginButton.setOnClickListener {
            if (token.isEmpty()) {
                loginSdk = (application as YaAuthApplication).container.loginSdk
                loginOptionsBuilder = YandexAuthLoginOptions.Builder()
                loginIntent = loginSdk.createLoginIntent(loginOptionsBuilder.build())

                startActivityForResult(loginIntent, REQUEST_LOGIN_SDK)
            }
            else
            {
                token = ""
                with (sP.edit()) {
                    putString("token", "")
                    apply()
                }
                hideInfoFields()
                _binding.loginButton.text = getString(R.string.login)
                devicesList = emptyList()
                adapter.setData(devicesList)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_LOGIN_SDK) {
            try {
                val token: YandexAuthToken? = loginSdk.extractToken(resultCode, data)
                if (token != null) {
                    this.token = token.value
                    with (sP.edit()) {
                        putString("token", token.value)
                        apply()
                    }
                    authViewModel.getUserInformation(this.token)
                    iotViewModel.getIotInformation(this.token)
                }
            } catch (e: YandexAuthException) {
                e.message?.let { Log.e("Authorization", it) }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}