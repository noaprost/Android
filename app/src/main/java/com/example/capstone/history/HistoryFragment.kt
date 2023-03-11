package com.example.capstone.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.capstone.R
import com.example.capstone.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment(){

    private var _binding : FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val previousWaitingList = arrayListOf(
            PreviousWaiting("2022-12-09", R.drawable.dummy_restaurant_image, "온리원 파스타 송도점"),
            PreviousWaiting("2022-12-12", R.drawable.dummy_restaurant_image, "온리원 파스타 송도점"),
            PreviousWaiting("2022-12-13", R.drawable.dummy_restaurant_image, "온리원 파스타 송도점"),
            PreviousWaiting("2022-12-22", R.drawable.dummy_restaurant_image, "온리원 파스타 송도점"),
            PreviousWaiting("2022-12-25", R.drawable.dummy_restaurant_image, "온리원 파스타 송도점"),
            PreviousWaiting("2022-12-31", R.drawable.dummy_restaurant_image, "온리원 파스타 송도점"),
            PreviousWaiting("2023-01-08", R.drawable.dummy_restaurant_image, "온리원 파스타 송도점"),
            PreviousWaiting("2023-01-22", R.drawable.dummy_restaurant_image, "온리원 파스타 송도점"),
            PreviousWaiting("2023-02-14", R.drawable.dummy_restaurant_image, "온리원 파스타 송도점"),
        )

        binding.previousWaitingRecyclerView.setHasFixedSize(true)
        binding.previousWaitingRecyclerView.adapter = PreviousWaitingAdapter(previousWaitingList)

        return root
    }

}