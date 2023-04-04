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
import com.example.capstone.ConfirmDialogInterface
import com.example.capstone.CustomDialog
import com.example.capstone.MainActivity
import com.example.capstone.R
import com.example.capstone.databinding.FragmentMyPageBinding
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
        val userId = this.requireActivity().getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE).getString("userId", "00")

        if(isMember){ //로그인 돼있으면
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
            val mainAct = activity as MainActivity
            mainAct.ChangeFragment("Like")
        }
        binding.myUseHistory.setOnClickListener{
            val mainAct = activity as MainActivity
            mainAct.ChangePage(R.id.navigation_history)
        }
        binding.myReviewBox.setOnClickListener{
            val mainAct = activity as MainActivity
            mainAct.ChangeFragment("MyReview")
        }
        binding.myStampBox.setOnClickListener {
            val intent = Intent(activity, StampActivity::class.java)
            startActivity(intent)
        }
        binding.myLogout.setOnClickListener {
            val dialog = CustomDialog(this, "로그아웃 하시겠습니까?", 0, 0)
            dialog.isCancelable = false
            this@MyPageFragment.fragmentManager?.let { it1 -> dialog.show(it1, "ConfirmDialog") }
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
                userInfo.edit().putString("userId", "0").putBoolean("isMember", false).apply() //정보변경
                val mainAct = activity as MainActivity
                mainAct.ChangePage(R.id.navigation_myPage)
            }
        }
    }

}
