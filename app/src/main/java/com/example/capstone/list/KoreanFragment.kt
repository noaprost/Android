package com.example.capstone.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    var category=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKoreanBinding.inflate(inflater, container, false)
        val root: View = binding.root
        category=arguments?.getString("category").toString()

        showRestaurants(resAddress(resAddress = "연수")) //todo 주소 연결
        return root
    }
    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        private lateinit var restaurantList: Restaurants
        val restaurantImg : ConstraintLayout = itemView.findViewById(R.id.list_image)
        val rating : TextView = itemView.findViewById(R.id.list_rating)
        val commentNumber: TextView = itemView.findViewById(R.id.list_commentNumber)
        val name: TextView = itemView.findViewById(R.id.list_name)
        val keyword1: TextView = itemView.findViewById(R.id.list_keyword1)
        val keyword2: TextView = itemView.findViewById(R.id.list_keyword2)
        val keyword3: TextView = itemView.findViewById(R.id.list_keyword3)
        val waitingNum: TextView = itemView.findViewById(R.id.list_waiting)

        fun bind(restaurantList: Restaurants){
            this.restaurantList=restaurantList

            //restaurantImg.setBackgroundResource(restaurantList.resImg)
            rating.text = restaurantList.resRating.toString()
            commentNumber.text = restaurantList.commentNumber.toString()
            name.text = restaurantList.resName
            keyword1.text=restaurantList.keyword1
            keyword2.text=restaurantList.keyword2
            keyword3.text=restaurantList.keyword3
            waitingNum.text=restaurantList.waiting.toString()
            itemView.setOnClickListener {
                val mainAct = activity as MainActivity
                mainAct.ChangeFragment("Restaurant")
            }

        }

    }
    inner class MyAdapter(private val list:List<Restaurants>): RecyclerView.Adapter<MyViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view=layoutInflater.inflate(R.layout.item_restaurant_list, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val post = list[position]
            holder.bind(post)
        }
    }
    private fun showRestaurants(resAddress: resAddress){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.showRestaurants(resAddress=resAddress) ?:return

        Log.d("retrofit", "${resAddress}")
        call.enqueue(object : Callback<RestaurantList> {

            override fun onResponse(call: Call<RestaurantList>, response: Response<RestaurantList>) {
                Log.d("retrofit", "음식점 리스트 - 응답 성공 / t : ${response.raw()} ${response.body()?.results}")
                val arr= response.body()?.results
                if (arr != null) {
                    binding.restaurantListRecyclerView.layoutManager = LinearLayoutManager(context)
                    binding.restaurantListRecyclerView.adapter = MyAdapter(arr)
                }else{
                    Toast.makeText(activity, "리스트를 불러올 수 없습니다.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RestaurantList>, t: Throwable) {
                Log.d("retrofit", "음식점 리스트 - 한식 응답 실패 / t: $t")
            }
        })
    }
}