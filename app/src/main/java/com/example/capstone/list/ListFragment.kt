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
import com.example.capstone.ConfirmDialogInterface
import com.example.capstone.CustomDialog
import com.example.capstone.databinding.FragmentListBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class ListFragment : Fragment(), OnMapReadyCallback, ConfirmDialogInterface {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView : MapView

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 10
    private lateinit var mMap: GoogleMap

    private var mlat by Delegates.notNull<Double>()
    private var mlng by Delegates.notNull<Double>()
    private var requestString = ""
    lateinit var userInfo: SharedPreferences

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

        viewPager.adapter = viewpagerFragmentAdapter
        viewPager.setUserInputEnabled(false);
        val tabTitles = listOf("전체","한식", "양식", "중식", "일식", "해산물", "육류", "카페", "베이커리", "브런치", "주점")

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
            val dialog = CustomDialog(this@ListFragment, "- 인기순으로 정렬됩니다.\n- 반경 내의 음식점에만 대기 신청이 가능합니다.\n오늘도 내 자리 내놔를 이용해주셔서 감사합니다." , 0, 1)
            dialog.isCancelable = true
            this.fragmentManager?.let { it1 -> dialog.show(it1, "ConfirmDialog") }
        }
        return root
    }

    override fun onYesButtonClick(num: Int, theme: Int) {

    }

    override fun onMapReady(map: GoogleMap) {
        mMap=map
        if(checkPermissionForLocation(requireContext())) startLocationUpdates()
    }
    fun onAddMarker(latitude:Double, longitude:Double, map:GoogleMap){
        Log.d("hy", "onAddMarker")
        val position = LatLng(latitude , longitude) //내 위치
        map.addMarker(MarkerOptions().position(position).title("현위치"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position,15f))

        val circle1KM = CircleOptions().center(position) // 원점
            .radius(2000.0)      //반지름 단위 : m, 반경 1km 원
            .strokeWidth(0f)  //선너비 0f : 선없음
            .fillColor(Color.parseColor("#41FCAF17")) //배경색

        map.addCircle(circle1KM)
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

    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            val location=locationResult.lastLocation
            mlat=location!!.latitude
            mlng= location.longitude
            onAddMarker(mlat, mlng, mMap)
            val geocoder = Geocoder(requireContext(), Locale.KOREA)
            val address:ArrayList<Address>
            val addressResult: String
            try {
                address = geocoder.getFromLocation(mlat, mlng, 10) as ArrayList<Address>
                if (address.size > 0) {
                    // 주소 받아오기
                    val currentLocationAddress = address[0].getAddressLine(0).toString()
                    addressResult = currentLocationAddress
                    var arr: List<String> = listOf("서울특별시", "중구", "명동")
                    for (addr in addressResult) {
                        val splitedAddr = addressResult.split(" ")
                        arr = splitedAddr
                    }
                    requestString=arr[2]
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
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