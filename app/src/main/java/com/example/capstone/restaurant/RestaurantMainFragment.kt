package com.example.capstone.restaurant

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.capstone.MainActivity
import com.example.capstone.databinding.FragmentRestaurantMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class RestaurantMainFragment : Fragment() {

    private var _binding: FragmentRestaurantMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestaurantMainBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val mainAct = activity as MainActivity
        //mainAct.HideBottomNavi(true)

        val viewPager: ViewPager2 = binding.pager
        val tabLayout: TabLayout = binding.tabLayout
        val viewpagerFragmentAdapter = ViewPagerAdapter(this)

        // ViewPager2의 adapter 설정
        viewPager.adapter = viewpagerFragmentAdapter

        // ###### TabLayout 과 ViewPager2를 연결
        // 1. 탭메뉴의 이름을 리스트로 생성해둔다.
        val tabTitles = listOf("대기", "매칭", "리뷰", "정보")

        // 2. TabLayout 과 ViewPager2를 연결하고, TabItem 의 메뉴명을 설정한다.
        TabLayoutMediator(tabLayout, viewPager) { tab, position -> tab.text = tabTitles[position] }.attach()

        binding.mainScrollView.run{
            header=binding.headerView
            stickListener = { _ ->
                Log.d("LOGGER_TAG", "stickListener")
            }
            freeListener = { _ ->
                Log.d("LOGGER_TAG", "freeListener")
            }
        }

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavi(false)
        _binding = null
    }
}