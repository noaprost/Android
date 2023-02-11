package com.example.capstone.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R



class SearchRestaurantAdapter(val searchRestaurantList : ArrayList<SearchRestaurant>) : RecyclerView.Adapter<SearchRestaurantAdapter.SearchViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : SearchRestaurantAdapter.SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant_search, parent, false)
        return SearchViewHolder(view).apply {

            itemView.setOnClickListener {
                val curPos : Int = adapterPosition
                val searchRestaurant : SearchRestaurant = searchRestaurantList.get(curPos)
                // 해당 음식점 상세 페이지로 이동하는 부분 추가
            }
        }
    }


    override fun getItemCount(): Int {
        return searchRestaurantList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int){
        holder.searchRestaurantImg.setImageResource(searchRestaurantList.get(position).searchRestaurantImg)
        holder.waitingTeamNum.text = searchRestaurantList.get(position).waitingTeamNum.toString()
        holder.searchRating.text = searchRestaurantList.get(position).searchRating.toString()
        holder.searchCommentNum.text = searchRestaurantList.get(position).searchCommentNum.toString()
        holder.tag1.text = searchRestaurantList.get(position).tag1
        holder.tag2.text = searchRestaurantList.get(position).tag2
        holder.tag3.text = searchRestaurantList.get(position).tag3
        holder.searchRestaurantImg.clipToOutline=true
    }

    class SearchViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val searchRestaurantImg = itemView.findViewById<ImageView>(R.id.searchRestaurantImage)
        val waitingTeamNum = itemView.findViewById<TextView>(R.id.waitingTeamNum)
        val searchRating = itemView.findViewById<TextView>(R.id.searchRating)
        val searchCommentNum = itemView.findViewById<TextView>(R.id.searchCommentNumber)
        val tag1 = itemView.findViewById<TextView>(R.id.tag1)
        val tag2 = itemView.findViewById<TextView>(R.id.tag2)
        val tag3 = itemView.findViewById<TextView>(R.id.tag3)
    }
}