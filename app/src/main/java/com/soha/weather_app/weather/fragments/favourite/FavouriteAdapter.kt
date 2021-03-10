package com.soha.weather_app.weather.fragments.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soha.weather_app.databinding.FavouriteCardBinding
import com.soha.weather_app.weather.db.model.entity.FavouriteData

class FavouriteAdapter(var favList:List<FavouriteData>, listener: OnItemClickListener)
    : RecyclerView.Adapter<FavouriteAdapter.FavViewHolder>() {


    private var listenerContact: OnItemClickListener = listener

    interface OnItemClickListener {
        fun onItemClick(contact: FavouriteData)
    }


    class FavViewHolder(var view: FavouriteCardBinding) : RecyclerView.ViewHolder(view.root) {
        private val imageview = view.imgForecastItem
        fun bind(favList: FavouriteData, listener: OnItemClickListener) {

            view.tvForecastHumidity.text=favList.lat.toString()
            view.tvForecastTemp.text = favList.lon.toString()

            itemView.setOnClickListener {
                listener.onItemClick(favList)
            }

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
        holder.bind(favList[position], listenerContact)



//        holder.itemView.setOnClickListener{
//            Toast.makeText(it.context,"$position",Toast.LENGTH_LONG).show()
//            //val bundle = bundleOf("", favList[position])
//          //  Navigation.findNavController(it).navigate(R.id.action_sevenDayFragment_to_favouriteFragment, bundle)
//        }
    }

    override fun getItemCount(): Int {
        return favList.size
    }

    fun setData(d:List<FavouriteData>){
        this.favList = d
        notifyDataSetChanged()

    }

    interface OnFavouriteItemClickListner{
        fun onItemClick(item: FavouriteData, position: Int)
    }

}





