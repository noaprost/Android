package com.example.capstone.matching

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
    lateinit var userInfo: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMatchingRestaurantBinding.inflate(layoutInflater, container, false)
        val root: View = binding.root
        userInfo = this.requireActivity().getSharedPreferences("userInfo", MODE_PRIVATE)
        val userId = this.requireActivity().getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE).getString("userId", "2")
        val userNickname = this.requireActivity().getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE).getString("userNickname", "")

        binding.textView66.text="${userNickname}님을 위한 추천 음식점"
        recommendRestaurant(userId("2"))
        binding.matchingBackBtn.setOnClickListener {
            destroy()
        }

        binding.matchingCategoryBtn.setOnClickListener {
            // 카테고리별로 필터 적용되는 기능 추가
        }
        return root
    }

    inner class MatchingViewHolder(view: View): RecyclerView.ViewHolder(view){
        private lateinit var matchingRes: RestaurantInfo
        private val matchResImg : AppCompatImageView = itemView.findViewById(R.id.matchResImg)
        private val matchTitle : TextView = itemView.findViewById(R.id.title)
        private val tag1 : TextView = itemView.findViewById(R.id.tag1)
        private val tag2 : TextView = itemView.findViewById(R.id.tag2)
        private val tag3 : TextView = itemView.findViewById(R.id.tag3)
        private val matchRating : TextView = itemView.findViewById(R.id.rating)
        private val matchCommentNumber : TextView = itemView.findViewById(R.id.commentNumber)
        private val matchAddress : TextView = itemView.findViewById(R.id.address)

        fun bind(Restaurants : RestaurantInfo){
            this.matchingRes = Restaurants
            if(Restaurants.resImg != null) {
                val url="${API.BASE_URL}/${Restaurants.resImg}"
                Glide.with(this@MatchingRestaurantFragment)
                    .load(url) // 불러올 이미지 url
                    .error(R.drawable.onlyone_logo) // 로딩 에러 발생 시 표시할 이미지
                    .fallback(R.drawable.onlyone_logo) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                    .override(500, 300)
                    .into(matchResImg) // 이미지를 넣을 뷰
            }
            matchTitle.text = Restaurants.resName
            matchRating.text = Restaurants.resRating.toString()
            matchCommentNumber.text = Restaurants.revCnt.toString()
            matchAddress.text=Restaurants.resAddress
            if(Restaurants.keyWord !=null){
                var arr:List<String> =listOf("", "", "")
                for (addr in Restaurants.keyWord) {
                    val splitedAddr = Restaurants.keyWord.split("[\"", "\", \"", "\"]")
                    arr = splitedAddr
                }
                tag1.text="#"+arr[1]
                tag2.text="#"+arr[2]
                tag3.text="#"+arr[3]
            }
            itemView.setOnClickListener {
                getResInfo(ResID(Restaurants.resIdx))
            }
        }
    }

    inner class MatchingAdapter(private val matchingRestaurantList: List<RestaurantInfo>): RecyclerView.Adapter<MatchingViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchingViewHolder {
            val view = layoutInflater.inflate(R.layout.item_main_restaurant, parent, false)
            return MatchingViewHolder(view)
        }

        override fun onBindViewHolder(holder: MatchingViewHolder, position: Int) {
            val post = matchingRestaurantList[position]
            holder.bind(post)
        }

        override fun getItemCount(): Int = matchingRestaurantList.size
    }

    private fun recommendRestaurant(userId: userId){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.recommendRestaurant(userId) ?:return

        call.enqueue(object : Callback<RecommendRestaurants> {

            override fun onResponse(call: Call<RecommendRestaurants>, response: Response<RecommendRestaurants>){
                Log.d("retrofit", "음식점 매칭- 응답 성공 / t : ${response.raw()} ${response.body()?.message}")
                val matcharr = response.body()?.message
                if(matcharr != null){
                    binding.matchingRestaurantRecyclerView.visibility=View.VISIBLE
                    binding.matchingRestaurantRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    binding.matchingRestaurantRecyclerView.adapter = MatchingAdapter(matcharr)
                }
            }
            override fun onFailure(call : Call<RecommendRestaurants>, t: Throwable){
                Log.d("retrofit", "음식점 매칭 - 응답 실패 / t: $t")
                binding.matchingRestaurantRecyclerView.visibility=View.GONE
            }
        })
    }
    private fun getResInfo(ResID: ResID){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.getResInfo(ResID) ?:return

        call.enqueue(object : Callback<List<Restaurants>> {

            override fun onResponse(call: Call<List<Restaurants>>, response: Response<List<Restaurants>>) {
                Log.d("retrofit", "레스토랑 정보 - 응답 성공 / t : ${response.raw()} ${response.body()}")
                val bundle=Bundle()
                bundle.putSerializable("restaurant", response.body()!![0])
                val mainAct = activity as MainActivity
                mainAct.ChangeFragment("Restaurant", bundle)
            }
            override fun onFailure(call: Call<List<Restaurants>>, t: Throwable) {
                Log.d("retrofit", "레스토랑 정보 - 응답 실패 / t: $t")
                Toast.makeText(activity, "레스토랑 정보를 불러올 수 없습니다.", Toast.LENGTH_LONG).show()

            }
        })
    }
    private fun destroy(){
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction().remove(this@MatchingRestaurantFragment).commit()
        fragmentManager.popBackStack()

    }
}
