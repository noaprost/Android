package com.example.capstone.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.capstone.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var userInfo: SharedPreferences
    private var isAccepted=true //로그인이 유효한가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //todo 레트로핏 연결해서 로직 처리 필요
        userInfo = this.getSharedPreferences("userInfo", MODE_PRIVATE)

        // 전화번호 입력시 자동 하이픈 추가
        binding.id.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        binding.button6.setOnClickListener {
            if(isAccepted){// 로그인이 유효하면
                userInfo.edit().putBoolean("isMember", true).apply()
                userInfo.edit().putString("userId", "2").apply()
                userInfo.edit().putString("userPassword", "pw1234").apply()
                userInfo.edit().putString("userPhoneNum", "010-1111-1111").apply()
                userInfo.edit().putString("userNickname", "김남준").apply()
                userInfo.edit().putString("userLocation", "").apply()
                userInfo.edit().putString("WaitIndex", "").apply()
                //인텐트 종료하면서 정보 전달
                intent.putExtra("isSignedIn",true )
                setResult(RESULT_OK, intent)
                finish()
            }else{
                userInfo.edit().putBoolean("isMember", false).apply()
                userInfo.edit().putString("userId", "").apply()
                userInfo.edit().putString("userPassword", "").apply()
                userInfo.edit().putString("userLocation", "").apply()
                userInfo.edit().putString("userPhoneNum", "").apply()
                userInfo.edit().putString("userNickname", "").apply()
                userInfo.edit().putString("WaitIndex", "").apply()
                //팝업 필요
            }
        }

        binding.joinBtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }
    }

}