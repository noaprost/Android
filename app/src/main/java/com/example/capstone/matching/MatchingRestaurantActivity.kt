package com.example.capstone.matching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.databinding.ActivityMatchingRestaurantBinding

class MatchingRestaurantActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMatchingRestaurantBinding
    private var matchingRestaurantList = ArrayList<MatchingRestaurant>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchingRestaurantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.matchingBackBtn.setOnClickListener {
            finish()
        }

        binding.matchingCategoryBtn.setOnClickListener {
            // 카테고리별로 필터 적용되는 기능 추가
        }

        matchingRestaurantList.apply {
            add(
                MatchingRestaurant("온리원 파스타 송도점", "파스타 전문점입니다:)", 5.0, 34, "인천광역시 연수구 송도동")
            )
            add(
                MatchingRestaurant("온리원 파스타 송도점", "파스타 전문점입니다:)", 4.0, 22, "인천광역시 연수구 송도동")
            )
            add(
                MatchingRestaurant("온리원 파스타 송도점", "파스타 전문점입니다:)", 3.9, 8, "인천광역시 연수구 송도동")
            )
        }

        binding.matchingRestaurantRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.matchingRestaurantRecyclerView.setHasFixedSize(true)
        /*binding.matchingRestaurantRecyclerView.adapter = MatchingAdapter(matchingRestaurantList)*/
    }

    /*inner class MatchingViewHolder(view : View): RecyclerView.ViewHolder(view){
        private lateinit var matchingRes: MatchingRestaurant

        fun bind(matchingRes : MatchingRestaurant){
            this.matchingRes = matchingRes

            itemView.setOnClickListener {

            }
        }
    }*/

    // 오류 수정중
    /*inner class MatchingAdapter(private val matchingRestaurantList: List<MatchingRestaurant>): RecyclerView.Adapter<MatchingRestaurant>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchingViewHolder {
            val view = ActivityMatchingRestaurantBinding.inflate(LayoutInflater.from(parent.context), false)
            return MatchingViewHolder(view)
        }

        override fun getItemCount(): Int {
            return matchingRestaurantList.size
        }

        override fun onBindViewHolder(holder: MatchingRestaurant, position: Int) {
            val post = matchingRestaurantList[position]
            holder.bind(post)
        }
    }
}*/
}