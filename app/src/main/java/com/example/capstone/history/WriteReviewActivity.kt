package com.example.capstone.history

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.databinding.ActivityWriteReviewBinding


class WriteReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityWriteReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.writeReviewComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                binding.writeReviewLength.setText(s.length.toString() + "/400")
            }
        })

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}