package com.example.capstone.login

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.R
import com.example.capstone.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity(){
    private lateinit var binding : ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 회원가입 화면에서 backBtn을 누를 경우 login page로 돌아감
        binding.joinBackBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
        }

        binding.submitBtn.setOnClickListener {
            val userPhNum = binding.userPhNum.text
            val userPw = binding.userPw.text
            val userPwCheck = binding.userPwCheck.text
            val birthYear = binding.birthYear.text
            val birthMonth = binding.birthMonth.text
            val birthDay = binding.birthDay.text
            val userBirth = "${birthYear}-${birthMonth}-${birthDay}"

            binding.genderCheck.setOnCheckedChangeListener { qroup, checkedId ->
                when(checkedId){
                    R.id.male -> {val male = "M"}
                    R.id.female -> {val female = "F"}
                }
            }

        }
    }
}