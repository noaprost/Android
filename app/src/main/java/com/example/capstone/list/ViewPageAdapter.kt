package com.example.capstone.list

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.capstone.mypage.MyReviewFragment


class ViewPagerAdapter (fragment : ListFragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 7
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> {
                val bundle= Bundle()
                bundle.putString("category", "한식")
                val fragment:Fragment= KoreanFragment()
                fragment.arguments=bundle
                fragment
            }
            2 -> {
                val bundle= Bundle()
                bundle.putString("category", "양식")
                val fragment:Fragment= KoreanFragment()
                fragment.arguments=bundle
                fragment
            }
            3 -> {
                val bundle= Bundle()
                bundle.putString("category", "중식")
                val fragment:Fragment= KoreanFragment()
                fragment.arguments=bundle
                fragment
            }
            4 -> {
                val bundle= Bundle()
                bundle.putString("category", "일식")
                val fragment:Fragment= KoreanFragment()
                fragment.arguments=bundle
                fragment
            }
            5 -> {
                val bundle= Bundle()
                bundle.putString("category", "카페/베이커리")
                val fragment:Fragment= KoreanFragment()
                fragment.arguments=bundle
                fragment
            }
            6 -> {
                val bundle= Bundle()
                bundle.putString("category", "주점")
                val fragment:Fragment= KoreanFragment()
                fragment.arguments=bundle
                fragment
            }
            else -> {
                val bundle= Bundle()
                bundle.putString("category", "전체")
                val fragment:Fragment= KoreanFragment()
                fragment.arguments=bundle
                fragment
            }
        }
    }
}