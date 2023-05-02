package com.example.capstone.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.capstone.R
import com.example.capstone.databinding.FragmentHistoryBinding

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
                PreviousWaiting("2022-12-09", R.drawable.dummy_restaurant_image, "온리원 파스타 송도점")
            )

            add(
                PreviousWaiting("2022-12-12", R.drawable.dummy_restaurant_image, "온리원 파스타 송도점")
                )

            add(
                PreviousWaiting("2022-12-13", R.drawable.dummy_restaurant_image, "온리원 파스타 송도점")
                )

            add(
                PreviousWaiting("2022-12-22", R.drawable.dummy_restaurant_image, "온리원 파스타 송도점")
                )

            add(
                PreviousWaiting("2022-12-25", R.drawable.dummy_restaurant_image, "온리원 파스타 송도점")

                )

            add(
                PreviousWaiting("2022-12-31", R.drawable.dummy_restaurant_image, "온리원 파스타 송도점")

                )

            add(
                PreviousWaiting("2023-01-08", R.drawable.dummy_restaurant_image, "온리원 파스타 송도점")
                )

            add(
                PreviousWaiting("2023-01-22", R.drawable.dummy_restaurant_image, "온리원 파스타 송도점")
                )

            add(
                PreviousWaiting("2023-02-14", R.drawable.dummy_restaurant_image, "온리원 파스타 송도점")
                )
        }

        binding.previousWaitingRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.previousWaitingRecyclerView.setHasFixedSize(true)
        binding.previousWaitingRecyclerView.adapter = PreviousWaitingAdapter(previousWaitingList)



        return root
    }

    inner class PreViewHolder(view: View): RecyclerView.ViewHolder(view){
        private lateinit var prewait: PreviousWaiting

        fun bind(prewait: PreviousWaiting){
            this.prewait = prewait


            itemView.setOnClickListener {

            }
        }
    }

    inner class PreviousWaitingAdapter(private val preList:List<PreviousWaiting>): RecyclerView.Adapter<PreViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreViewHolder {
            val view = layoutInflater.inflate(R.layout.item_previous_waiting_info, parent, false)
            return PreViewHolder(view)
        }

        override fun getItemCount(): Int {
            return previousWaitingList.size
        }

        override fun onBindViewHolder(holder: PreViewHolder, position: Int) {
            val post=preList[position]
            holder.bind(post)
        }

        override fun getItemId(position: Int): Long {
            return super.getItemId(position)
        }

    }

}