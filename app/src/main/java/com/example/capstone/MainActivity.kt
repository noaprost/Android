package com.example.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.capstone.databinding.ActivityMainBinding
import com.example.capstone.history.HistoryFragment
import com.example.capstone.home.HomeFragment
import com.example.capstone.like.LikeFragment
import com.example.capstone.mypage.MyPageFragment
import com.example.capstone.restaurant.RestaurantMainFragment
import com.example.capstone.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var bnv_main = findViewById(R.id.nav_view) as BottomNavigationView


        bnv_main.run { setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_home -> {
                    // 다른 프래그먼트 화면으로 이동하는 기능
                    val homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, homeFragment).commit()
                }
                R.id.navigation_search -> {
                    val boardFragment = RestaurantMainFragment()//SearchFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, boardFragment).commit()
                }
                R.id.navigation_history -> {
                    val settingFragment = HistoryFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, settingFragment).commit()
                }
                R.id.navigation_like -> {
                    val settingFragment = LikeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, settingFragment).commit()
                }
                R.id.navigation_myPage -> {
                    val settingFragment = MyPageFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, settingFragment).commit()
                }
            }
            true
        }
            selectedItemId = R.id.navigation_home
        }
    }
    fun HideBottomNavi(state: Boolean){
        if(state) binding.navView.visibility = View.GONE else binding.navView.visibility = View.VISIBLE
    }

}