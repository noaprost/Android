package com.example.capstone.restaurant

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.core.widget.EdgeEffectCompat.getDistance
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.capstone.*
import com.example.capstone.databinding.FragmentRestaurantMainBinding
import com.example.capstone.retrofit.API
import com.example.capstone.retrofit.IRetrofit
import com.example.capstone.retrofit.RetrofitClient
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.math.pow

class RestaurantMainFragment : Fragment() {

    private var _binding: FragmentRestaurantMainBinding? = null
    private val binding get() = _binding!!
    private var isLiked=false
    lateinit var resInfo: Restaurants
    lateinit var resId:String
    lateinit var userLocation: Location
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
        var Location = this.requireActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("userLocation", "")!!

        if(Location=="") {
            binding.button.visibility=View.INVISIBLE
            Toast.makeText(activity, "위치 설정이 필요해요.", Toast.LENGTH_LONG).show()
        }
        else {
            userLocation=getGeoCoding(Location)
            val resLocation = getGeoCoding(resInfo.resAddress)
            val distance = getDistance(resLocation.latitude, resLocation.longitude)

            if(distance<=2000){
                binding.button.visibility=View.VISIBLE
            }else {
                binding.button.visibility=View.VISIBLE
                Toast.makeText(activity, "현재 위치에선 대기가 불가능해요", Toast.LENGTH_LONG).show()
            }

        }


        binding.button.setOnClickListener {
            val intent = Intent(activity, RestaurantWaitingActivity::class.java)
            intent.putExtra("resPhNum", resInfo.resPhNum)
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
            /*
            //todo 이미지 url 수정
            val url="${API.BASE_URL}/${resInfo.resImg}"
            Glide.with(this)
                .load(url) // 불러올 이미지 url
                .error(R.drawable.ic_flag) // 로딩 에러 발생 시 표시할 이미지
                .fallback(R.drawable.onlyone_logo) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                .override(500, 300)
                .into(binding.imageView) // 이미지를 넣을 뷰

             */
        }
        //캡쳐용 코드
        if(resInfo.resCategory=="한식") binding.imageView.setImageResource(R.drawable.dummy_korean)
        else if(resInfo.resCategory=="중식") binding.imageView.setImageResource(R.drawable.dummy_chinese)
        else if(resInfo.resCategory=="양식") binding.imageView.setImageResource(R.drawable.dummy_restaurant_image)
        else if(resInfo.resCategory=="일식") binding.imageView.setImageResource(R.drawable.dummy_japanese)
        else if(resInfo.resCategory=="카페/베이커리") binding.imageView.setImageResource(R.drawable.dummy_bakery)
        else binding.imageView.setImageResource(R.drawable.dummy_alcohol)

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

    fun getGeoCoding(address: String): Location {
        return try {
            Geocoder(context, Locale.KOREA).getFromLocationName(address, 1)?.let{
                Location("").apply {
                    latitude =  it[0].latitude
                    longitude = it[0].longitude
                }
            }?: Location("").apply {
                latitude = 0.0
                longitude = 0.0
            }
        }catch (e:Exception) {
            e.printStackTrace()
            getGeoCoding(address) //재시도
        }
    }
    fun getDistance(lat: Double, lon: Double): Int {
        val dLat = Math.toRadians(userLocation.latitude - lat)
        val dLon = Math.toRadians(userLocation.longitude - lon)
        val a = kotlin.math.sin(dLat / 2).pow(2.0) + kotlin.math.sin(dLon / 2).pow(2.0) * kotlin.math.cos(
            Math.toRadians(lat)
        ) * kotlin.math.cos(Math.toRadians(userLocation.latitude))
        val c = 2 * kotlin.math.asin(kotlin.math.sqrt(a))
        return (6372.8 * 1000 * c).toInt()
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