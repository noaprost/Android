package com.example.capstone.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.*
import com.example.capstone.databinding.FragmentKoreanBinding

class KoreanFragment : Fragment() {

    private var _binding: FragmentKoreanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKoreanBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val restaurantList = arrayListOf(
            RestaurantList(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점", 10,"#연인끼리", "#분위기좋은", "#인스타감성" ),
            RestaurantList(R.drawable.dummy_food_image, 5.0, 5, "호호파스타 연수점", 2,"#연인끼리", "#분위기좋은", "#인스타감성" ),
            RestaurantList(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점", 10,"#연인끼리", "#분위기좋은", "인스타감성" ),
            RestaurantList(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점", 10,"#연인끼리", "#분위기좋은", "인스타감성" ),
            RestaurantList(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점", 10,"#연인끼리", "#분위기좋은", "인스타감성" ),
            RestaurantList(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점", 10,"#연인끼리", "#분위기좋은", "인스타감성" ),
            RestaurantList(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점", 10,"#연인끼리", "#분위기좋은", "인스타감성" ),
            RestaurantList(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점", 10,"#연인끼리", "#분위기좋은", "인스타감성" ),
            RestaurantList(R.drawable.dummy_restaurant_image, 5.0, 19, "온리원 파스타 송도점", 10,"#연인끼리", "#분위기좋은", "인스타감성" )

            )
        binding.restaurantListRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.restaurantListRecyclerView.adapter = MyAdapter(restaurantList)

        return root
    }
    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        private lateinit var restaurantList: RestaurantList
        val restaurantImg : ConstraintLayout = itemView.findViewById(R.id.list_image)
        val rating : TextView = itemView.findViewById(R.id.list_rating)
        val commentNumber: TextView = itemView.findViewById(R.id.list_commentNumber)
        val name: TextView = itemView.findViewById(R.id.list_name)
        val keyword1: TextView = itemView.findViewById(R.id.list_keyword1)
        val keyword2: TextView = itemView.findViewById(R.id.list_keyword2)
        val keyword3: TextView = itemView.findViewById(R.id.list_keyword3)
        val waitingNum: TextView = itemView.findViewById(R.id.list_waiting)

        fun bind(restaurantList: RestaurantList){
            this.restaurantList=restaurantList
            restaurantImg.setBackgroundResource(restaurantList.restaurantImg)
            rating.text = restaurantList.rating.toString()
            commentNumber.text = restaurantList.commentNumber.toString()
            name.text = restaurantList.name
            keyword1.text=restaurantList.keyword1
            keyword2.text=restaurantList.keyword2
            keyword3.text=restaurantList.keyword3
            waitingNum.text=restaurantList.waiting.toString()
            itemView.setOnClickListener {
                val mainAct = activity as MainActivity
                mainAct.ChangeFragment("Restaurant")
            }

        }

    }
    inner class MyAdapter(private val list:List<RestaurantList>): RecyclerView.Adapter<MyViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view=layoutInflater.inflate(R.layout.item_restaurant_list, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val post=list[position]
            holder.bind(post)
        }
    }

}