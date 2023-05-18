package com.example.capstone

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.capstone.databinding.DialogWaitingLayoutBinding
import com.example.capstone.retrofit.API
import com.example.capstone.retrofit.IRetrofit
import com.example.capstone.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.capstone.home.HomeFragment
import kotlinx.coroutines.withContext
import okhttp3.RequestBody

class WaitingCustomDialog (
    WaitingInfoCheckInterface: WaitingInfoCheckInterface,
    text: String, num: Int, theme:Int
) : DialogFragment(){
    //뷰 바인딩 정의
    private var _binding: DialogWaitingLayoutBinding? = null

    private val binding get() = _binding!!

    private var WaitingInfoCheckInterface: WaitingInfoCheckInterface? = null

    private var text: String? = null
    private var num: Int? = null
    private var theme: Int? = null
    private lateinit var waitingInfo: SharedPreferences

    init {
        this.text = text
        this.num = num
        this.theme=theme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogWaitingLayoutBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.waitingTeamText.text = text

        // 대기 취소 버튼 클릭
        binding.waitingCancelBtn.setOnClickListener {
            waitingInfo = this.requireActivity().getSharedPreferences("waitingInfo", MODE_PRIVATE)
            val waitIndex = this.requireActivity().getSharedPreferences("waitingInfo", AppCompatActivity.MODE_PRIVATE).getString("waitIndex", "43").toString()

            waitingCancel(WaitIndex(waitIndex))
            onDestroyView()
        }

        // 대기 미루기 버튼 클릭
        binding.waitingDelayBtn.setOnClickListener {
            waitingInfo = this.requireActivity().getSharedPreferences("waitingInfo", MODE_PRIVATE)
            val waitIndex = this.requireActivity().getSharedPreferences("waitingInfo", AppCompatActivity.MODE_PRIVATE).getString("WaitIndex", "43").toString()
            val resPhNum = this.requireActivity().getSharedPreferences("waitingInfo", AppCompatActivity.MODE_PRIVATE).getString("resPhNum", "032 525 3745")

            waitingDelay(ResDelayInfo("43", "032 525 3745"))
            onDestroyView()

        }

        if(this.theme==0){
            binding.waitingCancelBtn.visibility = View.VISIBLE
            binding.waitingDelayBtn.visibility = View.VISIBLE
        }else{
            binding.waitingCancelBtn.visibility = View.GONE
            binding.waitingDelayBtn.visibility = View.GONE
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface WaitingInfoCheckInterface {

}

// 대기 취소 레트로핏 연결
private fun waitingCancel(WaitIndex : WaitIndex){
    val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create((IRetrofit::class.java))
    val call = iRetrofit?.waitingCancel(WaitIndex) ?:return

    call.enqueue(object : Callback<ResWaitCancel> {
        override fun onResponse(call: Call<ResWaitCancel>, response: Response<ResWaitCancel>) {
            Log.d("retrofit", "대기 취소 - 응답 성공 / t : ${response.raw()} ${response.body()}")
        }

        override fun onFailure(call: Call<ResWaitCancel>, t: Throwable) {
            Log.d("retrofit", "대기 취소 - 응답 실패 / t: $t")
        }
    })
}

// 대기 미루기 레트로핏 연결
private fun waitingDelay(ResDelayInfo: ResDelayInfo){
    val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create((IRetrofit::class.java))
    val call = iRetrofit?.waitingDelay(ResDelayInfo) ?:return

    call.enqueue(object : Callback<ResWaitDelay>{
        override fun onResponse(call: Call<ResWaitDelay>, response: Response<ResWaitDelay>) {
            Log.d("retrofit", "대기 미루기 - 응답 성공 / t : ${response.raw()} ${response.body()}")
        }

        override fun onFailure(call: Call<ResWaitDelay>, t: Throwable) {
            Log.d("retrofit", "대기 미루기 - 응답 실패 / t: $t")
        }
    })
}
