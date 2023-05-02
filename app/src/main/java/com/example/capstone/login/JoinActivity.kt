package com.example.capstone.login

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity(){
    private lateinit var binding : ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.joinBackBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
        }
    }
}