package com.hecklerk.smarthousefa

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import coil.load
import com.hecklerk.smarthousefa.databinding.ActivityMainBinding
import com.hecklerk.smarthousefa.ui.YaAuthViewModel
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

    private val viewModel: YaAuthViewModel by viewModels(factoryProducer = { YaAuthViewModel.Factory })

    private lateinit var sP: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setLoginButton()
        hideInfoFields()

        sP = getPreferences(Context.MODE_PRIVATE)
        token = sP.getString("token", "").toString()

        viewModel.userInfo.observe(this@MainActivity) {
            _binding.loginButton.text = getString(R.string.logout)
            showInfoFields()

            it.let {
                _binding.fullNameTextView.text = it.realName
            }
        }

        if (token.isEmpty())
            setLoginButton()
        else
            viewModel.getUserInformation(token)
    }

    private fun hideInfoFields() {
        _binding.fullNameTextView.visibility = View.GONE
    }

    private fun showInfoFields() {
        _binding.fullNameTextView.visibility = View.VISIBLE
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
                    viewModel.getUserInformation(this.token)
                }
            } catch (e: YandexAuthException) {
                e.message?.let { Log.e("Authorization", it) }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}