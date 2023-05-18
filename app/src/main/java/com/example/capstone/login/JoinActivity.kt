package com.example.capstone.login

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.capstone.CheckedInfo
import com.example.capstone.R
import com.example.capstone.UserPhone
import com.example.capstone.databinding.ActivityJoinBinding
import com.example.capstone.retrofit.API
import com.example.capstone.retrofit.IRetrofit
import com.example.capstone.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Array

class JoinActivity : AppCompatActivity(){
    private lateinit var binding : ActivityJoinBinding
    private var userKeyword : MutableList<String> = mutableListOf("", "", "")
    private var checked = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 회원가입 화면에서 backBtn을 누를 경우 login page로 돌아감
        binding.joinBackBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // 전화번호 입력시 자동 하이픈 추가
        binding.userPhNum.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        val userPhNum = binding.userPhNum.text.toString()
        val userPw = binding.userPw.text.toString()
        val userPwCheck = binding.userPwCheck.text.toString()
        val birthYear = binding.birthYear.text
        val birthMonth = binding.birthMonth.text
        val birthDay = binding.birthDay.text
        val userBirth = "${birthYear}-${birthMonth}-${birthDay}"

        binding.genderCheck.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.male -> {
                    val male = "M"
                }

                R.id.female -> {
                    val female = "F"
                }
            }
        }

        val userName = binding.userName.text.toString()

        binding.userChipGroup1.setOnCheckedStateChangeListener { _, checkedIds ->
            if(checkedIds.size!=0){
                when(checkedIds[0]){
                    binding.userKeyword1.id -> userKeyword[0] = binding.userKeyword1.text.toString().replace("#","")
                    binding.userKeyword2.id -> userKeyword[0] = binding.userKeyword2.text.toString().replace("#","")
                    binding.userKeyword3.id -> userKeyword[0] = binding.userKeyword3.text.toString().replace("#","")
                    binding.userKeyword4.id -> userKeyword[0] = binding.userKeyword4.text.toString().replace("#","")
                    binding.userKeyword5.id -> userKeyword[0] = binding.userKeyword5.text.toString().replace("#","")
                    binding.userKeyword6.id -> userKeyword[0] = binding.userKeyword6.text.toString().replace("#","")
                    binding.userKeyword7.id -> userKeyword[0] = binding.userKeyword7.text.toString().replace("#","")
                }
            }
        }

        binding.userChipGroup2.setOnCheckedStateChangeListener { _, checkedIds ->
            if(checkedIds.size!=0){
                when(checkedIds[0]){
                    binding.userKeyword8.id -> userKeyword[1] = binding.userKeyword8.text.toString().replace("#","")
                    binding.userKeyword9.id -> userKeyword[1] = binding.userKeyword9.text.toString().replace("#","")
                    binding.userKeyword10.id -> userKeyword[1] = binding.userKeyword10.text.toString().replace("#","")
                    binding.userKeyword11.id -> userKeyword[1] = binding.userKeyword11.text.toString().replace("#","")
                    binding.userKeyword12.id -> userKeyword[1] = binding.userKeyword12.text.toString().replace("#","")
                    binding.userKeyword13.id -> userKeyword[1] = binding.userKeyword13.text.toString().replace("#","")
                    binding.userKeyword14.id -> userKeyword[1] = binding.userKeyword14.text.toString().replace("#","")
                    binding.userKeyword15.id -> userKeyword[1] = binding.userKeyword15.text.toString().replace("#","")
                    binding.userKeyword16.id -> userKeyword[1] = binding.userKeyword16.text.toString().replace("#","")
                }
            }
        }

        binding.userChipGroup3.setOnCheckedStateChangeListener { _, checkedIds ->
            if(checkedIds.size!=0){
                when(checkedIds[0]){
                    binding.userKeyword17.id -> userKeyword[2] = binding.userKeyword17.text.toString().replace("#","")
                    binding.userKeyword18.id -> userKeyword[2] = binding.userKeyword18.text.toString().replace("#","")
                    binding.userKeyword19.id -> userKeyword[2] = binding.userKeyword19.text.toString().replace("#","")
                    binding.userKeyword20.id -> userKeyword[2] = binding.userKeyword20.text.toString().replace("#","")
                    binding.userKeyword21.id -> userKeyword[2] = binding.userKeyword21.text.toString().replace("#","")
                    binding.userKeyword22.id -> userKeyword[2] = binding.userKeyword22.text.toString().replace("#","")
                }
            }
        }


        // 완료 버튼을 누를 경우 회원가입 api에 정보 전송하도록
        // 유효성 검사 부분 따로 빼서 함수로 만들어야함
        binding.submitBtn.setOnClickListener {

            if(userPhNum.isNullOrEmpty()){
                Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
            }

            if(userPw.isNullOrEmpty()){
                Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            }

            if(userPwCheck.isNullOrEmpty()){
                Toast.makeText(this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
            }

            if(birthYear.isNullOrEmpty()){
                Toast.makeText(this, "생년월일을 입력해주세요", Toast.LENGTH_SHORT).show()
            }

            if(birthMonth.isNullOrEmpty()){
                Toast.makeText(this, "생년월일을 입력해주세요", Toast.LENGTH_SHORT).show()
            }

            if(birthDay.isNullOrEmpty()){
                Toast.makeText(this, "생년월일을 입력해주세요", Toast.LENGTH_SHORT).show()
            }

            if(userName.isNullOrEmpty()){
                Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show()
            }

            if(userKeyword[0] == "" || userKeyword[1] == "" || userKeyword[2] == ""){
                Toast.makeText(this, "키워드를 입력해주세요", Toast.LENGTH_SHORT).show()
            }

            if(userPw != userPwCheck){
                Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                binding.userPwCheck.text = null
            }


        }
    }

    private fun checkId(userPhone: UserPhone){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.checkId(userPhone) ?:return

        call.enqueue(object : Callback<CheckedInfo> {
            override fun onResponse(call: Call<CheckedInfo>, response: Response<CheckedInfo>) {
                Log.d("retrofit", "아이디 중복 체크 - 성공 / t : ${response.raw()} ${response.body()}")
                if(response.body()!!.result){
                    Toast.makeText(this@JoinActivity, "중복된 아이디가 존재합니다", Toast.LENGTH_SHORT)
                    binding.userPhNum.text = null
                    checked = false
                }else{
                    Toast.makeText(this@JoinActivity, "사용 가능한 아이디입니다", Toast.LENGTH_SHORT)
                    checked = true
                }
            }

            override fun onFailure(call: Call<CheckedInfo>, t: Throwable) {
                Log.d("retrofit", "아이디 중복 체크 - 실패 / t")
            }
        })
    }
}