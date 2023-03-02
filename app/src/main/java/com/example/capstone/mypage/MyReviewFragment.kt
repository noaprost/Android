package com.example.capstone.mypage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.*
import com.example.capstone.databinding.FragmentMyReviewBinding

class MyReviewFragment : Fragment(), ConfirmDialogInterface {
    private var _binding: FragmentMyReviewBinding? = null
    private val binding get() = _binding!!
    private var dummy = ArrayList<MyReview>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyReviewBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.backButton.setOnClickListener {
            destroy()
        }
        dummy.apply {
            add(MyReview(
                R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(
                R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(
                R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(
                R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(
                R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(
                R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(
                R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(
                R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(
                R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(
                R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
            add(MyReview(
                R.drawable.dummy_food_image, "직원분도 친절하시고 음식도 너무 맛있어요!  인테리어도 예뻐서 애인이랑 오기 좋을 것 같아요  특히 라구파스타 최고,,", "어제",
                3.8.toLong(), "데이트하기 좋은", "인스타감성", "조용한", "온리원파스타"))
        }
        binding.myReviewRecyclerView.layoutManager= LinearLayoutManager(context)
        binding.myReviewRecyclerView.adapter=MyAdapter(dummy)
        return root
    }
    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        private lateinit var review: MyReview
        private val reviewComment: TextView =itemView.findViewById(R.id.reviewComment)
        private val deleteButton: TextView = itemView.findViewById(R.id.myReviewDelete)
        private val myReviewInfoBox: LinearLayout = itemView.findViewById(R.id.myReviewInfoBox)
        //todo 리사이클러뷰 연결

        fun bind(review: MyReview){
            this.review=review
            reviewComment.text=this.review.comment

            myReviewInfoBox.setOnClickListener {
                val mainAct = activity as MainActivity
                mainAct.ChangeFragment("Restaurant")
            }
            deleteButton.setOnClickListener{
                val dialog = CustomDialog(this@MyReviewFragment, "리뷰를 삭제하시겠습니까?\n재작성은 불가능합니다.", 0, 0)
                dialog.isCancelable = false
                this@MyReviewFragment.fragmentManager?.let { it1 -> dialog.show(it1, "ConfirmDialog") }
                //todo 리사이클러뷰 새로고침
            }
        }

    }
    inner class MyAdapter(private val list:List<MyReview>): RecyclerView.Adapter<MyViewHolder>(){
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