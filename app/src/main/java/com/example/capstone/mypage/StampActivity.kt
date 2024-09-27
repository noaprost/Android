package com.example.capstone.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.ResID
import com.example.capstone.RestaurantReviewList
import com.example.capstone.StampInfo
import com.example.capstone.UserPhone
import com.example.capstone.databinding.ActivityStampBinding
import com.example.capstone.retrofit.API
import com.example.capstone.retrofit.IRetrofit
import com.example.capstone.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StampActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStampBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityStampBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }
        val userPhone = this.getSharedPreferences("userInfo", MODE_PRIVATE).getString("userPhone", "010-1111-1111")

        CheckStamp((UserPhone(userPhone.toString())))
    }
    private fun CheckStamp(UserPhone: UserPhone){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.CheckStamp(UserPhone) ?:return

        call.enqueue(object : Callback<StampInfo> {

            override fun onResponse(call: Call<StampInfo>, response: Response<StampInfo>) {
                Log.d("retrofit", "음식점 리뷰 리스트 - 응답 성공 / t : ${response.raw()} ${response.body()}")
                binding.stampNum.text=response.body()!!.stamp.toString()
            }
            override fun onFailure(call: Call<StampInfo>, t: Throwable) {
                Log.d("retrofit", "음식점 리뷰 리스트 - 응답 실패 / t: $t")
            }
        })
    }
}