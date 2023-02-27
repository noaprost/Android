package com.example.capstone.history

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.capstone.ConfirmDialogInterface
import com.example.capstone.CustomDialog
import com.example.capstone.databinding.ActivityWriteReviewBinding


class WriteReviewActivity : AppCompatActivity(), ConfirmDialogInterface {
    private lateinit var binding: ActivityWriteReviewBinding
    private lateinit var reviewImage: Uri

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
        binding.writeReviewImage.setOnClickListener {
            val intent=Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            activityResult.launch(intent)
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
        ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode== RESULT_OK && it.data != null){
                val uri = it.data!!.data
                uri!!.also { reviewImage = it }
                Glide.with(this)
                    .load(uri) //이미지
                    .into(binding.writeReviewImage)
            }

        }

    override fun onYesButtonClick(num: Int, theme: Int) {
        when(num){
            0 -> {
                finish()
                //todo 리뷰 등록코드 작성
            }
        }
    }
}