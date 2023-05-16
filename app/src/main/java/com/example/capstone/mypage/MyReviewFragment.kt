package com.example.capstone.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstone.*
import com.example.capstone.databinding.FragmentMyReviewBinding
import com.example.capstone.restaurant.ReviewImageActivity
import com.example.capstone.retrofit.API
import com.example.capstone.retrofit.IRetrofit
import com.example.capstone.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyReviewFragment : Fragment(), ConfirmDialogInterface {
    private var _binding: FragmentMyReviewBinding? = null
    private val binding get() = _binding!!
    var hasReview = false
    lateinit var list:List<MyReviewData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyReviewBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.backButton.setOnClickListener {
            destroy()
        }
        val userId = this.requireActivity().getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE).getString("userId", "0")
        val isMember = this.requireActivity().getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE).getBoolean("isMember", false)

        if(isMember){//로그인이 돼있으면
            myReview(userId(userId!!))
        }else{
            binding.textView64.visibility=View.VISIBLE
            binding.myReviewRecyclerView.visibility=View.GONE
        }
        return root
    }
    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        private lateinit var review: MyReviewData
        private val reviewComment: TextView =itemView.findViewById(R.id.reviewComment)
        private val reviewScore: TextView =itemView.findViewById(R.id.reviewScore)
        private val writingDate: TextView =itemView.findViewById(R.id.writingDate)
        private val resName: TextView =itemView.findViewById(R.id.reviewRestaurantName)
        private val keyword1: TextView =itemView.findViewById(R.id.textView11)
        private val keyword2: TextView =itemView.findViewById(R.id.textView12)
        private val keyword3: TextView =itemView.findViewById(R.id.textView13)
        private val img: ImageView =itemView.findViewById(R.id.reviewImage)
        private val isSatisfied: ImageView =itemView.findViewById(R.id.isSatisfied)
        private val deleteButton: TextView = itemView.findViewById(R.id.myReviewDelete)
        private val myReviewInfoBox: LinearLayout = itemView.findViewById(R.id.myReviewInfoBox)


        fun bind(review: MyReviewData){
            this.review=review
            reviewComment.text=this.review.RevTxt
            reviewScore.text=review.Rating.toString()
            resName.text=review.resName
            if(review.RevTime!=null){
                var arr:List<String> =listOf("", "", "")
                for (addr in review.RevKeyWord) {
                    val splitedAddr = review.RevTime.split("T", ":")
                    arr = splitedAddr
                }
                writingDate.text="${arr[0]} ${arr[1]}:${arr[2]}"
            }else writingDate.text=""

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
            if(review.RevSatis!=0){
                isSatisfied.setImageResource(R.drawable.ic_unsatisfied)
            }
            if(review.RevImg!=null){
                img.visibility=View.VISIBLE
                val url="${API.BASE_URL}/${review.RevImg}"
                Glide.with(this@MyReviewFragment)
                    .load(url) // 불러올 이미지 url
                    .error(R.drawable.onlyone_logo) // 로딩 에러 발생 시 표시할 이미지
                    .fallback(R.drawable.onlyone_logo) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                    .into(img) // 이미지를 넣을 뷰
            }else{img.visibility=View.GONE}
            img.setOnClickListener {
                val intent = Intent(context, ReviewImageActivity::class.java)
                intent.putExtra("url", "${API.BASE_URL}/${review.RevImg}")
                startActivity(intent)
            }
            myReviewInfoBox.setOnClickListener {
                getResInfo(ResID(review.ResIdx))
            }
            deleteButton.setOnClickListener{
                val dialog = CustomDialog(this@MyReviewFragment, "리뷰를 삭제하시겠습니까?\n재작성은 불가능합니다.", 0, 0)
                dialog.isCancelable = false
                this@MyReviewFragment.fragmentManager?.let { it1 -> dialog.show(it1, "ConfirmDialog") }
                //todo 리사이클러뷰 새로고침
            }
        }

    }
    inner class MyAdapter(private val list:List<MyReviewData>): RecyclerView.Adapter<MyViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view=layoutInflater.inflate(R.layout.item_my_review, parent, false)
            return MyViewHolder(view)
        }
        override fun getItemCount(): Int = list.size
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val post=list[position]
            holder.bind(post)
        }
    }
    private fun myReview(userId: userId){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.myReview(userId) ?:return

        call.enqueue(object : Callback<MyReview> {

            override fun onResponse(call: Call<MyReview>, response: Response<MyReview>) {
                Log.d("retrofit", "내 리뷰 목록 - 응답 성공 / t : ${response.raw()} ${response.body()}")
                if(response.body()!=null){
                    if(response.body()!!.message.isNullOrEmpty()){
                        binding.textView64.visibility=View.VISIBLE
                        binding.myReviewRecyclerView.visibility=View.INVISIBLE
                    }else{
                        hasReview=true
                        binding.textView64.visibility=View.GONE
                        binding.myReviewRecyclerView.visibility=View.VISIBLE
                        binding.myReviewRecyclerView.layoutManager= LinearLayoutManager(context)
                        binding.myReviewRecyclerView.adapter=MyAdapter(response.body()!!.message)

                    }
                }
            }
            override fun onFailure(call: Call<MyReview>, t: Throwable) {
                Log.d("retrofit", "내 리뷰 목록 - 응답 실패 / t: $t")

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

    override fun onYesButtonClick(num: Int, theme: Int) {
        when(num){

        }
    }

    fun destroy(){
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction().remove(this@MyReviewFragment).commit()
        fragmentManager.popBackStack()
    }

}