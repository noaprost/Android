package com.example.capstone.restaurant

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.capstone.MainActivity
import com.example.capstone.R
import com.example.capstone.Restaurants
import com.example.capstone.databinding.FragmentRestaurantMainBinding
import com.example.capstone.retrofit.API
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.net.URL

class RestaurantMainFragment : Fragment() {

    private var _binding: FragmentRestaurantMainBinding? = null
    private val binding get() = _binding!!
    private var isLiked=false
    lateinit var resInfo: Restaurants
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantMainBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val mainAct = activity as MainActivity
        mainAct.HideBottomNavi(true)

        val bundle = arguments
        resInfo=bundle!!.getSerializable("restaurant") as Restaurants

        val viewPager: ViewPager2 = binding.pager
        val tabLayout: TabLayout = binding.tabLayout
        val viewpagerFragmentAdapter = ViewPagerAdapter(this,bundle)

        // ViewPager2의 adapter 설정
        viewPager.adapter = viewpagerFragmentAdapter

        // ###### TabLayout 과 ViewPager2를 연결
        // 1. 탭메뉴의 이름을 리스트로 생성해둔다.
        val tabTitles = listOf("대기", "매칭", "리뷰", "정보")

        // 2. TabLayout 과 ViewPager2를 연결하고, TabItem 의 메뉴명을 설정한다.
        TabLayoutMediator(tabLayout, viewPager) { tab, position -> tab.text = tabTitles[position] }.attach()

        binding.mainScrollView.run{
            header=binding.headerView
            stickListener = { _ -> Log.d("LOGGER_TAG", "stickListener") }
            freeListener = { _ -> Log.d("LOGGER_TAG", "freeListener") }
        }

        binding.backButton.setOnClickListener {
            destroy()
        }

        binding.button.setOnClickListener {
            val intent = Intent(activity, RestaurantWaitingActivity::class.java)
            intent.putExtra("resId", resInfo.resPhNum)
            intent.putExtra("currWaiting", resInfo.currWaiting)
            intent.putExtra("resSeat", resInfo.resSeat)
            intent.putExtra("resSeatCnt", resInfo.resSeatCnt)
            startActivity(intent)
        }

        binding.imageButton4.setOnClickListener {
            //todo 좋아요 버튼 연결
            //임시 코드
            isLiked = !isLiked
            isLiked = if(isLiked) {
                binding.imageButton4.setColorFilter(resources.getColor(R.color.INUYellow))
                true
            } else {
                binding.imageButton4.setColorFilter(resources.getColor(R.color.semiWhite))
                false
            }

        }
        viewPager.offscreenPageLimit=2
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val view = (viewPager[0] as RecyclerView).layoutManager?.findViewByPosition(position)
                view?.post {
                    val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                    val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    view.measure(wMeasureSpec, hMeasureSpec)
                    if (viewPager.layoutParams.height != view.measuredHeight) {
                        viewPager.layoutParams = (viewPager.layoutParams).also { lp -> lp.height = view.measuredHeight }
                    }
                }
            }
        })
        viewPager.isUserInputEnabled = false
        attach()
        return root
    }
    fun attach(){
        if(resInfo.resImg!=null) {
            //todo 이미지 url 수정
            val url="${API.BASE_URL}/${resInfo.resImg}"
            Glide.with(this)
                .load(url) // 불러올 이미지 url
                .error(R.drawable.onlyone_logo) // 로딩 에러 발생 시 표시할 이미지
                .fallback(R.drawable.onlyone_logo) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                .into(binding.imageView) // 이미지를 넣을 뷰

        }
        binding.textView.text=resInfo.resName
        binding.star.text=resInfo.resRating.toString()
        binding.totalReview.text="("+resInfo.revCnt.toString()+")"
        if(resInfo.keyWord !=null){
            var arr:List<String> =listOf("", "", "")
            for (addr in resInfo.keyWord) {
                val splitedAddr = resInfo.keyWord.split("[\"", "\", \"", "\"]")
                arr = splitedAddr
            }
            Log.d("hy", arr.toString())
            binding.keyword1.text="#"+arr[1]
            binding.keyword2.text="#"+arr[2]
            binding.keyword3.text="#"+arr[3]
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavi(false)
        _binding = null
    }
    private fun destroy(){
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction().remove(this@RestaurantMainFragment).commit()
        fragmentManager.popBackStack()

    }
}