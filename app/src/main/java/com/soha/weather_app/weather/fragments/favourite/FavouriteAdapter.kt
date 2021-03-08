package com.soha.weather_app.weather.fragments.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soha.weather_app.databinding.FavouriteCardBinding
import com.soha.weather_app.weather.db.models.currentModel.FavCurrent
import com.soha.weather_app.weather.db.models.currentModel.WeatherX
import com.soha.weather_app.weather.db.models.weatherModel.FavouriteData

class FavouriteAdapter(var favList:List<FavCurrent>)
    : RecyclerView.Adapter<FavouriteAdapter.FavViewHolder>() {

    class FavViewHolder(var view: FavouriteCardBinding) : RecyclerView.ViewHolder(view.root) {
        private val imageview = view.imgForecastItem
        fun bind(favList: FavCurrent) {

            view.tvForecastHumidity.text=favList.name
            view.tvForecastTemp.text = favList.base

        }

       /* fun initialize(item: FavouriteData, action:OnFavouriteItemClickListner){
            view.tvForecastHumidity.text = item.timezone
            view.tvForecastTemp.text = item.daily.get(0).humidity.toString()


            itemView.setOnClickListener{
                action.onItemClick(item,adapterPosition)
            }

        }*/


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {

        val view = FavouriteCardBinding.inflate(LayoutInflater.from(parent.context), parent,false)

        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.bind(favList[position])



//        holder.itemView.setOnClickListener{
//            Toast.makeText(it.context,"$position",Toast.LENGTH_LONG).show()
//            //val bundle = bundleOf("", favList[position])
//          //  Navigation.findNavController(it).navigate(R.id.action_sevenDayFragment_to_favouriteFragment, bundle)
//        }
    }

    override fun getItemCount(): Int {
        return favList.size
    }

    fun setData(d:List<FavCurrent>){
        this.favList = d
        notifyDataSetChanged()

    }

    interface OnFavouriteItemClickListner{
        fun onItemClick(item: FavouriteData, position: Int)
    }

}





