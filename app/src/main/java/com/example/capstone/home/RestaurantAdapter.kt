package com.example.capstone.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R

class RestaurantAdapter(val restaurantList: ArrayList<Restaurant>) : RecyclerView.Adapter<RestaurantAdapter.ResViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : RestaurantAdapter.ResViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
        return ResViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPos : Int = adapterPosition
                val restaurant : Restaurant = restaurantList.get(curPos)
                // 해당 음식점 상세 페이지로 이동하는 부분 추가
            }
        }
    }

    override fun getItemCount(): Int {
        return restaurantList.size

    }

    override fun onBindViewHolder(holder: ResViewHolder, position: Int) {
        holder.restaurantImg.setImageResource(restaurantList.get(position).restaurantImg)
        holder.rating.text = restaurantList.get(position).rating.toString()
        holder.commentNumber.text = restaurantList.get(position).commentNumber.toString()
        holder.name.text = restaurantList.get(position).name
        holder.restaurantImg.clipToOutline=true
    }

    class ResViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val restaurantImg : ImageView = itemView.findViewById<ImageView>(R.id.restaurantImage) // 음식점 메인 이미지
        val rating : TextView = itemView.findViewById<TextView>(R.id.rating)
        val commentNumber: TextView = itemView.findViewById<TextView>(R.id.commentNumber)
        val name: TextView = itemView.findViewById<TextView>(R.id.restaurantName)
    }

}