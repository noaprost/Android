package com.example.capstone.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.*
import com.example.capstone.databinding.FragmentSearchBinding
import com.example.capstone.retrofit.API
import com.example.capstone.retrofit.IRetrofit
import com.example.capstone.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment() {

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        /*binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener){
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("hy", query.toString())
                if (query != null) searchRestaurants(resName(query))
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.restaurantSearchRecyclerView.visibility = View.GONE
                return true
            }
        }*/
        return root
    }

    inner class SearchViewHolder(view : View): RecyclerView.ViewHolder(view){
        private lateinit var restaurant: Restaurants
        private val restaurantImg : AppCompatImageView = itemView.findViewById(R.id.searchRestaurantImage)
        private val rating : TextView = itemView.findViewById(R.id.searchRating)
        private val commentNumber: TextView = itemView.findViewById(R.id.searchCommentNumber)
        private val name: TextView = itemView.findViewById(R.id.searchRestaurantName)
        private val keyword1: TextView = itemView.findViewById(R.id.tag1)
        private val keyword2: TextView = itemView.findViewById(R.id.tag2)
        private val keyword3: TextView = itemView.findViewById(R.id.tag3)
        private val waitingNum: TextView = itemView.findViewById(R.id.waitingTeamNum)
        fun bind(Restaurants : Restaurants){
            this.restaurant = Restaurants
            //restaurantImg.setBackgroundResource(restaurantList.resImg)
            restaurantImg.clipToOutline=true
            rating.text = Restaurants.resRating.toString()
            commentNumber.text = Restaurants.revCnt.toString()
            name.text = Restaurants.resName

            if(Restaurants.keyWord !=null){
                var arr:List<String> =listOf("", "", "")
                for (addr in Restaurants.keyWord) {
                    val splitedAddr = Restaurants.keyWord.split("[", "]", ",", "\"")
                    arr = splitedAddr
                }
                keyword1.text="#"+arr[2]
                keyword2.text="#"+arr[5]
                keyword3.text="#"+arr[8]
            }

            waitingNum.text=Restaurants.currWaiting.toString()
            itemView.setOnClickListener {
                val bundle=Bundle()
                bundle.putSerializable("restaurant", restaurant)
                val mainAct = activity as MainActivity
                mainAct.ChangeFragment("Restaurant", bundle)
            }
        }
    }

    inner class SearchAdapter(private val searchList:List<Restaurants>): RecyclerView.Adapter<SearchViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
            val view = layoutInflater.inflate(R.layout.item_restaurant_search, parent, false)
            return SearchViewHolder(view)
        }

        override fun getItemCount(): Int = searchList.size

        override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
            val post=searchList[position]
            holder.bind(post)
        }
    }
    private fun searchRestaurants(resName: resName){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.searchRestaurants(resName =resName) ?:return

        call.enqueue(object : Callback<RestaurantList> {

            override fun onResponse(call: Call<RestaurantList>, response: Response<RestaurantList>) {
                Log.d("retrofit", "음식점 검색 - 응답 성공 / t : ${response.raw()} ${response.body()?.results}")
                val arr= response.body()?.results
                if(arr!= null){
                    binding.restaurantSearchRecyclerView.visibility=View.VISIBLE
                    binding.restaurantSearchRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    binding.restaurantSearchRecyclerView.setHasFixedSize(true)
                    binding.restaurantSearchRecyclerView.adapter = SearchAdapter(arr)
                }
            }
            override fun onFailure(call: Call<RestaurantList>, t: Throwable) {
                Log.d("retrofit", "음식점 검색 - 응답 실패 / t: $t")
                binding.restaurantSearchRecyclerView.visibility=View.GONE

            }
        })
    }
}