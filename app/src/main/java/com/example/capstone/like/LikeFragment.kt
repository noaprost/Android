package com.example.capstone.like

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.Restaurant
import com.example.capstone.databinding.FragmentLikeBinding

class LikeFragment : Fragment() {

    private var _binding: FragmentLikeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLikeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.backButton.setOnClickListener {
            //todo 백버튼 연결
        }
        val restaurantList = arrayListOf(
            Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점", 1),
            Restaurant(R.drawable.dummy_food_image, 5.0, 19, "네네치킨", 7),
            Restaurant(R.drawable.dummy_food_image, 5.0, 19, "온리원 파스타 송도점", 13),
            Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "오다초밥", 2),
            Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점", 0),
            Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "서브웨이 심곡점", 7),
            Restaurant(R.drawable.dummy_food_image, 5.0, 19, "온리원 파스타 송도점", 7),
            Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점", 7),
            Restaurant(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점", 7),
        )

        binding.likeRecyclerView.layoutManager =  GridLayoutManager(context, 2)
        binding.likeRecyclerView.adapter = restaurantList?.let { MyAdapter(it) }

        return root
    }
    inner class MyViewHolder(view:View): RecyclerView.ViewHolder(view){
        private lateinit var restaurant: Restaurant
        private val resName: TextView =itemView.findViewById(R.id.likeRestaurantName)
        private val img: ImageView =itemView.findViewById(R.id.likeRestaurantImg)
        private val waitingBox: LinearLayout =itemView.findViewById(R.id.likeWaitingBox)
        private val waitingNum: TextView =itemView.findViewById(R.id.waitingTeamNum)
        //todo 리뷰 개수 및 별점 추가

        fun bind(restaurant: Restaurant){
            this.restaurant=restaurant
            resName.text=this.restaurant.name
            waitingNum.text=this.restaurant.waiting.toString()
            img.setImageResource(this.restaurant.restaurantImg)
            if(this.restaurant.waiting==0){
                waitingBox.visibility=View.INVISIBLE
            }
            img.clipToOutline=true
            itemView.setOnClickListener{

            }
        }

    }
    inner class MyAdapter(private val list:List<Restaurant>): RecyclerView.Adapter<MyViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view=layoutInflater.inflate(R.layout.item_like, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int = list.size //최근 4개만

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val post=list[position]
            holder.bind(post)
            binding.textView34.visibility=View.GONE
        }
    }

}