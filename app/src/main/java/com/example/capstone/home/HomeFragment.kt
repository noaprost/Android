package com.example.capstone.home

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.capstone.R
import com.example.capstone.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // 대기 내역이 있는 경우에만 대기 정보 버튼이 보이도록 설정
        var isExistWatingInfo : Boolean = false // 불러온 데이터의 존재여부로 판단되도혹 수정 필요
        binding.watingInfoBtn.visibility = if(isExistWatingInfo == true){
            View.VISIBLE
        }else{
            View.INVISIBLE
        }

        // 대기 정보 버튼을 누를 경우 팝업 연결
        binding.watingInfoBtn.setOnClickListener {
            
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

}