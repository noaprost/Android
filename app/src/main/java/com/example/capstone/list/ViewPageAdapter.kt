package com.example.capstone.list

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter (fragment : ListFragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 10
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> KoreanFragment()
            else -> KoreanFragment()
        }
    }
}