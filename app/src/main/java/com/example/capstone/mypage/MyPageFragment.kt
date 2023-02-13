package com.example.capstone.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.capstone.MainActivity
import com.example.capstone.R
import com.example.capstone.databinding.FragmentMyPageBinding
import com.example.capstone.restaurant.RestaurantReviewDetail


class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.myLikeBox.setOnClickListener{
            //val mainAct = activity as MainActivity
            //mainAct.ChangePage(R.id.navigation_like)
        }
        binding.myUseHistory.setOnClickListener{
            val mainAct = activity as MainActivity
            mainAct.ChangePage(R.id.navigation_history)
        }
        binding.myCommentBox.setOnClickListener{
            val intent = Intent(activity, MyReviewActivity::class.java)
            startActivity(intent)
        }
        return root

    }

}