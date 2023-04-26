package com.example.capstone.mypage

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstone.ConfirmDialogInterface
import com.example.capstone.CustomDialog
import com.example.capstone.MainActivity
import com.example.capstone.R
import com.example.capstone.databinding.ActivityEditInfoBinding

class EditInfoActivity : AppCompatActivity(), ConfirmDialogInterface {
    private lateinit var binding: ActivityEditInfoBinding
    lateinit var userInfo: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityEditInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userInfo = this.getSharedPreferences("userInfo", MODE_PRIVATE)
        val userId = this.getSharedPreferences("userInfo", MODE_PRIVATE).getString("userId", "00")

        binding.myLogout.setOnClickListener {
            val dialog = CustomDialog(this, "로그아웃 하시겠습니까?", 0, 0)
            dialog.isCancelable = false
            dialog.show(this.supportFragmentManager,"ConfirmDialog")
        }
        binding.myDeleteAccount.setOnClickListener {
            val intent = Intent(this, DeleteAccountActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onYesButtonClick(num: Int, theme: Int) {
        when(num){
            0->{//로그아웃
                userInfo.edit().putString("userId", "0").putBoolean("isMember", false).apply() //정보변경
                val mainAct = MainActivity()
                mainAct.ChangePage(R.id.navigation_myPage)
            }
        }
    }
}