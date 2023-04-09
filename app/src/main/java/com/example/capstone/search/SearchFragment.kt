package com.example.capstone.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var searchRestaurantList = ArrayList<SearchRestaurant>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.inputSearchKeyword.setOnClickListener{
            binding.inputSearchKeyword.setText("")
            // 검색어 입력시 검은 글씨로 입력되도록 변경해주는 부분 필요
        }

        // 사용자 input을 받아 restaurant 목록을 보여주는 함수
        binding.searchBtn.setOnClickListener{
            var searchKeyWord = binding.inputSearchKeyword.text // get user input search keyword
            // 검색어 매칭 하는 부분 추가
        }

        // 위치 기반 추천 음식점 들이 보여질 부분
        searchRestaurantList.apply {
            add(
                SearchRestaurant(R.drawable.dummy_restaurant_image, 7, 4.9, 22, "#분위기좋은", "#연인끼리", "인스타맛집")
                )

            add(
                SearchRestaurant(R.drawable.dummy_restaurant_image, 7, 4.9, 22, "#분위기좋은", "#연인끼리", "인스타맛집")
                )

            add(
                SearchRestaurant(R.drawable.dummy_restaurant_image, 7, 4.9, 22, "#분위기좋은", "#연인끼리", "인스타맛집")
                )

            add(
                SearchRestaurant(R.drawable.dummy_restaurant_image, 7, 4.9, 22, "#분위기좋은", "#연인끼리", "인스타맛집")
                )

            add(
                SearchRestaurant(R.drawable.dummy_restaurant_image, 7, 4.9, 22, "#분위기좋은", "#연인끼리", "인스타맛집")
                )

            add(
                SearchRestaurant(R.drawable.dummy_restaurant_image, 7, 4.9, 22, "#분위기좋은", "#연인끼리", "인스타맛집")
                )

            add(
                SearchRestaurant(R.drawable.dummy_restaurant_image, 7, 4.9, 22, "#분위기좋은", "#연인끼리", "인스타맛집")
            )
        }

        binding.restaurantSearchRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.restaurantSearchRecyclerView.setHasFixedSize(true)
        binding.restaurantSearchRecyclerView.adapter = SearchAdapter(searchRestaurantList)

        return root
    }

    inner class SearchViewHolder(view : View): RecyclerView.ViewHolder(view){
        private lateinit var searchRestaurantItem: SearchRestaurant

        fun bind(searchRestaurantItem : SearchRestaurant){
            this.searchRestaurantItem = searchRestaurantItem

            itemView.setOnClickListener {

            }
        }
    }

    inner class SearchAdapter(private val searchList:List<SearchRestaurant>): RecyclerView.Adapter<SearchViewHolder>() {
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
}