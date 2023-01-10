package com.example.capstone.restaurant

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.Review

class RestaurantReviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurant_review, container, false)
    }
    inner class MyViewHolder(view:View): RecyclerView.ViewHolder(view){
        private lateinit var review: Review
        private val userName: TextView =itemView.findViewById(R.id.userName)


        fun bind(review: Review){
            this.review=review
            userName.text=this.review.userName


            itemView.setOnClickListener{

            }
        }

    }
    inner class MyAdapter(private val list:List<Review>): RecyclerView.Adapter<MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=layoutInflater.inflate(R.layout.item_review, parent, false)
        return MyViewHolder(view)
    }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val post=list[position]
            holder.bind(post)
        }
    }

}