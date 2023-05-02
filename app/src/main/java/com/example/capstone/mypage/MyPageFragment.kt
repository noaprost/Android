package com.example.capstone.mypage

import android.app.Activity.RESULT_OK
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.*
import com.example.capstone.databinding.FragmentMyPageBinding
import com.example.capstone.history.WriteReviewActivity
import com.example.capstone.login.LoginActivity


class MyPageFragment : Fragment(), ConfirmDialogInterface {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!
    lateinit var userInfo: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userInfo = this.requireActivity().getSharedPreferences("userInfo", MODE_PRIVATE)
        val isMember = this.requireActivity().getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE).getBoolean("isMember", false)
        val userNickname = this.requireActivity().getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE).getString("userNickname", "")

        if(isMember){ //로그인 돼있으면
            binding.myName.text=userNickname
            binding.myBox1.visibility=View.VISIBLE
            binding.goSignIn.visibility=View.GONE
            binding.myBox2.visibility=View.VISIBLE
            binding.LinearLayout.visibility=View.VISIBLE
        }else{
            binding.myBox1.visibility=View.VISIBLE
            binding.goSignIn.visibility=View.VISIBLE
            binding.myBox2.visibility=View.GONE
            binding.LinearLayout.visibility=View.GONE
        }
        binding.goSignIn.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivityForResult(intent, 1)
        }
        binding.myLikeBox.setOnClickListener{
            val bundle=Bundle()
            val mainAct = activity as MainActivity
            mainAct.ChangeFragment("Like", bundle)
        }
        binding.myUseHistory.setOnClickListener{
            val mainAct = activity as MainActivity
            mainAct.ChangePage(R.id.navigation_history)
        }
        binding.myReviewBox.setOnClickListener{
            val bundle=Bundle()
            val mainAct = activity as MainActivity
            mainAct.ChangeFragment("MyReview", bundle)
        }
        binding.myAnnounce.setOnClickListener {
            //todo 알림 페이지로 연결
            val intent = Intent(activity, WriteReviewActivity::class.java)
            startActivity(intent)
        }
        binding.myStampBox.setOnClickListener {
            val intent = Intent(activity, StampActivity::class.java)
            startActivity(intent)
        }
        binding.myLogout.setOnClickListener {
            userInfo.edit().putBoolean("isMember", false).apply()
            userInfo.edit().putString("userId", "01012345678").apply()
            userInfo.edit().putString("userPassword", "0000").apply()
            userInfo.edit().putString("userLocation", "").apply()
            val mainact = activity as MainActivity
            mainact.ChangePage(R.id.navigation_home)
        }
        binding.myEdit.setOnClickListener {
            val intent = Intent(activity, EditInfoActivity::class.java)
            startActivity(intent)
        }
        return root

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1 && resultCode == RESULT_OK) {
            var isSignedIn = data!!.getBooleanExtra("isSignedIn", false)
            if (isSignedIn) {
                val mainAct = activity as MainActivity
                mainAct.ChangePage(R.id.navigation_myPage)
            }
        }
    }
    override fun onYesButtonClick(num: Int, theme: Int) {
        when(num){
            0->{//로그아웃
                //todo 로그아웃 api 연결
                userInfo.edit().putString("userId", "0").putBoolean("isMember", false).apply() //정보변경
                val mainAct = activity as MainActivity
                mainAct.ChangePage(R.id.navigation_myPage)
            }
        }
    }

}
