package com.example.capstone.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.MainActivity
import com.example.capstone.R
import com.example.capstone.ResID
import com.example.capstone.Restaurants
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
    private var previousWaitingList = ArrayList<PreviousWaiting>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        previousWaitingList.apply {
            add(
                PreviousWaiting("3","2023-05-03", R.drawable.dummy_restaurant_image, "명태어장")
            )
            add(
                PreviousWaiting("1","2023-05-04", R.drawable.dummy_restaurant_image, "명태어장")
            )
        }

        binding.previousWaitingRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.previousWaitingRecyclerView.setHasFixedSize(true)
        binding.previousWaitingRecyclerView.adapter = PreviousWaitingAdapter(previousWaitingList)

        return root
    }

    inner class PreViewHolder(view: View): RecyclerView.ViewHolder(view){
        private lateinit var prewait: PreviousWaiting
        private val writeReviewBtn: TextView =itemView.findViewById(R.id.writeReviewBtn)

        fun bind(prewait : PreviousWaiting) : Unit{
            this.prewait = prewait

            writeReviewBtn.setOnClickListener {
                val intent = Intent(activity, WriteReviewActivity::class.java)
                intent.putExtra("resId", prewait.resId)
                intent.putExtra("resName", prewait.waitingTitle)
                startActivity(intent)
            }

            itemView.setOnClickListener {
                getResInfo(ResID(prewait.resId))
            }
        }
    }

    inner class PreviousWaitingAdapter(private val preList:List<PreviousWaiting>): RecyclerView.Adapter<PreViewHolder>(){
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
    private fun getResInfo(ResID: ResID){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.getResInfo(ResID) ?:return

        call.enqueue(object : Callback<Restaurants> {

            override fun onResponse(call: Call<Restaurants>, response: Response<Restaurants>) {
                Log.d("retrofit", "레스토랑 정보 - 응답 성공 / t : ${response.raw()} ${response.body()}")
                val bundle=Bundle()
                bundle.putSerializable("restaurant", response.body())
                val mainAct = activity as MainActivity
                mainAct.ChangeFragment("Restaurant", bundle)
            }
            override fun onFailure(call: Call<Restaurants>, t: Throwable) {
                Log.d("retrofit", "레스토랑 정보 - 응답 실패 / t: $t")
                Toast.makeText(activity, "레스토랑 정보를 불러올 수 없습니다.", Toast.LENGTH_LONG).show()

            }
        })
    }

}