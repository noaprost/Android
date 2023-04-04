package com.example.capstone.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstone.databinding.ActivityStampBinding

class StampActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStampBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityStampBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }
        binding.stampNum.text //todo 스탬프 개수 입력
    }
}