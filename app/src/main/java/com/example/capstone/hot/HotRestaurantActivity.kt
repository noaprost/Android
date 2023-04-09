package com.example.capstone.hot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.databinding.ActivityHotRestaurantBinding

class HotRestaurantActivity : AppCompatActivity(){
    private lateinit var binding: ActivityHotRestaurantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotRestaurantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.hotBackBtn.setOnClickListener {
            finish()
        }

        binding.hotCategoryBtn.setOnClickListener {
            // 카테고리별로 필터 적용되는 기능 추가
        }
    }


}