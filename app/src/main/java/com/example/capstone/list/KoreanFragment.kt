package com.example.capstone.list

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstone.*
import com.example.capstone.databinding.FragmentKoreanBinding
import com.example.capstone.retrofit.API
import com.example.capstone.retrofit.IRetrofit
import com.example.capstone.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KoreanFragment : Fragment() {

    private var _binding: FragmentKoreanBinding? = null
    private val binding get() = _binding!!
    private var category=""
    lateinit var userInfo: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKoreanBinding.inflate(inflater, container, false)
        val root: View = binding.root
        category=arguments?.getString("category").toString()
        userInfo = this.requireActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val userLocation=userInfo.getString("userLocation", "")!!
        var arr: List<String> = listOf("인천광역시", "연수구", "송도동") //기본 주소
        for (addr in userLocation) {
            val splitedAddr = userLocation.split(" ")
            arr = splitedAddr
        }
        val requestString=arr[1]
        if(category=="전체") showRestaurants(resAddress(requestString))
        else showRestaurantsByCategory(resAddressCategory(requestString, category))
        return root
    }
    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        private lateinit var restaurantList: Restaurants
        private val restaurantImg : ConstraintLayout = itemView.findViewById(R.id.list_image)
        private val rating : TextView = itemView.findViewById(R.id.list_rating)
        private val commentNumber: TextView = itemView.findViewById(R.id.list_commentNumber)
        private val name: TextView = itemView.findViewById(R.id.list_name)
        private val keyword1: TextView = itemView.findViewById(R.id.list_keyword1)
        private val keyword2: TextView = itemView.findViewById(R.id.list_keyword2)
        private val keyword3: TextView = itemView.findViewById(R.id.list_keyword3)
        private val waitingNum: TextView = itemView.findViewById(R.id.list_waiting)

        fun bind(restaurantList: Restaurants){
            this.restaurantList=restaurantList
            //if(restaurantList.resImg != null)restaurantImg.setBackgroundResource(restaurantList.resImg)
            rating.text = restaurantList.resRating.toString()
            commentNumber.text = restaurantList.revCnt.toString()
            name.text = restaurantList.resName
            if(restaurantList.keyWord !=null){
                var arr:List<String> =listOf("", "", "")
                for (addr in restaurantList.keyWord) {
                    val splitedAddr = restaurantList.keyWord.split("[\"", "\", \"", "\"]")
                    arr = splitedAddr
                }
                keyword1.text="#"+arr[1]
                keyword2.text="#"+arr[2]
                keyword3.text="#"+arr[3]
            }
            waitingNum.text=restaurantList.currWaiting.toString()
            itemView.setOnClickListener {
                val bundle=Bundle()
                bundle.putSerializable("restaurant", restaurantList)
                val mainAct = activity as MainActivity
                mainAct.ChangeFragment("Restaurant", bundle)
            }
            when (restaurantList.resCategory) {
                "한식" -> restaurantImg.setBackgroundResource(R.drawable.dummy_korean)
                "중식" -> restaurantImg.setBackgroundResource(R.drawable.dummy_chinese)
                "양식" -> restaurantImg.setBackgroundResource(R.drawable.dummy_restaurant_image)
                "일식" -> restaurantImg.setBackgroundResource(R.drawable.dummy_japanese)
                "카페/베이커리" -> restaurantImg.setBackgroundResource(R.drawable.dummy_bakery)
                else -> restaurantImg.setBackgroundResource(R.drawable.dummy_alcohol)
            }
            if(restaurantList.resImg!=null) {
                /*
                val url="${API.BASE_URL}/${restaurantList.resImg}"
                Glide.with(this@KoreanFragment)
                    .load(url) // 불러올 이미지 url
                    .error(R.drawable.ic_flag) // 로딩 에러 발생 시 표시할 이미지
                    .fallback(R.drawable.onlyone_logo) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                    .override(500, 300)
                    .into(restaurantImg) // 이미지를 넣을 뷰

                 */
                //캡쳐용 코드

            }
        }
    }
    inner class MyAdapter(private val list:List<Restaurants>): RecyclerView.Adapter<MyViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view=layoutInflater.inflate(R.layout.item_restaurant_list, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int = 5//list.size

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val post = list[position]
            holder.bind(post)
        }
    }
    private fun showRestaurants(resAddress: resAddress){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.showRestaurants(resAddress=resAddress) ?:return

        call.enqueue(object : Callback<RestaurantList> {

            override fun onResponse(call: Call<RestaurantList>, response: Response<RestaurantList>) {
                Log.d("retrofit", "음식점 리스트 - 응답 성공 / t : ${response.raw()} ${response.body()?.results}")
                val arr= response.body()?.results
                if (arr != null) {
                    binding.textView87.visibility=View.INVISIBLE
                    binding.restaurantListRecyclerView.visibility=View.VISIBLE
                    binding.restaurantListRecyclerView.layoutManager = LinearLayoutManager(context)
                    binding.restaurantListRecyclerView.adapter = MyAdapter(arr)
                }else{
                    binding.textView87.visibility=View.VISIBLE
                    binding.restaurantListRecyclerView.visibility=View.INVISIBLE
                }
            }
            override fun onFailure(call: Call<RestaurantList>, t: Throwable) {
                Log.d("retrofit", "음식점  리스트 - 응답 실패 / t: $t")
                //Toast.makeText(activity, "리스트를 불러올 수 없습니다.", Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun showRestaurantsByCategory(resAddressCategory: resAddressCategory){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.showRestaurantsByCategory(resAddressCategory=resAddressCategory) ?:return

        call.enqueue(object : Callback<RestaurantList> {

            override fun onResponse(call: Call<RestaurantList>, response: Response<RestaurantList>) {
                Log.d("retrofit", "음식점 리스트 -  ${category} 응답 성공 / t : ${response.raw()} ${response.body()?.results}")
                val arr= response.body()?.results
                if (arr != null) {
                    binding.textView87.visibility=View.INVISIBLE
                    binding.restaurantListRecyclerView.visibility=View.VISIBLE
                    binding.restaurantListRecyclerView.layoutManager = LinearLayoutManager(context)
                    binding.restaurantListRecyclerView.adapter = MyAdapter(arr)
                }else{
                    binding.textView87.visibility=View.VISIBLE
                    binding.restaurantListRecyclerView.visibility=View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<RestaurantList>, t: Throwable) {
                Log.d("retrofit", "음식점 리스트 - ${category} 응답 실패 / t: $t")
                Toast.makeText(activity, "리스트를 불러올 수 없습니다.", Toast.LENGTH_LONG).show()
            }
        })
    }
}