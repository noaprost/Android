package com.example.capstone

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.capstone.databinding.DialogWaitingLayoutBinding

class WaitingCustomDialog (
    WaitingInfoCheckInterface: WaitingInfoCheckInterface,
    text: String, num: Int, theme:Int
) : DialogFragment(){
    //뷰 바인딩 정의
    private var _binding: DialogWaitingLayoutBinding? = null

    private val binding get() = _binding!!

    private var WaitingInfoCheckInterface: WaitingInfoCheckInterface? = null

    private var text: String? = null
    private var num: Int? = null
    private var theme: Int? = null

    init {
        this.text = text
        this.num = num
        this.theme=theme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogWaitingLayoutBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.waitingTeamText.text = text

        // 대기 취소 버튼 클릭
        binding.waitingCancelBtn.setOnClickListener {

        }

        // 대기 미루기 버튼 클릭
        binding.waitingPstBtn.setOnClickListener {

        }

        if(this.theme==0){
            binding.waitingCancelBtn.visibility = View.VISIBLE
            binding.waitingPstBtn.visibility = View.VISIBLE
        }else{
            binding.waitingCancelBtn.visibility = View.GONE
            binding.waitingPstBtn.visibility = View.GONE
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface WaitingInfoCheckInterface {

}