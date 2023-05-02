package com.example.capstone.list

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.capstone.*
import com.example.capstone.R
import com.example.capstone.databinding.FragmentListBinding
import com.example.capstone.retrofit.API
import com.example.capstone.retrofit.IRetrofit
import com.example.capstone.retrofit.RetrofitClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.Math.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.properties.Delegates

class ListFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView : MapView

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 10
    private lateinit var mMap: GoogleMap

    private var mlat by Delegates.notNull<Double>()
    private var mlng by Delegates.notNull<Double>()
    lateinit var userInfo: SharedPreferences
    private var presentLocation = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        MapsInitializer.initialize(requireContext())
        val viewPager: ViewPager2 = binding.pager
        val tabLayout: TabLayout = binding.tabLayout
        val viewpagerFragmentAdapter = ViewPagerAdapter(this@ListFragment)
        userInfo = this.requireActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE)

        viewPager.adapter = viewpagerFragmentAdapter
        viewPager.isUserInputEnabled = false
        val tabTitles = listOf("전체","한식", "양식", "중식", "일식", "카페/베이커리", "주점")

        TabLayoutMediator(tabLayout, viewPager) { tab, position -> tab.text = tabTitles[position] }.attach()

        mapView = binding.mapFragment2
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        binding.listButton.setOnClickListener {//패널 닫기
            binding.mainFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }

        mLocationRequest =  LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        binding.infoButton.setOnClickListener {
            binding.infoLayout.visibility=View.VISIBLE
        }
        binding.infoLayout.setOnClickListener {
            binding.infoLayout.visibility=View.GONE
        }
        return root
    }

    override fun onMapReady(map: GoogleMap) {
        mMap=map
        if(checkPermissionForLocation(requireContext())) startLocationUpdates()
        map.setOnMarkerClickListener {
            if(it.tag!=null){
                val restaurant:Restaurants = it.tag as Restaurants
                binding.mapResName.text = restaurant.resName
                binding.mapResRaiting.text = restaurant.resRating.toString()
                binding.mapResRevCnt.text = restaurant.revCnt.toString()
                binding.waitingTeamNum.text = restaurant.currWaiting.toString()

                if(restaurant.keyWord !=null){
                    var arr:List<String> =listOf("", "", "")
                    for (addr in restaurant.keyWord) {
                        val splitedAddr = restaurant.keyWord.split("[\"", "\", \"", "\"]")
                        arr = splitedAddr
                    }
                    Log.d("hy", arr.toString())
                    binding.maKeyword1.text="#"+arr[1]
                    binding.maKeyword2.text="#"+arr[2]
                    binding.maKeyword3.text="#"+arr[3]
                }
                if(restaurant.resImg!=null) {
                    /*
                    val url="${API.BASE_URL}/${restaurant.resImg}"
                    Glide.with(this)
                        .load(url) // 불러올 이미지 url
                        .error(R.drawable.ic_flag) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(R.drawable.onlyone_logo) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                        .override(500, 300)
                        .into(binding.imageView24) // 이미지를 넣을 뷰

                     */
                }
                //캡쳐용 코드
                when (restaurant.resCategory) {
                    "한식" -> binding.imageView28.setImageResource(R.drawable.dummy_korean)
                    "중식" -> binding.imageView28.setImageResource(R.drawable.dummy_chinese)
                    "양식" -> binding.imageView28.setImageResource(R.drawable.dummy_restaurant_image)
                    "일식" -> binding.imageView28.setImageResource(R.drawable.dummy_japanese)
                    "카페/베이커리" -> binding.imageView28.setImageResource(R.drawable.dummy_bakery)
                    else -> binding.imageView28.setImageResource(R.drawable.dummy_alcohol)
                }
                binding.cardView.visibility = View.VISIBLE
                binding.cardView.setOnClickListener {
                    val bundle=Bundle()
                    bundle.putSerializable("restaurant", restaurant)
                    val mainAct = activity as MainActivity
                    mainAct.ChangeFragment("Restaurant", bundle)
                }
            }else{
                binding.cardView.visibility = View.GONE
            }
            false
        }
        map.setOnMapClickListener { binding.cardView.visibility = View.GONE }
    }
    fun onAddMarker(latitude:Double, longitude:Double, map:GoogleMap, restaurant: Restaurants?){
        val position = LatLng(latitude , longitude)
        var markerOption=MarkerOptions()

        if(restaurant==null) markerOption.position(position).title("현위치").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_flag))
        else markerOption.position(position).title(restaurant.resName)
        val marker:Marker= map.addMarker(markerOption)!!
        marker.tag=restaurant
    }

    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult) {
            val location=locationResult.lastLocation
            mlat=location!!.latitude
            mlng= location.longitude
            val position = LatLng(mlat , mlng)
            //내위치 표시
            onAddMarker(mlat, mlng, mMap, restaurant = null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,15f))
            val circle = CircleOptions().center(position) // 원점
                .radius(2000.0)      //반지름 단위 : m, 반경 1km 원
                .strokeWidth(0f)  //선너비 0f : 선없음
                .fillColor(Color.parseColor("#41FCAF17")) //배경색
            mMap.addCircle(circle)

            val address:ArrayList<Address>
            val addressResult: String
            try {
                val geocoder = Geocoder(requireContext(), Locale.KOREA)
                address = geocoder.getFromLocation(mlat, mlng, 10) as ArrayList<Address>
                if (address.size > 0) {
                    addressResult = address[0].getAddressLine(0).toString()
                    var arr: List<String> = listOf("인천광역시", "연수구", "송도동")
                    for (addr in addressResult)
                        arr = addressResult.split(" ")
                    for(i in 1 until arr.size){
                        presentLocation=presentLocation+arr[i]+" "
                    }
                    userInfo.edit().putString("userLocation", presentLocation).apply()
                    showRestaurants(resAddress(arr[3]))
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun showRestaurants(resAddress: resAddress){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.showRestaurants(resAddress=resAddress) ?:return

        call.enqueue(object : Callback<RestaurantList> {

            override fun onResponse(call: Call<RestaurantList>, response: Response<RestaurantList>) {
                Log.d("retrofit", "음식점 지도 리스트 - 응답 성공 / t : ${response.raw()} ${response.body()?.results}")
                var list:List<Address>?
                val geocoder = Geocoder(requireContext(), Locale.KOREA)
                var n=0
                for (addr in response.body()!!.results){
                    list = geocoder.getFromLocationName(addr.resAddress, 10)
                    if(getDistance(list[0].latitude, list[0].longitude)<=2000){
                        onAddMarker(list[0].latitude, list[0].longitude, mMap, addr)
                        n += 1
                        if(n==10) break
                    }
                }
            }
            override fun onFailure(call: Call<RestaurantList>, t: Throwable) {
                Log.d("retrofit", "음식점 지도 리스트 - 응답 실패 / t: $t")
            }
        })
    }
    fun getDistance(lat: Double, lon: Double): Int {
        val dLat = toRadians(mlat - lat)
        val dLon = toRadians(mlng - lon)
        val a = kotlin.math.sin(dLat / 2).pow(2.0) + kotlin.math.sin(dLon / 2).pow(2.0) * kotlin.math.cos(toRadians(lat)) * kotlin.math.cos(toRadians(mlat))
        val c = 2 * kotlin.math.asin(kotlin.math.sqrt(a))
        return (6372.8 * 1000 * c).toInt()
    }

    // 위치 권한이 있는지 확인 하는 메서드
    private fun checkPermissionForLocation(context: Context): Boolean {
        // Android 6.0 Marshmallow 이상에서는 위치 권한에 추가 런타임 권한이 필요
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                //권한 요청 알림 보내기
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    private fun startLocationUpdates() {
        //FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper()!!)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Toast.makeText(context, "위치 접근 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

}