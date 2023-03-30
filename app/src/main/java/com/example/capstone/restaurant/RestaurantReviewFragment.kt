package com.example.capstone.restaurant

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.Review
import com.example.capstone.databinding.FragmentRestaurantReviewBinding

class RestaurantReviewFragment : Fragment() {
    private var _binding: FragmentRestaurantReviewBinding? = null
    private val binding get() = _binding!!
    private var dummy = ArrayList<Review>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRestaurantReviewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewReviewButton.setOnClickListener {
            val intent = Intent(activity, RestaurantReviewDetail::class.java)
            startActivity(intent)
        }
        dummy.apply {
            add(
                Review(R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "df")
            )
            add(
                Review(R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                    3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "df")
            )
            add(
                Review(R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                    3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "df")
            )
            add(
                Review(R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                    3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "df")
            )
        }
        binding.restaurantReviewRecyclerView.layoutManager= LinearLayoutManager(context)
        binding.restaurantReviewRecyclerView.adapter=MyAdapter(dummy)
        return root
    }
    inner class MyViewHolder(view:View): RecyclerView.ViewHolder(view){
        private lateinit var review: Review
        private val userName: TextView =itemView.findViewById(R.id.userName)
        //todo 리사이클러뷰 연결

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

        override fun getItemCount(): Int = 3

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val post=list[position]
            holder.bind(post)
        }
    }

}