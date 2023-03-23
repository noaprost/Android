package com.example.capstone.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.MainActivity
import com.example.capstone.MyReview
import com.example.capstone.R
import com.example.capstone.Review
import com.example.capstone.databinding.ActivityMyReviewBinding
import com.google.android.gms.maps.SupportMapFragment

class MyReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyReviewBinding
    private var dummy = ArrayList<MyReview>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMyReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            finish()
        }
        dummy.apply {
            add(MyReview(R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
        }
        binding.myReviewRecyclerView.layoutManager=LinearLayoutManager(this)
        binding.myReviewRecyclerView.adapter=MyAdapter(dummy)

    }
    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        private lateinit var review: MyReview
        private val reviewComment: TextView =itemView.findViewById(R.id.reviewComment)
        private val deleteButton:TextView = itemView.findViewById(R.id.myReviewDelete)

        //todo 리사이클러뷰 연결

        fun bind(review: MyReview){
            this.review=review
            reviewComment.text=this.review.comment


            deleteButton.setOnClickListener{

            }
        }

    }
    inner class MyAdapter(private val list:List<MyReview>): RecyclerView.Adapter<MyViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view=layoutInflater.inflate(R.layout.item_my_review, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val post=list[position]
            holder.bind(post)
        }
    }
}