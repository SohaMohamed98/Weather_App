package com.soha.weather_app.weather.view.fragments.alarm

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.soha.weather_app.R
import com.soha.weather_app.weather.db.entity.AlarmEntity
import java.util.ArrayList

class Alarmadabter(val context: Context) : RecyclerView.Adapter<Alarmadabter.ViewHolder>(){
        private var alarmList: MutableList<AlarmEntity>
        lateinit var sp:SharedPreferences
        init {
            alarmList = ArrayList<AlarmEntity>()
            sp= PreferenceManager.getDefaultSharedPreferences(context)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.alert_item, parent, false)
            return ViewHolder(v)
        }

        override fun getItemCount(): Int {
            return alarmList.size
        }

        fun fetchData(alertList: MutableList<AlarmEntity>, context: Context) {
            this.alarmList = alertList
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.time.text = alarmList[position].TimeFrom
            holder.state.text=alarmList[position].main
        }



        fun getItemByVH(viewHolder: RecyclerView.ViewHolder): AlarmEntity {
            return alarmList.get(viewHolder.adapterPosition)
        }

        fun removeAlarmItem(viewHolder: RecyclerView.ViewHolder) {
            alarmList.removeAt(viewHolder.adapterPosition)
            notifyItemRemoved(viewHolder.adapterPosition)

        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val time = itemView.findViewById(R.id.time) as TextView
            val state = itemView.findViewById(R.id.tv_status) as TextView


        }
    }