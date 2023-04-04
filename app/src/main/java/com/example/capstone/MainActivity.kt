package com.example.capstone

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.capstone.databinding.ActivityMainBinding
import com.example.capstone.history.HistoryFragment
import com.example.capstone.home.HomeFragment
import com.example.capstone.like.LikeFragment
import com.example.capstone.list.ListFragment
import com.example.capstone.mypage.MyPageFragment
import com.example.capstone.mypage.MyReviewFragment
import com.example.capstone.restaurant.RestaurantMainFragment
import com.example.capstone.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var userId by Delegates.notNull<Long>()
    var isMember by Delegates.notNull<Boolean>()
    lateinit var userInfo: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userInfo = getSharedPreferences("userInfo", 0)
        userInfo.edit().putBoolean("isMember", false).apply()
        userInfo.edit().putString("userId", "0").apply()
        userInfo.edit().putString("userLocation", "").apply()
        userId = userInfo.getString("userInfo", "2")!!.toLong()
        isMember = userInfo.getBoolean("isMember", false)

        var bnv_main = findViewById<BottomNavigationView>(R.id.nav_view)

        bnv_main.run { setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_home -> {
                    val homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, homeFragment).commit()
                }
                R.id.navigation_search -> {
                    val boardFragment = SearchFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, boardFragment).commit()
                }
                R.id.navigation_list -> {
                    val settingFragment = ListFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, settingFragment).commit()
                }
                R.id.navigation_history -> {
                    val settingFragment = HistoryFragment()
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
    fun ChangePage(pageId:Int){
        var bnv_main = findViewById<BottomNavigationView>(R.id.nav_view)
        bnv_main.selectedItemId=pageId
    }

    fun ChangeFragment(page:String){
        val transaction = supportFragmentManager.beginTransaction()
        val bundle = Bundle()
        when(page) {
            "Like" -> {
                transaction.addToBackStack(null)
                transaction.replace(R.id.fragmentContainerView, LikeFragment())
            }
            "Restaurant" -> {
                transaction.addToBackStack(null)
                transaction.replace(R.id.fragmentContainerView, RestaurantMainFragment())
            }
            "MyReview" -> {
                val fragment:Fragment=MyReviewFragment()
                //bundle.putString("bundleData", "번들데이터의데이터")
                //fragment.arguments=bundle
                transaction.addToBackStack(null)
                transaction.replace(R.id.fragmentContainerView, fragment)
            }
        }
        transaction.commit()
    }

}