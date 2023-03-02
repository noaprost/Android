package com.example.capstone.mypage

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.example.capstone.ConfirmDialogInterface
import com.example.capstone.CustomDialog
import com.example.capstone.MainActivity
import com.example.capstone.R
import com.example.capstone.databinding.FragmentMyPageBinding
import com.example.capstone.history.WriteReviewActivity


class MyPageFragment : Fragment(), ConfirmDialogInterface {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        val root: View = binding.root

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
        binding.myEditBox.setOnClickListener {
            val intent = Intent(activity, WriteReviewActivity::class.java)
            startActivity(intent)
        }
        binding.myLogout.setOnClickListener {
            val dialog = CustomDialog(this, "로그아웃 하시겠습니까?", 0, 0)
            dialog.isCancelable = false
            this@MyPageFragment.fragmentManager?.let { it1 -> dialog.show(it1, "ConfirmDialog") }
        }
        return root

    }

    override fun onYesButtonClick(num: Int, theme: Int) {
        when(num){
            //todo 로그아웃 연결
        }
    }

}