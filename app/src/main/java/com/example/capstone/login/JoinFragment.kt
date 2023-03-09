package com.example.capstone.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capstone.databinding.FragmentJoinBinding

class JoinFragment : Fragment(){
    private var _binding : FragmentJoinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentJoinBinding.inflate(inflater, container, false)
        val root : View = binding.root

        binding.joinBackBtn.setOnClickListener {
            // 로그인 페이지로 이동
        }

        return root
    }
}