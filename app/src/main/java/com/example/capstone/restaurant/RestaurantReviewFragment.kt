package com.example.capstone.restaurant

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.*
import com.example.capstone.databinding.FragmentRestaurantReviewBinding
import com.example.capstone.retrofit.API
import com.example.capstone.retrofit.IRetrofit
import com.example.capstone.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RestaurantReviewFragment : Fragment() {
    private var _binding: FragmentRestaurantReviewBinding? = null
    private val binding get() = _binding!!
    lateinit var resInfo: Restaurants
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRestaurantReviewBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val bundle = arguments
        resInfo=bundle!!.getSerializable("restaurant") as Restaurants
        binding.viewReviewButton.setOnClickListener {
            val intent = Intent(activity, RestaurantReviewDetail::class.java)
            intent.putExtra("resInfo", resInfo)
            startActivity(intent)
        }
        showRestaurantReview(ResID(resInfo.resIdx))
        binding.textView3.text=resInfo.revCnt.toString()+"건"

        return root
    }
    inner class MyViewHolder(view:View): RecyclerView.ViewHolder(view){
        private lateinit var review: Review
        private val userName: TextView =itemView.findViewById(R.id.userName)
        private val writingDate: TextView =itemView.findViewById(R.id.writingDate)
        private val reviewScore: TextView =itemView.findViewById(R.id.reviewScore)
        private val isSatisfied: ImageView =itemView.findViewById(R.id.isSatisfied)
        private val keyword1: TextView =itemView.findViewById(R.id.textView11)
        private val keyword2: TextView =itemView.findViewById(R.id.textView12)
        private val keyword3: TextView =itemView.findViewById(R.id.textView13)
        private val reviewImage: ImageView =itemView.findViewById(R.id.reviewImage)
        private val reviewComment: TextView =itemView.findViewById(R.id.reviewComment)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(review: Review){
            this.review=review
            if(review.RevTime!=null){
                var arr:List<String> =listOf("", "", "")
                for (addr in review.RevKeyWord) {
                    val splitedAddr = review.RevTime.split("T", ":")
                    arr = splitedAddr
                }

                writingDate.text="${arr[0]} ${arr[1]}:${arr[2]}"
            }else writingDate.text=""

            reviewScore.text=review.Rating.toString()
            if(review.RevKeyWord !=null){
                var arr:List<String> =listOf("", "", "")
                for (addr in review.RevKeyWord) {
                    val splitedAddr = review.RevKeyWord.split("[\"", "\", \"", "\"]")
                    arr = splitedAddr
                }
                Log.d("hy", review.RevKeyWord)
                Log.d("hy", arr.toString())
                keyword1.text="#"+arr[1]
                keyword2.text="#"+arr[2]
                keyword3.text="#"+arr[3]
            }
            if(review.RevImgID!=null){
                reviewImage.visibility=View.VISIBLE
                //todo 이미지 등록
            }
            reviewComment.text= review.RevTxt
        }

    }
    inner class MyAdapter(private val list:List<Review>): RecyclerView.Adapter<MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=layoutInflater.inflate(R.layout.item_review, parent, false)
        return MyViewHolder(view)
    }

        override fun getItemCount(): Int = 2

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val post=list[position]
            holder.bind(post)
        }
    }
    private fun showRestaurantReview(ResID: ResID){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.showRestaurantReview(ResID=ResID) ?:return

        call.enqueue(object : Callback<RestaurantReviewList> {

            override fun onResponse(call: Call<RestaurantReviewList>, response: Response<RestaurantReviewList>) {
                Log.d("retrofit", "음식점 리뷰 리스트 - 응답 성공 / t : ${response.raw()} ${response.body()}")
                val arr= response.body()?.result
                if (arr != null) {
                    binding.restaurantReviewRecyclerView.layoutManager= LinearLayoutManager(context)
                    binding.restaurantReviewRecyclerView.adapter=MyAdapter(arr)
                }else{
                    Log.d("hy", ResID.toString())
                    Toast.makeText(activity, "리뷰를 불러올 수 없습니다.", Toast.LENGTH_LONG).show()
                }

            }
            override fun onFailure(call: Call<RestaurantReviewList>, t: Throwable) {
                Log.d("retrofit", "음식점 리뷰 리스트 - 응답 실패 / t: $t")
            }
        })
    }
}