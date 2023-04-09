package com.example.capstone.restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.capstone.list.KoreanFragment
import com.example.capstone.restaurant.RestaurantMainFragment

class ViewPagerAdapter (fragment : RestaurantMainFragment, bundle: Bundle) : FragmentStateAdapter(fragment){
    val data=bundle
    override fun getItemCount(): Int = 4
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment:Fragment= RestaurantStateFragment()
                fragment.arguments=data
                fragment
            }
            1-> {
                val fragment:Fragment=RestaurantMatchingFragment()
                fragment.arguments=data
                fragment
            }
            2-> {
                val fragment:Fragment= RestaurantReviewFragment()
                fragment.arguments=data
                fragment
            }
            else -> {
                val fragment:Fragment= RestaurantInfoFragment()
                fragment.arguments=data
                fragment
            }
        }
    }
}