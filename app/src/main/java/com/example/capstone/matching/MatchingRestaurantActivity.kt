package com.example.capstone.matching

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.databinding.ActivityMatchingRestaurantBinding

class MatchingRestaurantActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMatchingRestaurantBinding

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
    }
}