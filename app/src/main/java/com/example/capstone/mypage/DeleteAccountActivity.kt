package com.example.capstone.mypage

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.example.capstone.AddWaiting
import com.example.capstone.ConfirmDialogInterface
import com.example.capstone.CustomDialog
import com.example.capstone.UserIdPassword
import com.example.capstone.databinding.ActivityDeleteAccountBinding
import com.example.capstone.retrofit.API
import com.example.capstone.retrofit.IRetrofit
import com.example.capstone.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Response

class DeleteAccountActivity : AppCompatActivity(), ConfirmDialogInterface {
    private lateinit var binding: ActivityDeleteAccountBinding
    lateinit var userInfo: SharedPreferences
    var phoneNum=""
    var phone1="010"
    var phone2=""
    var phone3=""
    var password=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityDeleteAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userInfo = this.getSharedPreferences("userInfo", MODE_PRIVATE)
        val userId = this.getSharedPreferences("userInfo", MODE_PRIVATE).getString("userPhoneNum", "01012345678")
        val userPassword = this.getSharedPreferences("userInfo", MODE_PRIVATE).getString("userPassword", "0000")
        binding.backButton.setOnClickListener { finish() }

        binding.editTextPhone1.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                phone1= binding.editTextPhone1.text.toString()
                if(p0!!.length==4) binding.editTextPhone2?.requestFocus()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.editTextPhone2.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                phone2= binding.editTextPhone2.text.toString()

            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.editTextPhone3.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                phone3= binding.editTextPhone3.text.toString()
                if(p0!!.length==3) binding.editTextPhone1?.requestFocus()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.deletePassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                password=s.toString()
            }
        })
        binding.button5.setOnClickListener {
            phoneNum="${phone3}-${phone1}-${phone2}"
            if(userId==phoneNum && userPassword==password){
                deleteAccount(UserIdPassword(phoneNum, password))
            }else{
                val dialog = CustomDialog(this, "정보가 일치하지 않습니다.", 0, 1)
                dialog.isCancelable = true
                dialog.show(this.supportFragmentManager,"ConfirmDialog")
            }
        }
    }

    override fun onYesButtonClick(num: Int, theme: Int) {
    }
    private fun deleteAccount(userIdPassword: UserIdPassword){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.deleteAccount(userIdPassword) ?:return

        call.enqueue(object : retrofit2.Callback<UserIdPassword> {

            override fun onResponse(call: Call<UserIdPassword>, response: Response<UserIdPassword>) {
                Log.d("retrofit", "탈퇴 - 응답 성공 / t : ${response.raw()} ${response.body()}")

            }
            override fun onFailure(call: Call<UserIdPassword>, t: Throwable) {
                Log.d("retrofit", "탈퇴 - 응답 실패 / t: $t")
            }
        })
    }
}