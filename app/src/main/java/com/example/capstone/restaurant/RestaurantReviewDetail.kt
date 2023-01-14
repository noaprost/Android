package com.example.capstone.restaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstone.databinding.ActivityRestaurantReviewDetailBinding

class RestaurantReviewDetail : AppCompatActivity() {

    private lateinit var binding: ActivityRestaurantReviewDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityRestaurantReviewDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}