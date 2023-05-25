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
import android.os.Looper
import android.util.JsonToken
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
import java.util.*
import com.example.capstone.*
import com.example.capstone.retrofit.API
import com.example.capstone.retrofit.API.BASE_URL
import com.example.capstone.retrofit.IRetrofit
import com.example.capstone.retrofit.RetrofitClient
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okio.IOException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

private var stampNum = 0

class HomeFragment : Fragment(), WaitingInfoCheckInterface, ConfirmDialogInterface {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    private lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는
    private val REQUEST_PERMISSION_LOCATION = 10
    private var presentLocation = ""

    private lateinit var userInfo: SharedPreferences
    private lateinit var userId : String
    private lateinit var userPhoneNum : String

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
        userPhoneNum = userInfo.getString("userPhoneNum", "").toString()
        val isMember = userInfo.getBoolean("isMember",false)
        if(isMember){
            binding.textView90.visibility=View.GONE
            binding.restaurantHomeRecyclerView1.visibility=View.VISIBLE
            recommendRestaurant(userId(userId))
            getWaitingIndex(UserPhone(userPhoneNum))
            binding.title1.setOnClickListener {
                val bundle = Bundle()
                val mainAct = activity as MainActivity
                mainAct.ChangeFragment("Matching", bundle)
            }
        }else{
            binding.textView90.visibility=View.VISIBLE
            binding.restaurantHomeRecyclerView1.visibility=View.GONE
            binding.waitingInfoBtn.visibility=View.INVISIBLE
        }
        hotRestaurant()

        // 대기 정보 버튼을 누를 경우 팝업 연결
        binding.waitingInfoBtn.setOnClickListener {

            waitingInfo = this.requireActivity().getSharedPreferences("waitingInfo", MODE_PRIVATE)
            val waitIndex = this.requireActivity().getSharedPreferences("waitingInfo", AppCompatActivity.MODE_PRIVATE).getString("waitIndex", "")
            val resPhNum = this.requireActivity().getSharedPreferences("waitingInfo", AppCompatActivity.MODE_PRIVATE).getString("resPhNum", "").toString()
            waitingInfoCheck(WaitCheckForm(waitIndex!!, resPhNum))
            Log.d("hyhyhy", waitIndex)
            Log.d("hyhyhy", resPhNum)
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
    private fun getWaitingIndex(UserPhone: UserPhone){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.getWaitingIndex(UserPhone) ?:return

        call.enqueue(object : Callback<WaitIndexList> {

            override fun onResponse(call: Call<WaitIndexList>, response: Response<WaitIndexList>) {
                Log.d("retrofit", "대기 인덱스 - 응답 성공 / t : ${response.raw()} ${response.body()}")
                if(response.body()?.result.isNullOrEmpty()){
                    binding.waitingInfoBtn.visibility=View.VISIBLE
                }else{
                    val waitingInfo=this@HomeFragment.requireActivity().getSharedPreferences("waitingInfo", AppCompatActivity.MODE_PRIVATE)
                    waitingInfo.edit().putString("waitIndex", response.body()!!.result[0].WaitIndex.toString()).apply()
                    binding.waitingInfoBtn.visibility=View.VISIBLE
                }

            }
            override fun onFailure(call: Call<WaitIndexList>, t: Throwable) {
                Log.d("retrofit", "대기 인덱스 - 응답 실패 / t: $t ${t.message}")

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

    override fun onResume() {
        super.onResume()
        getWaitingIndex(UserPhone(userPhoneNum))
    }

    override fun onCancelButtonClick(num: Int, theme: Int) {
        when(num){
            1->binding.waitingInfoBtn.visibility = View.GONE
        }
    }

    override fun onDelayButtonClick(num: Int, theme: Int) {
        when (num) {
            0 -> {
                val userPhone = this.requireActivity().getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE).getString("userPhoneNum", "").toString()
                Log.d("baby", "userPhone : $userPhone")
                val requestBody = FormBody.Builder()
                    .add("UserPhone", userPhone)
                    .build()
                stampNumCheck(requestBody)
            }
        }
    }

    // 스탭프 개수 확인
    private fun stampNumCheck(requestBody: RequestBody) {

        var client = OkHttpClient()

        val request: Request = Request.Builder()
            .url(BASE_URL+"/user/stamp")
            .post(requestBody)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback{
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                var stampNum = 0
                var json = response.body.string().replace(" ", "")

                json = json.replace("{", "")
                json = json.replace("}", "")
                json = json.replace(",", "")
                json = json.replace("\"", "")
                json = json.replace(":", "")
                json = json.replace("stamp", "")
                json = json.replace("message스탬프개수는", "")
                json = json.replace("개입니다.", "")

                if(json.length == 2){
                    json = json.substring(0, 1)
                    stampNum = json.toInt()
                    Log.d("baby", "$stampNum")
                }else{
                    json = json.substring(0, 2)
                    stampNum = json.toInt()
                    Log.d("baby", "$stampNum")
                }

                if(stampNum > 0){
                    val waitIndex = waitingInfo.getString("waitIndex", "")
                    val resPhNum = waitingInfo.getString("resPhNum", "")
                    Log.d("baby", "${waitIndex}, $resPhNum")
                    waitingDelay(ResDelayInfo(waitIndex!!, resPhNum!!))
                }
                else{
                    val dialog = CustomDialog(this@HomeFragment, "스탬프 개수가 모자랍니다", 0, 1)
                    dialog.isCancelable = true
                    dialog.show(this@HomeFragment.parentFragmentManager, "CustomDialog")
                }
            }

            override fun onFailure(call: okhttp3.Call, e: okio.IOException) {
                Log.d("retrofit", "스탬프 개수 확인 실패 ${e.message}")
            }
        })
    }

    // 대기 미루기 레트로핏 연결
    fun waitingDelay(ResDelayInfo: ResDelayInfo){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create((IRetrofit::class.java))
        val call = iRetrofit?.waitingDelay(ResDelayInfo) ?:return

        call.enqueue(object : Callback<ResWaitDelay>{
            override fun onResponse(call: Call<ResWaitDelay>, response: Response<ResWaitDelay>) {
                Log.d("retrofit", "대기 미루기 - 응답 성공 / t : ${response.raw()} ${response.body()}")
            }

            override fun onFailure(call: Call<ResWaitDelay>, t: Throwable) {
                Log.d("retrofit", "대기 미루기 - 응답 실패 t : $t")
            }
        })
    }

    override fun onYesButtonClick(num: Int, theme: Int) {

    }
}
