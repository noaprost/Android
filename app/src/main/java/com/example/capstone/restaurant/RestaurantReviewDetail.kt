package com.example.capstone.restaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.MainActivity
import com.example.capstone.R
import com.example.capstone.Review
import com.example.capstone.databinding.ActivityRestaurantReviewDetailBinding

class RestaurantReviewDetail : AppCompatActivity() {

    private lateinit var binding: ActivityRestaurantReviewDetailBinding
    private var dummy = ArrayList<Review>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityRestaurantReviewDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reviewRecyclerView.layoutManager= LinearLayoutManager(this)
        binding.reviewRecyclerView.adapter=MyAdapter(dummy)

        binding.backButton.setOnClickListener {
            finish()
        }
    }
    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        private lateinit var review: Review
        private val userName: TextView =itemView.findViewById(R.id.userName)
        private val writeDate: TextView =itemView.findViewById(R.id.writingDate)
        private val keyword1: TextView =itemView.findViewById(R.id.textView11)
        private val keyword2: TextView =itemView.findViewById(R.id.textView12)
        private val keyword3: TextView =itemView.findViewById(R.id.textView13)
        private val comment: TextView =itemView.findViewById(R.id.reviewComment)


        fun bind(review: Review){
            this.review=review

            // writeDate.text=this.review.userName //todo 작성일자 연결
            //todo 사진 연결
        }

    }
    inner class MyAdapter(private val list:List<Review>): RecyclerView.Adapter<MyViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view=layoutInflater.inflate(R.layout.item_review, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int = list.size//최근 4개만

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val post=list[position]
            holder.bind(post)
        }
    }
}