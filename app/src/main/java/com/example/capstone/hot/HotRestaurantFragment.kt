package com.example.capstone.hot

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.HotRestaurant
import com.example.capstone.MainActivity
import com.example.capstone.R
import com.example.capstone.databinding.FragmentHotRestaurantBinding
import com.example.capstone.restaurant.RestaurantWaitingActivity

class HotRestaurantFragment : Fragment() {
    private var _binding : FragmentHotRestaurantBinding? = null
    private val binding get() = _binding!!
    private var hotRestaurantList = ArrayList<HotRestaurant>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHotRestaurantBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.hotBackBtn.setOnClickListener {
            this.onDestroy()
        }

        binding.hotCategoryBtn.setOnClickListener {
            // 카테고리별로 필터 적용되는 기능 추가
        }

        hotRestaurantList.apply {
            add(
                HotRestaurant("온리원 파스타 송도점", "파스타 전문점입니다:)", 5.0, 34, "인천광역시 연수구 송도동")
            )
            add(
                HotRestaurant("온리원 파스타 송도점", "파스타 전문점입니다:)", 4.3, 23, "인천광역시 연수구 송도동")
            )
            add(
                HotRestaurant("온리원 파스타 송도점", "파스타 전문점입니다:)", 4.1, 66, "인천광역시 연수구 송도동")
            )
            add(
                HotRestaurant("온리원 파스타 송도점", "파스타 전문점입니다:)", 5.0, 34, "인천광역시 연수구 송도동")
            )
            add(
                HotRestaurant("온리원 파스타 송도점", "파스타 전문점입니다:)", 4.3, 23, "인천광역시 연수구 송도동")
            )
            add(
                HotRestaurant("온리원 파스타 송도점", "파스타 전문점입니다:)", 4.1, 66, "인천광역시 연수구 송도동")
            )
        }

        binding.hotRestaurantRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.hotRestaurantRecyclerView.setHasFixedSize(true)
        binding.hotRestaurantRecyclerView.adapter = HotAdapter(hotRestaurantList)

        return root
    }

    inner class HotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var hotRes: HotRestaurant

        fun bind(hotRes: HotRestaurant) {
            this.hotRes = hotRes

            itemView.setOnClickListener {
                val bundle = Bundle()
                val mainAct = activity as MainActivity
                mainAct.ChangeFragment("Restaurant", bundle)
            }
        }
    }

    inner class HotAdapter(private val hotRestaurantList: List<HotRestaurant>) :
        RecyclerView.Adapter<HotViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotViewHolder {
            val view = layoutInflater.inflate(
                R.layout.item_main_restaurant,
                parent,
                false
            )
            return HotViewHolder(view)
        }

        override fun onBindViewHolder(holder: HotViewHolder, position: Int) {
            val post = hotRestaurantList[position]
            holder.bind(post)
        }

        override fun getItemCount(): Int {
            return hotRestaurantList.size
        }
    }
}

