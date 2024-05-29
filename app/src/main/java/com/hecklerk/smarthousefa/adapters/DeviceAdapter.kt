package com.hecklerk.smarthousefa.adapters

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hecklerk.smarthousefa.data.Device
import com.hecklerk.smarthousefa.databinding.ListItemBinding

class DeviceAdapter(private var devicesList: List<Device>): RecyclerView.Adapter<DeviceAdapter.DevicesHolder>() {
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevicesHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DevicesHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: DevicesHolder, position: Int) {
        val devicesBean: Device = devicesList[position]
        holder.bind(devicesBean)
        holder.itemView.setOnClickListener{
            if (onClickListener != null) {
                onClickListener!!.onClick(position, devicesBean)
            }
        }
    }

    override fun getItemCount(): Int = devicesList.size

    class DevicesHolder(private val itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(deviceBean: Device) {
            itemBinding.idView.text = deviceBean.id
            itemBinding.nameView.text = deviceBean.name
        }
    }

    interface OnClickListener {
        fun onClick(position: Int, model: Device)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun setData(devicesList: List<Device>) {
        this.devicesList = devicesList
    }

}