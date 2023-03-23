package com.example.capstone.history

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.capstone.ConfirmDialogInterface
import com.example.capstone.CustomDialog
import com.example.capstone.R
import com.example.capstone.databinding.ActivityWriteReviewBinding
import com.example.capstone.mypage.MyPageFragment


class WriteReviewActivity : AppCompatActivity(), ConfirmDialogInterface {
    private lateinit var binding: ActivityWriteReviewBinding
    private lateinit var reviewImage: Uri
    private var isSatisfied=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityWriteReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.writeReviewComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                binding.writeReviewLength.text = s.length.toString() + "/400"
            }
        })
        binding.writeReviewImage.setOnClickListener {
            val intent=Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            activityResult.launch(intent)
        }
        binding.reviewImageBox.setOnClickListener {
            binding.addReviewImage.background=getDrawable(R.drawable.background_round_rectangular)
            binding.reviewImageBox.visibility= GONE
        }
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when(checkedId){
                binding.radioSatisfied.id -> isSatisfied=true
                binding.radioUnsatisfied.id -> isSatisfied=false
            }
        }
        binding.writeReviewButton.setOnClickListener{
            val dialog = CustomDialog(this, "리뷰를 등록하시겠습니까?", 0, 0)
            dialog.isCancelable = false
            dialog.show(this.supportFragmentManager, "ConfirmDialog")
        }
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private val activityResult:ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ it ->
        if(it.resultCode== RESULT_OK && it.data != null){
                val uri = it.data!!.data
                uri!!.also { reviewImage = it }
                Glide.with(this)
                    .load(uri) //이미지
                    .into(binding.addReviewImage)
                binding.reviewImageBox.visibility= View.VISIBLE
                binding.addReviewImage.clipToOutline = true
            }

        }

    override fun onYesButtonClick(num: Int, theme: Int) {
        when(num){
            0 -> {
                this.finish()
                //todo 리뷰 등록코드 작성
                // todo 작성 완료 팝업 작성


            }
        }
    }
}