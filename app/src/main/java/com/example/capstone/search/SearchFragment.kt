package com.example.capstone.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capstone.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.inputSearchKeyword.setOnClickListener{
            binding.inputSearchKeyword.text.clear()
            // 검색어 입력시 검은 글씨로 입력되도록 변경해주는 부분 필요
        }

        // 사용자 input을 받아 restaurant 목록을 보여주는 함수
        binding.searchBtn.setOnClickListener{
            var searchKeyWord = binding.inputSearchKeyword.text // get user input search keyword
            // 검색어 매칭 하는 부분 추가
        }

        return root
    }
}