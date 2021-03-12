package com.soha.weather_app.weather.view.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.soha.weather_app.databinding.FavouriteItemBinding
import com.soha.weather_app.weather.db.entity.FavouriteData
import com.soha.weather_app.weather.utils.dayConverter
import com.soha.weather_app.weather.utils.getAddressGeocoder
import com.soha.weather_app.weather.utils.setImage

class FavouriteAdapter(var favList: MutableList<FavouriteData>, listener: OnItemClickListener) :
    RecyclerView.Adapter<FavouriteAdapter.FavViewHolder>() {
    lateinit var context: Context
    private var listenerContact: OnItemClickListener = listener

    interface OnItemClickListener {
        fun onItemClick(contact: FavouriteData)
    }


    fun setData(list: MutableList<FavouriteData>, context: Context) {
        this.favList = list
        this.context = context
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {

        val view = FavouriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.bind(favList[position], listenerContact)

        var sp= PreferenceManager.getDefaultSharedPreferences(context)

        context?.let {
            holder.view.tvFavAddress.text =getAddressGeocoder( favList[position].lat, favList[position].lon, it)
            holder.view.textCelcius.text = sp.getString("cel", "")

        }


    }

    public fun getFavByVH(viewHolder: RecyclerView.ViewHolder): FavouriteData {
        return favList.get(viewHolder.adapterPosition)
    }

    fun removeFavItem(viewHolder: RecyclerView.ViewHolder) {
        favList.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)

    }

    override fun getItemCount(): Int {
        return favList.size
    }


    class FavViewHolder(var view: FavouriteItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(favList: FavouriteData, listener: OnItemClickListener) {
            view.tvFavDate.text = dayConverter(favList.daily.get(0).dt.toLong())
            view.tvForecastState.text = favList.daily.get(0).weather.get(0).description
            view.tvForecastTemp.text = favList.daily.get(0).temp.day.toString()
            val img = favList.daily.get(0).weather.get(0).icon


            setImage(view.imgForecastItem, img)

            itemView.setOnClickListener {
                listener.onItemClick(favList)
            }

        }

    }


}





