package com.soha.weather_app.weather.view.fragments.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soha.weather_app.databinding.FavouriteItemBinding
import com.soha.weather_app.utils.setImage
import com.soha.weather_app.weather.db.entity.AlertEntity
import com.soha.weather_app.weather.db.entity.FavouriteData

class FavouriteAdapter(var favList:MutableList<FavouriteData>, listener: OnItemClickListener)
    : RecyclerView.Adapter<FavouriteAdapter.FavViewHolder>() {


    private var listenerContact: OnItemClickListener = listener

    interface OnItemClickListener {
        fun onItemClick(contact: FavouriteData)
    }


    class FavViewHolder(var view: FavouriteItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(favList: FavouriteData, listener: OnItemClickListener) {

            view.tvForecastState.text=favList.daily.get(0).weather.get(0).description
            view.tvForecastTemp.text =favList.daily.get(0).temp.day.toString()
            val img=favList.daily.get(0).weather.get(0).icon
            setImage(view.imgForecastItem, img)

            itemView.setOnClickListener {
                listener.onItemClick(favList)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {

        val view = FavouriteItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)

        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.bind(favList[position], listenerContact)

    }

   public fun getFavByVH(viewHolder: RecyclerView.ViewHolder): FavouriteData {
        return favList.get(viewHolder.adapterPosition)
    }
    fun removeFavItem(viewHolder: RecyclerView.ViewHolder){
       favList.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)

    }

    override fun getItemCount(): Int {
        return favList.size
    }

    fun setData(d:MutableList<FavouriteData>){
        this.favList = d
        notifyDataSetChanged()

    }


}





