package com.hecklerk.smarthousefa.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hecklerk.smarthousefa.data.Device
import com.hecklerk.smarthousefa.databinding.ListItemBinding

class DeviceAdapter(private var devicesList: List<Device>): RecyclerView.Adapter<DeviceAdapter.DevicesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevicesHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DevicesHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: DevicesHolder, position: Int) {
        val devicesBean: Device = devicesList[position]
        holder.bind(devicesBean)
    }

    override fun getItemCount(): Int = devicesList.size

    class DevicesHolder(private val itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(deviceBean: Device) {
            itemBinding.idView.text = deviceBean.id
            itemBinding.nameView.text = deviceBean.name
        }
    }

    fun setData(devicesList: List<Device>) {
        this.devicesList = devicesList
    }

}