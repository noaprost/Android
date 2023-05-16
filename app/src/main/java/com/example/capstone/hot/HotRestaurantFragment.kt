package com.example.capstone.hot

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstone.*
import com.example.capstone.databinding.FragmentHotRestaurantBinding
import com.example.capstone.retrofit.API
import com.example.capstone.retrofit.IRetrofit
import com.example.capstone.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HotRestaurantFragment : Fragment() {
    private var _binding : FragmentHotRestaurantBinding? = null
    private val binding get() = _binding!!
    lateinit var userInfo: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHotRestaurantBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userInfo = this.requireActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val userId = this.requireActivity().getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE).getString("userId", "")

        hotRestaurant()
        binding.hotBackBtn.setOnClickListener {
            destroy()
        }

        binding.hotCategoryBtn.setOnClickListener {
            // 카테고리별로 필터 적용되는 기능 추가
        }

        return root
    }

    inner class HotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var hotRes: RestaurantInfo
        private val matchResImg : AppCompatImageView = itemView.findViewById(R.id.matchResImg)
        private val matchTitle : TextView = itemView.findViewById(R.id.title)
        private val matchRating : TextView = itemView.findViewById(R.id.rating)
        private val matchCommentNumber : TextView = itemView.findViewById(R.id.commentNumber)
        private val matchAddress : TextView = itemView.findViewById(R.id.address)
        private val keywordBox: LinearLayout = itemView.findViewById(R.id.keywordBox)

        fun bind(hotRes: RestaurantInfo) {
            this.hotRes = hotRes
            keywordBox.visibility=View.GONE
            if(hotRes.resImg != null) {
                val url="${API.BASE_URL}/${hotRes.resImg}"
                Glide.with(this@HotRestaurantFragment)
                    .load(url) // 불러올 이미지 url
                    .error(R.drawable.onlyone_logo) // 로딩 에러 발생 시 표시할 이미지
                    .fallback(R.drawable.onlyone_logo) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                    .override(500, 300)
                    .into(matchResImg) // 이미지를 넣을 뷰
            }
            matchTitle.text = hotRes.resName
            matchRating.text = hotRes.resRating.toString()
            matchCommentNumber.text = hotRes.revCnt.toString()
            matchAddress.text=hotRes.resAddress
            if(hotRes.keyWord !=null){
                var arr:List<String> =listOf("", "", "")
                for (addr in hotRes.keyWord) {
                    val splitedAddr = hotRes.keyWord.split("[\"", "\", \"", "\"]")
                    arr = splitedAddr
                }
            }
            itemView.setOnClickListener {
                getResInfo(ResID(hotRes.resIdx))

            }
        }
    }

    inner class HotAdapter(private val hotRestaurantList: List<RestaurantInfo>) :
        RecyclerView.Adapter<HotViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotViewHolder {
            val view = layoutInflater.inflate(R.layout.item_main_restaurant, parent, false)
            return HotViewHolder(view)
        }

        override fun onBindViewHolder(holder: HotViewHolder, position: Int) {
            val post = hotRestaurantList[position]
            holder.bind(post)
        }

        override fun getItemCount(): Int {
            return hotRestaurantList.size
        }
    }
    private fun hotRestaurant(){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.hotRestaurant() ?:return

        call.enqueue(object : Callback<RecommendRestaurants> {

            override fun onResponse(call: Call<RecommendRestaurants>, response: Response<RecommendRestaurants>){
                Log.d("retrofit", "핫한 음식점 - 응답 성공 / t : ${response.raw()} ${response.body()?.message}")
                val matcharr = response.body()?.message
                if(matcharr != null){
                    binding.hotRestaurantRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    binding.hotRestaurantRecyclerView.setHasFixedSize(true)
                    binding.hotRestaurantRecyclerView.adapter = HotAdapter(matcharr)
                }
            }
            override fun onFailure(call : Call<RecommendRestaurants>, t: Throwable){
                Log.d("retrofit", "핫한 음식점 - 응답 실패 / t: $t")
                binding.hotRestaurantRecyclerView.visibility=View.GONE
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
        fragmentManager.beginTransaction().remove(this@HotRestaurantFragment).commit()
        fragmentManager.popBackStack()

    }
}

