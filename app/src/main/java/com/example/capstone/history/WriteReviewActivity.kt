package com.example.capstone.history

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import okhttp3.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.capstone.*
import com.example.capstone.databinding.ActivityWriteReviewBinding
import com.example.capstone.retrofit.API
import com.example.capstone.retrofit.IRetrofit
import com.example.capstone.retrofit.RetrofitClient
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class WriteReviewActivity : AppCompatActivity(), ConfirmDialogInterface {
    private lateinit var binding: ActivityWriteReviewBinding
    private lateinit var reviewImage: Uri
    private var isSatisfied=true
    private lateinit var dialog1:CustomDialog
    private lateinit var userInfo: SharedPreferences
    private lateinit var userId:String
    private lateinit var resId:String
    private lateinit var reviewImagePath:File
    var reviewText=""
    private var keyword:MutableList<String> = mutableListOf("", "", "")

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf<String>(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityWriteReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userInfo = this.getSharedPreferences("userInfo", MODE_PRIVATE)
        userId = this.getSharedPreferences("userInfo", MODE_PRIVATE).getString("userId", "1").toString()
        //resId= intent.getStringExtra("resId").toString() //todo resId 연결
        //binding.writeReviewName.text=intent.getStringExtra("resName").toString()//todo resName 연결
        binding.writeReviewComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                reviewText=s.toString()
                binding.writeReviewLength.text = s.length.toString() + "/400"
            }
        })
        binding.writeReviewImage.setOnClickListener {
            val permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE)
            }
            val intent=Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            activityResult.launch(intent)
        }
        binding.reviewImageBox.setOnClickListener {
            binding.addReviewImage.background=getDrawable(R.drawable.background_round_rectangular)
            binding.reviewImageBox.visibility= GONE
        }
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                binding.radioSatisfied.id -> isSatisfied=true
                binding.radioUnsatisfied.id -> isSatisfied=false
            }
        }

        binding.chipGroup1.setOnCheckedStateChangeListener { _, checkedIds ->
            when(checkedIds[0]){
                binding.writeReviewKeyword1.id->keyword[0]=binding.writeReviewKeyword1.text.toString()
                binding.writeReviewKeyword2.id->keyword[0]=binding.writeReviewKeyword2.text.toString()
                binding.writeReviewKeyword3.id->keyword[0]=binding.writeReviewKeyword3.text.toString()
                binding.writeReviewKeyword4.id->keyword[0]=binding.writeReviewKeyword4.text.toString()
                binding.writeReviewKeyword5.id->keyword[0]=binding.writeReviewKeyword5.text.toString()
                binding.writeReviewKeyword6.id->keyword[0]=binding.writeReviewKeyword6.text.toString()
                binding.writeReviewKeyword7.id->keyword[0]=binding.writeReviewKeyword7.text.toString()
                binding.writeReviewKeyword8.id->keyword[0]=binding.writeReviewKeyword8.text.toString()
                binding.writeReviewKeyword9.id->keyword[0]=binding.writeReviewKeyword9.text.toString()
                binding.writeReviewKeyword10.id->keyword[0]=binding.writeReviewKeyword10.text.toString()
                binding.writeReviewKeyword11.id->keyword[0]=binding.writeReviewKeyword11.text.toString()
                binding.writeReviewKeyword12.id->keyword[0]=binding.writeReviewKeyword12.text.toString()
            }
        }
        binding.chipGroup2.setOnCheckedStateChangeListener { _, checkedIds ->
            when(checkedIds[0]){
                binding.writeReviewKeyword8.id->keyword[1]=binding.writeReviewKeyword8.text.toString()
                binding.writeReviewKeyword9.id->keyword[1]=binding.writeReviewKeyword9.text.toString()
                binding.writeReviewKeyword10.id->keyword[1]=binding.writeReviewKeyword10.text.toString()
                binding.writeReviewKeyword11.id->keyword[1]=binding.writeReviewKeyword11.text.toString()
                binding.writeReviewKeyword12.id->keyword[1]=binding.writeReviewKeyword12.text.toString()
                binding.writeReviewKeyword13.id->keyword[1]=binding.writeReviewKeyword13.text.toString()
                binding.writeReviewKeyword14.id->keyword[1]=binding.writeReviewKeyword14.text.toString()
                binding.writeReviewKeyword15.id->keyword[1]=binding.writeReviewKeyword15.text.toString()
                binding.writeReviewKeyword16.id->keyword[1]=binding.writeReviewKeyword16.text.toString()
            }
        }
        binding.chipGroup3.setOnCheckedStateChangeListener { _, checkedIds ->
            when(checkedIds[0]){
                binding.writeReviewKeyword17.id->keyword[2]=binding.writeReviewKeyword17.text.toString()
                binding.writeReviewKeyword18.id->keyword[2]=binding.writeReviewKeyword18.text.toString()
                binding.writeReviewKeyword19.id->keyword[2]=binding.writeReviewKeyword19.text.toString()
                binding.writeReviewKeyword20.id->keyword[2]=binding.writeReviewKeyword20.text.toString()
                binding.writeReviewKeyword21.id->keyword[2]=binding.writeReviewKeyword21.text.toString()
                binding.writeReviewKeyword22.id->keyword[2]=binding.writeReviewKeyword22.text.toString()
            }
        }

        binding.writeReviewButton.setOnClickListener{
            dialog1 = CustomDialog(this, "리뷰를 등록하시겠습니까?", 0, 0)
            dialog1.isCancelable = false
            dialog1.show(this.supportFragmentManager, "ConfirmDialog")
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
            val file = File(absolutelyPath(uri, this))
            reviewImagePath = file
        }
    }
    private fun absolutelyPath(path: Uri?, context : Context): String { //이미지 절대경로 변환 함수
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()
        val result = c?.getString(index!!)
        return result!!
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onYesButtonClick(num: Int, theme: Int) {
        when(num){
            0 -> {
                if(!binding.radioSatisfied.isChecked && !binding.radioUnsatisfied.isChecked){
                    dialog1.dismiss()
                    binding.radioSatisfied.requestFocus()
                    Toast.makeText(this, "만족 여부를 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
                else if( reviewText.isEmpty()){
                    dialog1.dismiss()
                    binding.writeReviewComment.requestFocus()
                    Toast.makeText(this, "리뷰를 작성해주세요.", Toast.LENGTH_SHORT).show()
                }
                else if(keyword[0]=="" || keyword[1]=="" || keyword[2]=="" ){
                    dialog1.dismiss()
                    binding.textView61.requestFocus()
                    Toast.makeText(this, "키워드를 선택해주세요.", Toast.LENGTH_SHORT).show()
                } else{
                    val current = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val date = current.format(formatter).toString()

                    val requestBody:RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("UserID", userId)
                        .addFormDataPart("ResID", "3") //todo 레스토랑 아이디 연결
                        .addFormDataPart("Rating",binding.ratingBar2.rating.toString())
                        .addFormDataPart("RevTxt", reviewText)
                        .addFormDataPart("RevKeyWord", keyword.toString())
                        .addFormDataPart("RevSatis", isSatisfied.toString())
                        .addFormDataPart("RevRecom", isSatisfied.toString()) //todo 수정
                        .addFormDataPart("RevTime", date)
                        .addFormDataPart("myFile", reviewImagePath.name, RequestBody.create(MultipartBody.FORM,reviewImagePath))
                        .build()
                    writeReview(requestBody)
                }
            }
        }
    }

    private fun writeReview(requestBody: RequestBody){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.writeReview(requestBody) ?:return
        call.enqueue(object : Callback<WriteReview> {

            override fun onResponse(call: Call<WriteReview>, response: Response<WriteReview>) {
                Log.d("retrofit", "리뷰 작성 - 응답 성공 / t : ${response.raw()} ${response.body()}")
                val dialog = CustomDialog(this@WriteReviewActivity, "리뷰 작성을 완료하였습니다", 0, 1)
                dialog.isCancelable = true
                dialog.show(this@WriteReviewActivity.supportFragmentManager, "ConfirmDialog")
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 1000)
            }
            override fun onFailure(call: Call<WriteReview>, t: Throwable) {
                Log.d("retrofit", "리뷰 작성 -  응답 실패 / t: $t")
            }
        })
    }
}