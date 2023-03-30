package com.example.capstone.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.MainActivity
import com.example.capstone.R
import com.example.capstone.restaurant.RestaurantReviewDetail


class PreviousWaitingAdapter(val previousWaitingList: ArrayList<PreviousWaiting>, fragment : HistoryFragment) : RecyclerView.Adapter<PreviousWaitingAdapter.WaitingViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : WaitingViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_previous_waiting_info, parent, false)
        return WaitingViewHolder(view).apply {
            itemView.setOnClickListener {

                val curPos : Int = adapterPosition
                val watingHistory : PreviousWaiting = previousWaitingList.get(curPos)

                val detailBtn : Button = itemView.findViewById(R.id.historyDetailBtn)
                val reviewBtn : Button = itemView.findViewById(R.id.historyReviewBtn)

                detailBtn.setOnClickListener {

                }

                reviewBtn.setOnClickListener {

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return previousWaitingList.size
    }

    override fun onBindViewHolder(holder: WaitingViewHolder, position: Int) {
        holder.txtWaitingDate.text = previousWaitingList.get(position).txtWaitingDate
        holder.waitingImg.setImageResource(previousWaitingList.get(position).waitingImg)
        holder.waitingTitle.text = previousWaitingList.get(position).waitingTitle
        holder.waitingImg.clipToOutline=true
    }

    class WaitingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtWaitingDate : TextView = itemView.findViewById(R.id.txtWaitingDate)
        val waitingImg: ImageView = itemView.findViewById(R.id.waitingImg)
        val waitingTitle: TextView = itemView.findViewById(R.id.txtWatingTitle)
    }
}