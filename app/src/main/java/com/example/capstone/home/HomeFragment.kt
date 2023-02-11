package com.example.capstone.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.R
import com.example.capstone.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 대기 내역이 있는 경우에만 대기 정보 버튼이 보이도록 설정
        var isExistWatingInfo : Boolean = true // 불러온 데이터의 존재여부로 판단되도혹 수정 필요
        binding.watingInfoBtn.visibility = if(isExistWatingInfo == true){
            View.VISIBLE
        }else{
            View.INVISIBLE
        }

        // 대기 정보 버튼을 누를 경우 팝업 연결
        binding.watingInfoBtn.setOnClickListener {
            
        }

        val restaurantList = arrayListOf(
            Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점"),
            Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점"),
            Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점"),
            Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점"),
            Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점"),
            Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점"),
            Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점"),
            Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점"),
            Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점"),
            Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점"),
            Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점"),
            Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점"),
        )

        // 스크롤 오류 수정 필요
        binding.restaurantHomeRecyclerView1.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.restaurantHomeRecyclerView1.setHasFixedSize(true)
        binding.restaurantHomeRecyclerView1.adapter = RestaurantAdapter(restaurantList)

        // 스크롤 오류 수정 필요
        binding.restaurantHomeRecyclerView2.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.restaurantHomeRecyclerView2.setHasFixedSize(true)
        binding.restaurantHomeRecyclerView2.adapter = RestaurantAdapter(restaurantList)

        return root
    }

}
