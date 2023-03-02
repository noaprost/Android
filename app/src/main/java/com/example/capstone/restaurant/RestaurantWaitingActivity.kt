package com.example.capstone.restaurant

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.capstone.ConfirmDialogInterface
import com.example.capstone.CustomDialog
import com.example.capstone.R
import com.example.capstone.databinding.ActivityRestaurantWaitingBinding

class RestaurantWaitingActivity : AppCompatActivity(), ConfirmDialogInterface {
    private lateinit var binding: ActivityRestaurantWaitingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityRestaurantWaitingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var numberOfPeople=2
        var spinnerData=resources.getStringArray(R.array.spinner_array)
        var adapter=ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerData)
        binding.spinner.adapter=adapter

        binding.spinner.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2){
                    0->numberOfPeople=2
                    1->numberOfPeople=3
                    2->numberOfPeople=4
                    3->numberOfPeople=5
                    4->numberOfPeople=6
                    5->numberOfPeople=7
                    6->numberOfPeople=8
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        val textData: String = binding.textView20.text.toString()
        val builder = SpannableStringBuilder(textData)
        builder.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setSpan(ForegroundColorSpan(resources.getColor(R.color.INUYellow)), 7, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.textView20.text = builder
        binding.waitingButton.setOnClickListener {
            val dialog = CustomDialog(this@RestaurantWaitingActivity, "대기를 신청하시겠습니까?", 0, 0)
            dialog.isCancelable = false
            dialog.show(this.supportFragmentManager, "ConfirmDialog")
        }
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    override fun onYesButtonClick(num: Int, theme: Int) {
        when(num){
            0->{
                //todo 대기 신청 연결
                finish()
            }
        }
    }
}