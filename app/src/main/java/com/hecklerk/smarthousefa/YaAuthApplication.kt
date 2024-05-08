package com.hecklerk.smarthousefa

import android.app.Application
import com.hecklerk.smarthousefa.data.AppDataContainer
import com.hecklerk.smarthousefa.data.YaAuthContainer

class YaAuthApplication : Application() {
    private lateinit var _container: AppDataContainer
    val container: AppDataContainer
        get() = _container

    override fun onCreate() {
        super.onCreate()
        _container = YaAuthContainer(this)
    }
}