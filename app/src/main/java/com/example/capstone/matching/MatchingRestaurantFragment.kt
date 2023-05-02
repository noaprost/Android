package com.example.capstone.matching

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.MainActivity
import com.example.capstone.*
import com.example.capstone.databinding.FragmentMatchingRestaurantBinding
import com.example.capstone.retrofit.API
import com.example.capstone.retrofit.IRetrofit
import com.example.capstone.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MatchingRestaurantFragment : Fragment() {
    private var _binding : FragmentMatchingRestaurantBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMatchingRestaurantBinding.inflate(layoutInflater, container, false)
        val root: View = binding.root

        binding.matchingBackBtn.setOnClickListener {
            this.onDestroy()
        }

        binding.matchingCategoryBtn.setOnClickListener {
            // 카테고리별로 필터 적용되는 기능 추가
        }
        return root
    }

    inner class MatchingViewHolder(view: View): RecyclerView.ViewHolder(view){
        private lateinit var matchingRes: Restaurants
        private val matchResImg : AppCompatImageView = itemView.findViewById(R.id.matchResImg)
        private val matchTitle : TextView = itemView.findViewById(R.id.title)
        private val matchInfo : TextView = itemView.findViewById(R.id.info)
        private val matchRating : TextView = itemView.findViewById(R.id.rating)
        private val matchCommentNumber : TextView = itemView.findViewById(R.id.commentNumber)
        private val matchAddress : TextView = itemView.findViewById(R.id.address)

        fun bind(Restaurants : Restaurants){
            this.matchingRes = Restaurants
            if(Restaurants.resImg != null) matchResImg.setBackgroundResource(Restaurants.resImg.toInt())
            matchTitle.text = Restaurants.resName
            matchInfo.text = "파스타 전문점입니다:)"
            matchRating.text = Restaurants.resRating.toString()
            matchCommentNumber.text = Restaurants.revCnt.toString()
            matchAddress.text = Restaurants.resAddress

            itemView.setOnClickListener {
                val bundle=Bundle()
                bundle.putSerializable("matchingRes", matchingRes)
                val mainAct = activity as MainActivity
                mainAct.ChangeFragment("Restaurant", bundle)
            }
        }
    }

    inner class MatchingAdapter(private val matchingRestaurantList: List<Restaurants>): RecyclerView.Adapter<MatchingViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchingViewHolder {
            val view = layoutInflater.inflate(R.layout.item_main_restaurant, parent, false)
            return MatchingViewHolder(view)
        }

        override fun onBindViewHolder(holder: MatchingViewHolder, position: Int) {
            val post = matchingRestaurantList[position]
            holder.bind(post)
        }

        override fun getItemCount(): Int {
            return 10
        }
    }

    // 추천 음식점 목록으로 변경 필요
    private fun matchingRestaurant(resAddress: resAddress){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.showRestaurants(resAddress = resAddress) ?:return

        call.enqueue(object : Callback<RestaurantList> {

            override fun onResponse(call: Call<RestaurantList>, response: Response<RestaurantList>){
                Log.d("retrofit", "음식점 검색 - 응답 성공 / t : ${response.raw()} ${response.body()?.results}")
                val matcharr = response.body()?.results
                if(matcharr != null){
                    binding.matchingRestaurantRecyclerView.visibility=View.VISIBLE
                    binding.matchingRestaurantRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    binding.matchingRestaurantRecyclerView.setHasFixedSize(true)
                    binding.matchingRestaurantRecyclerView.adapter = MatchingAdapter(matcharr)
                }
            }
            override fun onFailure(call : Call<RestaurantList>, t: Throwable){
                Log.d("retrofit", "음식점 검색 - 응답 실패 / t: $t")
                binding.matchingRestaurantRecyclerView.visibility=View.GONE
            }
        })
    }
}
