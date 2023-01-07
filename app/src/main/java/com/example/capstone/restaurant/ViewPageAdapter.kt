package com.example.capstone.restaurant

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.capstone.restaurant.RestaurantMainFragment

class ViewPagerAdapter (fragment : RestaurantMainFragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 4
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RestaurantStateFragment()
            1-> RestaurantMatchingFragment()
            2-> RestaurantReviewFragment()
            else -> RestaurantInfoFragment()
        }
    }
}