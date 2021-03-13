package com.soha.weather_app.weather.view.fragments.custom_alarm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soha.weather_app.R
import com.soha.weather_app.weather.db.entity.CustomAlert
import java.util.ArrayList

class CustomAlertadabter(val context: Context) : RecyclerView.Adapter<CustomAlertadabter.ViewHolder>(){

        private var customAlertList: MutableList<CustomAlert>


        init {
            customAlertList = ArrayList<CustomAlert>()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.alert_item, parent, false)
            return ViewHolder(v)
        }

        override fun getItemCount(): Int {
            return customAlertList.size
        }

        fun fetchData(alertList: MutableList<CustomAlert>, context: Context) {
            this.customAlertList = alertList
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
          //  holder.time.text = customAlertList[position].start
        }



        fun getItemByVH(viewHolder: RecyclerView.ViewHolder): CustomAlert {
            return customAlertList.get(viewHolder.adapterPosition)
        }

        fun removeAlertItem(viewHolder: RecyclerView.ViewHolder) {
            customAlertList.removeAt(viewHolder.adapterPosition)
            notifyItemRemoved(viewHolder.adapterPosition)

        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val time = itemView.findViewById(R.id.time) as TextView


        }
    }
