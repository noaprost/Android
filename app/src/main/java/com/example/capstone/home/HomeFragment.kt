package com.example.capstone.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.ConfirmDialogInterface
import com.example.capstone.CustomDialog
import com.example.capstone.MainActivity
import com.example.capstone.R
import com.example.capstone.databinding.FragmentHomeBinding
import com.example.capstone.hot.HotRestaurantFragment
import com.example.capstone.matching.MatchingRestaurantFragment
import com.google.android.gms.location.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment(), ConfirmDialogInterface {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var matchingRestaurantList = ArrayList<Restaurant>()
    private var hotRestaurantList = ArrayList<Restaurant>()

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    private lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는
    private val REQUEST_PERMISSION_LOCATION = 10
    lateinit var userInfo: SharedPreferences
    private var presentLocation = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userInfo = this.requireActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE)

        // 대기 내역이 있는 경우에만 대기 정보 버튼이 보이도록 설정
        var isExistWatingInfo = true // 불러온 데이터의 존재여부로 판단되도혹 수정 필요
        binding.watingInfoBtn.visibility = if(isExistWatingInfo){
            View.VISIBLE
        }else{
            View.INVISIBLE
        }

        // 대기 정보 버튼을 누를 경우 팝업 연결
        binding.watingInfoBtn.setOnClickListener {
            val dialog = CustomDialog(this,"", 0, 0)
        }

        binding.title1.setOnClickListener {
            val bundle = Bundle()
            val mainAct = activity as MainActivity
            mainAct.ChangeFragment("Matching", bundle)
        }

        binding.title2.setOnClickListener {
            val bundle = Bundle()
            val mainAct = activity as MainActivity
            mainAct.ChangeFragment("Hot", bundle)
        }

        // 추천 음식점 데이터
        matchingRestaurantList.apply{
            add(
                Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점")
            )
            add(
                Restaurant(R.drawable.dummy_restaurant_image, 4.9, 20, "온리원 파스타 송도점")
            )
            add(
                Restaurant(R.drawable.dummy_restaurant_image, 3.8, 82, "온리원 파스타 송도점")
            )
            add(
                Restaurant(R.drawable.dummy_restaurant_image, 4.1, 5, "온리원 파스타 송도점")
            )
            add(
                Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점")
            )
            add(
                Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점")
            )
            add(
                Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점")
            )
            add(
                Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점")
            )
            add(
                Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점")
            )

        }

        // hot한 음식점 데이터
        hotRestaurantList.apply {
            add(
                Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점")
            )
            add(
                Restaurant(R.drawable.dummy_restaurant_image, 4.4, 29, "온리원 파스타 송도점")
            )
            add(
                Restaurant(R.drawable.dummy_restaurant_image, 4.8, 72, "온리원 파스타 송도점")
            )
            add(
                Restaurant(R.drawable.dummy_restaurant_image, 4.1, 5, "온리원 파스타 송도점")
            )
            add(
                Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점")
            )
            add(
                Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점")
            )
            add(
                Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점")
            )
            add(
                Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점")
            )
            add(
                Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점")
            )
        }

        binding.restaurantHomeRecyclerView1.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.restaurantHomeRecyclerView1.setHasFixedSize(true)
        binding.restaurantHomeRecyclerView1.adapter = MatchingRestaurantAdapter(matchingRestaurantList)

        binding.restaurantHomeRecyclerView2.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.restaurantHomeRecyclerView2.setHasFixedSize(true)
        binding.restaurantHomeRecyclerView2.adapter = HotRestaurantAdapter(hotRestaurantList)

        //위치 관련 코드
        mLocationRequest =  LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        binding.constraintLayout4.setOnClickListener {
            if (checkPermissionForLocation(requireContext())) {
                startLocationUpdates()
            }
        }
        if(userInfo.getString("userLocation", "") !="")
            binding.userLocation.text= userInfo.getString("userLocation", "")!!

        return root
    }

    inner class RestaurantViewHolder(view : View): RecyclerView.ViewHolder(view){
        private lateinit var restaurantItem: Restaurant

        fun bind(restaurantItem : Restaurant){
            this.restaurantItem = restaurantItem

            itemView.setOnClickListener {
                val bundle = Bundle()
                val mainAct = activity as MainActivity
                mainAct.ChangeFragment("Restaurant", bundle)
            }
        }
    }

    inner class MatchingRestaurantAdapter(private val matchingRestaurantList: List<Restaurant>): RecyclerView.Adapter<RestaurantViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
            val view = layoutInflater.inflate(R.layout.item_restaurant, parent, false)
            return RestaurantViewHolder(view)
        }

        override fun getItemCount(): Int = matchingRestaurantList.size

        override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
            val post = matchingRestaurantList[position]
            holder.bind(post)
        }
    }

    inner class HotRestaurantAdapter(private val hotRestaurantList: List<Restaurant>): RecyclerView.Adapter<RestaurantViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
            val view = layoutInflater.inflate(R.layout.item_restaurant, parent, false)
            return RestaurantViewHolder(view)
        }

        override fun getItemCount(): Int = hotRestaurantList.size


        override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
            val post = hotRestaurantList[position]
            holder.bind(post)
        }
    }

    override fun onYesButtonClick(num: Int, theme: Int) {
        TODO("Not yet implemented")
    }

    private fun startLocationUpdates() {
        //FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
    }

    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult) {
            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
            val location=locationResult.lastLocation
            val geocoder = Geocoder(context, Locale.KOREA)
            val address:ArrayList<Address>
            val lat:Double=location!!.latitude
            val lng:Double=location.longitude
            try {
                address = geocoder.getFromLocation(lat, lng, 10) as ArrayList<Address>
                if (address.size > 0) {
                    // 주소 받아오기
                    val addressResult = address[0].getAddressLine(0).toString()
                    var arr: List<String> = listOf("인천광역시", "연수구", "송도동")
                    for (addr in addressResult) {
                        val splitedAddr = addressResult.split(" ")
                        arr = splitedAddr
                    }
                    for(i in 1 until arr.size){
                        presentLocation=presentLocation+arr[i]+" "
                    }
                    Log.d("hy", presentLocation)
                    binding.userLocation.text=presentLocation
                    userInfo.edit().putString("userLocation", presentLocation).apply()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                binding.userLocation.text = location!!.latitude.toString()+" "+location.longitude.toString()
            }
        }
    }

    // 위치 권한이 있는지 확인하는 메서드
    private fun checkPermissionForLocation(context: Context): Boolean {
        // Android 6.0 Marshmallow 이상에서는 위치 권한에 추가 런타임 권한이 필요
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                // 권한이 없으므로 권한 요청 알림 보내기
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    // 사용자에게 권한 요청 후 결과에 대한 처리 로직
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Log.d("ttt", "onRequestPermissionsResult() _ 권한 허용 거부")
                Toast.makeText(requireContext(), "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onPause() {
        super.onPause()
        if(presentLocation!="") mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
    }
}
