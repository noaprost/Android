package com.example.capstone.history

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.Rating
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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.capstone.*
import com.example.capstone.databinding.ActivityWriteReviewBinding
import com.example.capstone.retrofit.API.BASE_URL
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import okhttp3.OkHttpClient
import java.io.IOException


class WriteReviewActivity : AppCompatActivity(), ConfirmDialogInterface {
    private lateinit var binding: ActivityWriteReviewBinding
    private lateinit var reviewImage: Uri
    private var isSatisfied=true
    private lateinit var dialog1:CustomDialog
    private lateinit var userInfo: SharedPreferences
    private lateinit var userId:String
    private lateinit var resId:String
    var reviewImagePath: File? = null
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
        userId = userInfo.getString("userId", "0").toString()
        resId= intent.getStringExtra("resId").toString()
        binding.writeReviewName.text=intent.getStringExtra("resName").toString()

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
            if(checkedIds.size!=0){
                when(checkedIds[0]){
                    binding.writeReviewKeyword1.id->keyword[0]="\""+binding.writeReviewKeyword1.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword2.id->keyword[0]="\""+binding.writeReviewKeyword2.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword3.id->keyword[0]="\""+binding.writeReviewKeyword3.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword4.id->keyword[0]="\""+binding.writeReviewKeyword4.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword5.id->keyword[0]="\""+binding.writeReviewKeyword5.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword6.id->keyword[0]="\""+binding.writeReviewKeyword6.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword7.id->keyword[0]="\""+binding.writeReviewKeyword7.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword8.id->keyword[0]="\""+binding.writeReviewKeyword8.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword9.id->keyword[0]="\""+binding.writeReviewKeyword9.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword10.id->keyword[0]="\""+binding.writeReviewKeyword10.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword11.id->keyword[0]="\""+binding.writeReviewKeyword11.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword12.id->keyword[0]="\""+binding.writeReviewKeyword12.text.toString().replace("#","")+"\""
                }
            }

        }
        binding.chipGroup2.setOnCheckedStateChangeListener { _, checkedIds ->
            if(checkedIds.size!=0){
                when(checkedIds[0]){
                    binding.writeReviewKeyword8.id->keyword[1]="\""+binding.writeReviewKeyword8.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword9.id->keyword[1]="\""+binding.writeReviewKeyword9.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword10.id->keyword[1]="\""+binding.writeReviewKeyword10.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword11.id->keyword[1]="\""+binding.writeReviewKeyword11.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword12.id->keyword[1]="\""+binding.writeReviewKeyword12.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword13.id->keyword[1]="\""+binding.writeReviewKeyword13.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword14.id->keyword[1]="\""+binding.writeReviewKeyword14.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword15.id->keyword[1]="\""+binding.writeReviewKeyword15.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword16.id->keyword[1]="\""+binding.writeReviewKeyword16.text.toString().replace("#","")+"\""
                }
            }
        }
        binding.chipGroup3.setOnCheckedStateChangeListener { _, checkedIds ->
            if(checkedIds.size!=0){
                when(checkedIds[0]){
                    binding.writeReviewKeyword17.id->keyword[2]="\""+binding.writeReviewKeyword17.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword18.id->keyword[2]="\""+binding.writeReviewKeyword18.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword19.id->keyword[2]="\""+binding.writeReviewKeyword19.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword20.id->keyword[2]="\""+binding.writeReviewKeyword20.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword21.id->keyword[2]="\""+binding.writeReviewKeyword21.text.toString().replace("#","")+"\""
                    binding.writeReviewKeyword22.id->keyword[2]="\""+binding.writeReviewKeyword22.text.toString().replace("#","")+"\""
                }
            }

        }

        binding.writeReviewButton.setOnClickListener{
            Log.d("retrofit", keyword.toString())
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

                    if(reviewImagePath==null){
                        val requestBody:RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart("UserID", userId)
                            .addFormDataPart("ResID", resId)
                            .addFormDataPart("Rating",binding.ratingBar2.rating.toString())
                            .addFormDataPart("RevTxt", reviewText)
                            .addFormDataPart("RevKeyWord", keyword.toString())
                            .addFormDataPart("RevSatis", isSatisfied.toString())
                            .addFormDataPart("RevRecom", isSatisfied.toString())
                            .addFormDataPart("RevTime", date)
                            .build()
                        uploadReview(requestBody)
                    }else{
                        val requestBody:RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart("UserID", userId)
                            .addFormDataPart("ResID", resId)
                            .addFormDataPart("Rating",binding.ratingBar2.rating.toString())
                            .addFormDataPart("RevTxt", reviewText)
                            .addFormDataPart("RevKeyWord", keyword.toString())
                            .addFormDataPart("RevSatis", isSatisfied.toString())
                            .addFormDataPart("RevRecom", isSatisfied.toString())
                            .addFormDataPart("RevTime", date)
                            .addFormDataPart("myFile", reviewImagePath!!.name, RequestBody.create(MultipartBody.FORM, reviewImagePath!!))
                            .build()
                        uploadReview(requestBody)
                    }


                }
            }
        }
    }

    private fun uploadReview(requestBody:RequestBody) {
        val client = OkHttpClient()


        val request = Request.Builder()
            .url(BASE_URL)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                // 실패 처리 및 결과 출력
                runOnUiThread {
                    Log.d("retrofit fail","업로드 실패: ${e.message}")
                    Toast.makeText(this@WriteReviewActivity, "[오류 발생]\n잠시 후 다시 시도해주세요.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                // 성공 처리
                val responseData = response.body?.string()
                // 결과를 처리하고 UI 업데이트 등을 수행
                runOnUiThread {
                    try {
                        val jsonObject = JSONObject(responseData!!)
                        Log.d("retrofit", jsonObject.toString())
                        val userID = jsonObject.getString("UserID")
                        val resID = jsonObject.getString("ResID")
                        val rating = jsonObject.getDouble("Rating")
                        val revTxt = jsonObject.getString("RevTxt")
                        val revKeyWordArray = JSONArray(jsonObject.getString("RevKeyWord"))
                        val revKeyWord = mutableListOf<String>()
                        for (i in 0 until revKeyWordArray.length()) {
                            revKeyWord.add(revKeyWordArray.getString(i))
                        }
                        val revSatis = jsonObject.getBoolean("RevSatis")
                        val revRecom = jsonObject.getBoolean("RevRecom")
                        val revTime = jsonObject.getString("RevTime")
                        val imgPath = jsonObject.getString("ImgPath")
                        val message = jsonObject.getString("message")

                        Log.d("retrofit", "UserID: $userID\nResID: $resID\nRating: $rating\nRevTxt: $revTxt\nRevKeyWord: $revKeyWord\nRevSatis: $revSatis\nRevRecom: $revRecom\nRevTime: $revTime\nImgPath: $imgPath\nMessage: $message")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.d("retrofit", "JSON 파싱 오류: ${e.message}")
                        println(e.message)
                    }
                }
                val dialog = CustomDialog(this@WriteReviewActivity, "리뷰 작성을 완료하였습니다", 0, 1)
                dialog.isCancelable = true
                dialog.show(this@WriteReviewActivity.supportFragmentManager, "ConfirmDialog")
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 1000)

            }
        })
    }

}