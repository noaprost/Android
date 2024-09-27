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
import com.bumptech.glide.Glide
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
    private var imgUrl=""
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
            userName.text=review.UserName
            reviewScore.text=review.Rating.toString()
            if(review.RevKeyWord !=null){
                var arr:List<String> =listOf("", "", "")
                for (addr in review.RevKeyWord) {
                    val splitedAddr = review.RevKeyWord.split("[\"", "\", \"", "\"]")
                    arr = splitedAddr
                }
                keyword1.text="#"+arr[1]
                keyword2.text="#"+arr[2]
                keyword3.text="#"+arr[3]
            }
            if(review.RevImg!=null){
                reviewImage.visibility=View.VISIBLE
                val url="${API.BASE_URL}/${review.RevImg}"
                Glide.with(this@RestaurantReviewFragment)
                    .load(url) // 불러올 이미지 url
                    .error(R.drawable.dummy_chinese) // 로딩 에러 발생 시 표시할 이미지
                    .fallback(R.drawable.dummy_chinese) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                    .into(reviewImage) // 이미지를 넣을 뷰
            }else{reviewImage.visibility=View.GONE}
            reviewComment.text= review.RevTxt
            if(review.RevSatis!=0){
                isSatisfied.setImageResource(R.drawable.ic_unsatisfied)
            }
        }

    }
    inner class MyAdapter(private val list:List<Review>): RecyclerView.Adapter<MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=layoutInflater.inflate(R.layout.item_review, parent, false)
        return MyViewHolder(view)
    }

        override fun getItemCount(): Int = if(2 <list.size){ 2 }else list.size

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
                if (!arr.isNullOrEmpty()) {
                    binding.textView3.text=arr.size.toString()+"건"
                    binding.restaurantReviewRecyclerView.layoutManager= LinearLayoutManager(context)
                    binding.restaurantReviewRecyclerView.adapter=MyAdapter(arr)
                }
            }
            override fun onFailure(call: Call<RestaurantReviewList>, t: Throwable) {
                Log.d("retrofit", "음식점 리뷰 리스트 - 응답 실패 / t: $t")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        showRestaurantReview(ResID(resInfo.resIdx.toString()))
    }
}