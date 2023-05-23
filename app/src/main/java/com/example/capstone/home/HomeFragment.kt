package com.example.capstone.home

import android.Manifest
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstone.MainActivity
import com.example.capstone.R
import com.example.capstone.WaitingCustomDialog
import com.example.capstone.databinding.FragmentHomeBinding
import com.google.android.gms.location.*
import java.io.IOException
import java.util.*
import com.example.capstone.*
import com.example.capstone.retrofit.API
import com.example.capstone.retrofit.IRetrofit
import com.example.capstone.retrofit.RetrofitClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class HomeFragment : Fragment(), WaitingInfoCheckInterface {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    private lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는
    private val REQUEST_PERMISSION_LOCATION = 10
    private var presentLocation = ""

    private lateinit var userInfo: SharedPreferences
    private lateinit var userId : String
    private lateinit var waitingInfoDialog: WaitingCustomDialog
    private lateinit var waitingInfo : SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userInfo = this.requireActivity().getSharedPreferences("userInfo", MODE_PRIVATE)
        userId = userInfo.getString("userId", "0").toString()
        Log.d("hyhyhy", userInfo.getString("userLocation", "").toString())
        val isMember = userInfo.getBoolean("isMember",false)
        if(isMember){
            binding.textView90.visibility=View.GONE
            binding.restaurantHomeRecyclerView1.visibility=View.VISIBLE
            recommendRestaurant(userId(userId))
            binding.title1.setOnClickListener {
                val bundle = Bundle()
                val mainAct = activity as MainActivity
                mainAct.ChangeFragment("Matching", bundle)
            }
        }else{
            binding.textView90.visibility=View.VISIBLE
            binding.restaurantHomeRecyclerView1.visibility=View.GONE
        }
        hotRestaurant()


        // 대기 내역이 있는 경우에만 대기 정보 버튼이 보이도록 설정
        var isExistWatingInfo = true // 불러온 데이터의 존재 여부로 판단되도혹 수정 필요
        binding.watingInfoBtn.visibility = if(isExistWatingInfo){
            View.VISIBLE
        }else{
            View.INVISIBLE
        }

        // 대기 정보 버튼을 누를 경우 팝업 연결
        binding.watingInfoBtn.setOnClickListener {
            waitingInfo = this.requireActivity().getSharedPreferences("waitingInfo", MODE_PRIVATE)
            val waitIndex = this.requireActivity().getSharedPreferences("waitingInfo", AppCompatActivity.MODE_PRIVATE).getString("waitIndex", "58").toString()
            val resPhNum = this.requireActivity().getSharedPreferences("waitingInfo", AppCompatActivity.MODE_PRIVATE).getString("resPhNum", "032 934 6188").toString()
            waitingInfoCheck(WaitCheckForm(waitIndex, resPhNum))
        }

        binding.title2.setOnClickListener {
            val bundle = Bundle()
            val mainAct = activity as MainActivity
            mainAct.ChangeFragment("Hot", bundle)
        }

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
        private lateinit var restaurantItem: RestaurantInfo
        private val restaurantImg : ImageView = itemView.findViewById(R.id.restaurantImage)
        private val rating : TextView = itemView.findViewById(R.id.rating)
        private val commentNumber : TextView = itemView.findViewById(R.id.commentNumber)
        private val restaurantName : TextView = itemView.findViewById(R.id.restaurantName)


        fun bind(restaurantItem : RestaurantInfo){
            restaurantImg.clipToOutline=true
            rating.text=restaurantItem.resRating.toString()
            commentNumber.text=restaurantItem.revCnt.toString()
            restaurantName.text=restaurantItem.resName

            if(restaurantItem.resImg!=null){
                val url="${API.BASE_URL}/${restaurantItem.resImg}"
                Glide.with(this@HomeFragment)
                    .load(url) // 불러올 이미지 url
                    .error(R.drawable.onlyone_logo) // 로딩 에러 발생 시 표시할 이미지
                    .fallback(R.drawable.onlyone_logo) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                    .into(restaurantImg) // 이미지를 넣을 뷰
            }
            itemView.setOnClickListener {
                getResInfo(ResID(restaurantItem.resIdx))
            }
        }
    }

    inner class MatchingRestaurantAdapter(private val matchingRestaurantList: List<RestaurantInfo>): RecyclerView.Adapter<RestaurantViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
            val view = layoutInflater.inflate(R.layout.item_restaurant, parent, false)
            return RestaurantViewHolder(view)
        }

        override fun getItemCount(): Int = if (matchingRestaurantList.size>5) 5 else matchingRestaurantList.size

        override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
            val post = matchingRestaurantList[position]
            holder.bind(post)
        }
    }

    inner class HotRestaurantAdapter(private val hotRestaurantList: List<RestaurantInfo>): RecyclerView.Adapter<RestaurantViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
            val view = layoutInflater.inflate(R.layout.item_restaurant, parent, false)
            return RestaurantViewHolder(view)
        }

        override fun getItemCount(): Int = if (hotRestaurantList.size>5) 5 else hotRestaurantList.size


        override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
            val post = hotRestaurantList[position]
            holder.bind(post)
        }
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
                    binding.userLocation.text=presentLocation
                    userInfo.edit().putString("userLocation", presentLocation).apply()
                    userInfo.edit().putString("userLat", lat.toString()).apply()
                    userInfo.edit().putString("userLng", lng.toString()).apply()

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

    private fun hotRestaurant(){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.hotRestaurant() ?:return

        call.enqueue(object : retrofit2.Callback<RecommendRestaurants> {

            override fun onResponse(call: Call<RecommendRestaurants>, response: Response<RecommendRestaurants>){
                Log.d("retrofit", "핫한 음식점 - 응답 성공 / t : ${response.raw()} ${response.body()?.message}")
                val matcharr = response.body()?.message
                if(matcharr != null){
                    binding.restaurantHomeRecyclerView2.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    binding.restaurantHomeRecyclerView2.setHasFixedSize(true)
                    binding.restaurantHomeRecyclerView2.adapter = HotRestaurantAdapter(matcharr)
                }
            }
            override fun onFailure(call : Call<RecommendRestaurants>, t: Throwable){
                Log.d("retrofit", "핫한 음식점 - 응답 실패 / t: $t")
            }
        })
    }
    private fun recommendRestaurant(userId: userId){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.recommendRestaurant(userId) ?:return

        call.enqueue(object : retrofit2.Callback<RecommendRestaurants> {

            override fun onResponse(call: Call<RecommendRestaurants>, response: Response<RecommendRestaurants>){
                Log.d("retrofit", "음식점 매칭- 응답 성공 / t : ${response.raw()} ${response.body()?.message}")
                val matcharr = response.body()?.message
                if(matcharr != null){
                    binding.restaurantHomeRecyclerView1.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    binding.restaurantHomeRecyclerView1.setHasFixedSize(true)
                    binding.restaurantHomeRecyclerView1.adapter = MatchingRestaurantAdapter(matcharr)
                }
            }
            override fun onFailure(call : Call<RecommendRestaurants>, t: Throwable){
                Log.d("retrofit", "음식점 매칭 - 응답 실패 / t: $t")
            }
        })
    }
    private fun getResInfo(ResID: ResID){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.getResInfo(ResID) ?:return

        call.enqueue(object : Callback<List<Restaurants>> {

            override fun onResponse(call: Call<List<Restaurants>>, response: Response<List<Restaurants>>) {
                Log.d("retrofit", "레스토랑 정보 - 응답 성공 / t : ${response.raw()} ${response.body()}")
                val bundle=Bundle()
                bundle.putSerializable("restaurant", response.body()!![0])
                val mainAct = activity as MainActivity
                mainAct.ChangeFragment("Restaurant", bundle)
            }
            override fun onFailure(call: Call<List<Restaurants>>, t: Throwable) {
                Log.d("retrofit", "레스토랑 정보 - 응답 실패 / t: $t")
                Toast.makeText(activity, "레스토랑 정보를 불러올 수 없습니다.", Toast.LENGTH_LONG).show()

            }
        })
    }

    // 대기 현황 팝업 레트로핏 연결
    private fun waitingInfoCheck(WaitCheckForm : WaitCheckForm){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.waitingInfoCheck(WaitCheckForm) ?:return

        call.enqueue(object : Callback<ResWaitInfo> {
            override fun onResponse(call: Call<ResWaitInfo>, response: Response<ResWaitInfo>){
                Log.d("retrofit", "대기 현황 - 응답 성공 / t : ${response.raw()} ${response.body()}")
                val mes = response.body()?.message.toString()
                waitingInfoDialog = WaitingCustomDialog(this@HomeFragment, mes, 0, 0)
                waitingInfoDialog.isCancelable = true
                waitingInfoDialog.show(this@HomeFragment.parentFragmentManager, "WaitingCustomDialog")

            }

            override fun onFailure(call: Call<ResWaitInfo>, t: Throwable) {
                Log.d("retrofit", "대기 현황 - 응답 실패 / t: $t")
            }
        })
    }

}
