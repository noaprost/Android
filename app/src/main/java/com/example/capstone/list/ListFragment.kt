package com.example.capstone.list

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.capstone.databinding.FragmentListBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class ListFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView : MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val viewPager: ViewPager2 = binding.pager
        val tabLayout: TabLayout = binding.tabLayout
        val viewpagerFragmentAdapter = ViewPagerAdapter(this@ListFragment)

        // ViewPager2의 adapter 설정
        viewPager.adapter = viewpagerFragmentAdapter

        // ###### TabLayout 과 ViewPager2를 연결
        // 1. 탭메뉴의 이름을 리스트로 생성해둔다.
        val tabTitles = listOf("한식", "양식", "중식", "일식", "해산물", "육류", "카페", "베이커리", "브런치", "주점")

        // 2. TabLayout 과 ViewPager2를 연결하고, TabItem 의 메뉴명을 설정한다.
        TabLayoutMediator(tabLayout, viewPager) { tab, position -> tab.text = tabTitles[position] }.attach()

        mapView = binding.mapFragment2
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        binding.listButton.setOnClickListener {//패널 닫기
            binding.mainFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }
        return root
    }

    override fun onMapReady(map: GoogleMap) {

        //val point = LatLng(37.514655, 126.979974)
        //map.addMarker(MarkerOptions().position(point).title("현위치"))
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(point,12f))
        onAddMarker(37.374605366192256, 126.63383983056585, map) //내위치
    }
    fun onAddMarker(latitude:Double, longitude:Double, map:GoogleMap){

        val position = LatLng(latitude , longitude) //내 위치
        map.addMarker(MarkerOptions().position(position).title("현위치"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position,12f))

        val circle1KM = CircleOptions().center(position) // 원점
            .radius(2000.0)      //반지름 단위 : m, 반경 1km 원
            .strokeWidth(0f)  //선너비 0f : 선없음
            .fillColor(Color.parseColor("#41FCAF17")); //배경색

        map.addCircle(circle1KM);
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