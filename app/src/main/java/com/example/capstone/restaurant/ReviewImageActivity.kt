package com.example.capstone.restaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.capstone.R
import com.example.capstone.databinding.ActivityRestaurantWaitingBinding
import com.example.capstone.databinding.ActivityReviewImageBinding
import com.example.capstone.databinding.ActivityStampBinding
import com.example.capstone.resPhNum
import com.example.capstone.retrofit.API

class ReviewImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityReviewImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButton.setOnClickListener { finish() }
        val url = intent.getStringExtra("url").toString()
        if(url!=null){
            Glide.with(this@ReviewImageActivity)
                .load(url) // 불러올 이미지 url
                .error(R.drawable.onlyone_logo) // 로딩 에러 발생 시 표시할 이미지
                .fallback(R.drawable.onlyone_logo) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                .into(binding.imageView31) // 이미지를 넣을 뷰
        }
    }
}