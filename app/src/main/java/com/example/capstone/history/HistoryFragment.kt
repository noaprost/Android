package com.example.capstone.history

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstone.*
import com.example.capstone.databinding.FragmentHistoryBinding
import com.example.capstone.retrofit.API
import com.example.capstone.retrofit.IRetrofit
import com.example.capstone.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment() {

    private var _binding : FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    lateinit var userInfo: SharedPreferences
    var reviewData = HashMap<String, String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        userInfo = this.requireActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val userPhoneNum = userInfo.getString("userPhoneNum", "010-1234-5678").toString()
        getWaitingHistory(UserPhone(userPhoneNum))

        return root
    }

    inner class PreViewHolder(view: View): RecyclerView.ViewHolder(view){
        private lateinit var prewait: WaitHistory
        private val writeReviewBtn: TextView =itemView.findViewById(R.id.writeReviewBtn)
        private val Date: TextView =itemView.findViewById(R.id.txtWaitingDate)
        private val Title: TextView =itemView.findViewById(R.id.txtWatingTitle)
        private val Img: ImageView =itemView.findViewById(R.id.waitingImg)


        fun bind(prewait : WaitHistory) : Unit{
            this.prewait = prewait
            Img.clipToOutline=true
            var arr:List<String> =listOf("", "", "")
            for (addr in prewait.acceptedTime) {
                val splitedAddr = prewait.acceptedTime.split("T")
                arr = splitedAddr
            }
            Date.text=arr[0]
            Title.text=prewait.resName
            if(prewait.resImg!=null){
                val url="${API.BASE_URL}/${prewait.resImg}"
                Glide.with(this@HistoryFragment)
                    .load(url) // 불러올 이미지 url
                    .error(R.drawable.onlyone_logo) // 로딩 에러 발생 시 표시할 이미지
                    .fallback(R.drawable.onlyone_logo) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                    .into(Img) // 이미지를 넣을 뷰
            }
            if(prewait.WaitisAccepted==3) {
                writeReviewBtn.text="리뷰 작성 완료!"
            }else{
                writeReviewBtn.text="리뷰 작성 하러 가기"
                writeReviewBtn.setOnClickListener {
                    val intent = Intent(activity, WriteReviewActivity::class.java)
                    intent.putExtra("resId", prewait.resIdx)
                    intent.putExtra("resName", prewait.resName)
                    intent.putExtra("WaitedIdx", prewait.resName)
                    startActivity(intent)
                }
            }


            itemView.setOnClickListener {
                getResInfo(ResID(prewait.resIdx))
            }
        }
    }

    inner class PreviousWaitingAdapter(private val preList:List<WaitHistory>): RecyclerView.Adapter<PreViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreViewHolder {
            val view = layoutInflater.inflate(R.layout.item_previous_waiting_info, parent, false)
            return PreViewHolder(view)
        }

        override fun getItemCount(): Int {
            return preList.size
        }

        override fun onBindViewHolder(holder: PreViewHolder, position: Int) {
            val post=preList[position]
            holder.bind(post)
        }

        override fun getItemId(position: Int): Long {
            return super.getItemId(position)
        }

    }
    private fun getWaitingHistory(UserPhone: UserPhone){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.getWaitingHistory(UserPhone) ?:return

        call.enqueue(object : Callback<WaitingHistoryList> {

            override fun onResponse(call: Call<WaitingHistoryList>, response: Response<WaitingHistoryList>) {
                Log.d("retrofit", "대기 내역 - 응답 성공 / t : ${response.raw()} ${response.body()}")
                if(!response.body()?.result1.isNullOrEmpty()){
                    binding.previousWaitingRecyclerView.visibility=View.VISIBLE
                    binding.textView88.visibility=View.INVISIBLE
                    val manager=LinearLayoutManager(context)
                    manager.reverseLayout=true
                    manager.stackFromEnd=true
                    binding.previousWaitingRecyclerView.layoutManager = manager
                    binding.previousWaitingRecyclerView.adapter = PreviousWaitingAdapter(response.body()!!.result1)
                }else{
                    binding.previousWaitingRecyclerView.visibility=View.INVISIBLE
                    binding.textView88.visibility=View.VISIBLE
                }
            }
            override fun onFailure(call: Call<WaitingHistoryList>, t: Throwable) {
                Log.d("retrofit", "대기 내역 - 응답 실패 / t: $t")
                Toast.makeText(activity, "대기 내역을 불러올 수 없습니다.", Toast.LENGTH_LONG).show()

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

}