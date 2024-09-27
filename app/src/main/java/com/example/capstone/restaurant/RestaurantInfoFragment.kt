package com.example.capstone.restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.capstone.R
import com.example.capstone.Restaurants
import com.example.capstone.databinding.FragmentRestaurantInfoBinding
import com.example.capstone.databinding.FragmentRestaurantMatchingBinding

class RestaurantInfoFragment : Fragment() {
    private var _binding: FragmentRestaurantInfoBinding? = null
    private val binding get() = _binding!!
    lateinit var resInfo:Restaurants

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestaurantInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val bundle = arguments
        resInfo=bundle!!.getSerializable("restaurant") as Restaurants

        binding.location.text=resInfo.resAddress
        if(resInfo.resOpen==null || resInfo.resClose==null){
            binding.open.text="정보가 없습니다."
        }else{
            binding.open.text=resInfo.resOpen+" ~ "+resInfo.resClose
        }
        binding.waitingTime.text=resInfo.resWaitOpen+" ~  "+resInfo.resWaitClose
        binding.phone.text=resInfo.resPhNum
        binding.note.text=resInfo.ResComment
        return root
    }

}