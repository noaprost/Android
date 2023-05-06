package com.example.capstone.restaurant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.capstone.*
import com.example.capstone.databinding.FragmentRestaurantStateBinding
import com.example.capstone.retrofit.API
import com.example.capstone.retrofit.IRetrofit
import com.example.capstone.retrofit.RetrofitClient
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantStateFragment : Fragment() {

    private var _binding: FragmentRestaurantStateBinding? = null
    private val binding get() = _binding!!
    lateinit var resInfo:Restaurants
    lateinit var HourData:complexityByHour
    lateinit var DateData:complexityByDate
    var total=0.toFloat()
    var total2=0.toFloat()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestaurantStateBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val bundle = arguments
        resInfo=bundle!!.getSerializable("restaurant") as Restaurants
        complexityByHour(resPhNum("032 934 6188"))
        complexityByDate(resPhNum("032 934 6188"))

        val seatKeywordBox:List<LinearLayout> =listOf(binding.seatKeywordBox1,binding.seatKeywordBox2, binding.seatKeywordBox3, binding.seatKeywordBox4, binding.seatKeywordBox5, binding.seatKeywordBox6, binding.seatKeywordBox7, binding.seatKeywordBox8 )
        val seatKeywordList:List<TextView> =listOf(binding.seatKeyword1, binding.seatKeyword2, binding.seatKeyword3, binding.seatKeyword4, binding.seatKeyword5, binding.seatKeyword6, binding.seatKeyword7, binding.seatKeyword8)
        val seatKeywordMaxList:List<TextView> =listOf(binding.seat1Max, binding.seat2Max, binding.seat3Max, binding.seat4Max, binding.seat5Max, binding.seat6Max, binding.seat7Max, binding.seat8Max)
        var arr:List<String> =listOf("", "", "")
        var arr1:List<String> =listOf("", "", "")
        if(resInfo.resSeat.isNullOrEmpty()){
            seatKeywordList[0].text="해당 가게는 좌석 정보를 제공하지 않습니다."
            seatKeywordBox[0].visibility=View.VISIBLE
            seatKeywordMaxList[0].text=""
        }else{
            for (addr in resInfo.resSeat) {
                arr =  resInfo.resSeat.split(", ")
                arr1 =  resInfo.resSeatCnt.split(",")
            }
            var n=0
            for(i in arr){
                seatKeywordList[n].text="#"+arr[n]
                seatKeywordBox[n].visibility=View.VISIBLE
                seatKeywordMaxList[n].text=arr1[n]
                n+=1
            }
        }
        return root
    }
    fun showComplexityByHour(){
        val barChart: BarChart = binding.barChart // barChart 생성

        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(1f,HourData._0.toFloat()/total*100))
        entries.add(BarEntry(2f,HourData._2.toFloat()/total*100))
        entries.add(BarEntry(3f,HourData._4.toFloat()/total*100))
        entries.add(BarEntry(4f,HourData._6.toFloat()/total*100))
        entries.add(BarEntry(5f,HourData._8.toFloat()/total*100))
        entries.add(BarEntry(6f,HourData._10.toFloat()/total*100))
        entries.add(BarEntry(7f,HourData._12.toFloat()/total*100))
        entries.add(BarEntry(8f,HourData._14.toFloat()/total*100))
        entries.add(BarEntry(9f,HourData._16.toFloat()/total*100))
        entries.add(BarEntry(10f,HourData._18.toFloat()/total*100))
        entries.add(BarEntry(11f,HourData._20.toFloat()/total*100))
        entries.add(BarEntry(12f,HourData._22.toFloat()/total*100))

        barChart.run {
            description.isEnabled = false // 차트 옆에 별도로 표기되는 description을 안보이게 설정 (false)
            setMaxVisibleValueCount(12) // 최대 보이는 그래프 개수
            axisLeft.run { //왼쪽 축. 즉 Y방향 축을 뜻한다.
                axisMaximum = 101f //100 위치에 선을 그리기 위해 101f로 맥시멈값 설정
                axisMinimum = 0f // 최소값 0
                granularity = 20f // 20 단위마다 선을 그리려고 설정.
                setDrawLabels(true) // 값 적는거 허용
                setDrawGridLines(true) //격자 라인 활용
                setDrawAxisLine(false) // 축 그리기 설정
                axisLineColor = ContextCompat.getColor(context, R.color.LightGray) // 축 색깔 설정
                gridColor = ContextCompat.getColor(context, R.color.LightGray) // 축 아닌 격자 색깔 설정
                textColor = ContextCompat.getColor(context, R.color.black) // 라벨 텍스트 컬러 설정
                textSize = 10f //라벨 텍스트 크기
            }
            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM //X축을 아래에다가 둔다.
                xAxis.labelCount=12
                granularity = 0f // 1 단위만큼 간격 두기
                setDrawAxisLine(true) // 축 그림
                setDrawGridLines(false) // 격자
                textColor = ContextCompat.getColor(context, R.color.black) //라벨 색상
                textSize = 11f // 텍스트 크기
                valueFormatter =
                    StateFragmentXAxisFormatter() // X축 라벨값(밑에 표시되는 글자) 바꿔주기 위해 설정
            }
            axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌.
            setTouchEnabled(false) // 그래프 터치해도 아무 변화없게 막음
            animateY(1000) // 밑에서부터 올라오는 애니매이션 적용
            legend.isEnabled = false //차트 범례 설정
        }

        val set = BarDataSet(entries,"DataSet") // 데이터셋 초기화
        set.color = ContextCompat.getColor(requireContext(), R.color.INUYellow) // 바 그래프 색 설정

        val dataSet :ArrayList<IBarDataSet> = ArrayList()
        dataSet.add(set)

        val data = BarData(dataSet)
        data.barWidth = 0.5f //막대 너비 설정
        barChart.run {
            this.data = data //차트의 데이터를 data로 설정해줌.
            setFitBars(true)
            invalidate()
        }
    }
    fun showComplexityByDate(){
        val barChart: BarChart = binding.barChart2

        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(1f, if (DateData.Mon==0) DateData.Mon.toFloat() else DateData.Mon.toFloat()/total2*100f))
        entries.add(BarEntry(2f, if (DateData.Mon==0) DateData.Tue.toFloat() else DateData.Tue.toFloat()/total2*100f))
        entries.add(BarEntry(3f, if (DateData.Mon==0) DateData.Wed.toFloat() else DateData.Wed.toFloat()/total2*100f))
        entries.add(BarEntry(4f, if (DateData.Mon==0) DateData.Thu.toFloat() else DateData.Thu.toFloat()/total2*100f))
        entries.add(BarEntry(5f, if (DateData.Mon==0) DateData.Fri.toFloat() else DateData.Fri.toFloat()/total2*100f))
        entries.add(BarEntry(6f, if (DateData.Mon==0) DateData.Sat.toFloat() else DateData.Sat.toFloat()/total2*100f))
        entries.add(BarEntry(7f, if (DateData.Mon==0) DateData.Sun.toFloat() else DateData.Sun.toFloat()/total2*100f))

        barChart.run {
            description.isEnabled = false
            setMaxVisibleValueCount(7)
            axisLeft.run {
                axisMaximum = 101f
                axisMinimum = 0f
                granularity = 20f
                setDrawLabels(true)
                setDrawGridLines(true)
                setDrawAxisLine(false)
                axisLineColor = ContextCompat.getColor(context, R.color.LightGray)
                gridColor = ContextCompat.getColor(context, R.color.LightGray)
                textColor = ContextCompat.getColor(context, R.color.black)
                textSize = 10f
            }
            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM
                xAxis.labelCount=7
                granularity = 0f
                setDrawAxisLine(true)
                setDrawGridLines(false)
                textColor = ContextCompat.getColor(context, R.color.black)
                textSize = 12f
                valueFormatter = MyXAxisFormatter2()
            }
            axisRight.isEnabled = false
            setTouchEnabled(false)
            animateY(1000)
            legend.isEnabled = false
        }

        val set = BarDataSet(entries,"DataSet")
        set.color = ContextCompat.getColor(requireContext(), R.color.INUBlue) // 바 그래프 색 설정

        val dataSet :ArrayList<IBarDataSet> = ArrayList()
        dataSet.add(set)

        val data = BarData(dataSet)
        data.barWidth = 0.5f //막대 너비 설정
        barChart.run {
            this.data = data //차트의 데이터를 data로 설정해줌.
            setFitBars(false)
            invalidate()
        }
    }
    inner class StateFragmentXAxisFormatter : ValueFormatter() {
        private val days = arrayOf("0시","2시","4시","6시","8시","10시","12시","14시", "16시" ,"18시","20시" ,"22시" )
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()-1) ?: value.toString()
        }
    }
    inner class MyXAxisFormatter2 : ValueFormatter() {
        private val days = arrayOf("월", "화", "수", "목", "금", "토", "일")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()-1) ?: value.toString()
        }
    }

    private fun complexityByHour(resPhNum: resPhNum){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.complexityByHour(resPhNum) ?:return
        call.enqueue(object : Callback<List<complexityByHour>> {

            override fun onResponse(call: Call<List<complexityByHour>>, response: Response<List<complexityByHour>>) {
                Log.d("retrofit", "시간 복잡도 - 응답 성공 / t : ${response.raw()} ${response.body()}")
                val data=response.body()
                HourData= data!![0]
                total=HourData._0+HourData._2+HourData._4+HourData._6+HourData._8+HourData._10+HourData._12+HourData._14+HourData._16+HourData._18+HourData._20+HourData._22.toFloat()
                showComplexityByHour()
            }
            override fun onFailure(call: Call<List<complexityByHour>>, t: Throwable) {
                Log.d("retrofit", "시간 복잡도 -  응답 실패 / t: $t")
            }
        })
    }
    private fun complexityByDate(resPhNum: resPhNum){
        val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        val call = iRetrofit?.complexityByDate(resPhNum) ?:return
        call.enqueue(object : Callback<List<complexityByDate>> {

            override fun onResponse(call: Call<List<complexityByDate>>, response: Response<List<complexityByDate>>) {
                Log.d("retrofit", "요일 복잡도 - 응답 성공 / t : ${response.raw()} ${response.body()}")
                val data=response.body()
                DateData= data!![0]
                total2=DateData.Mon+DateData.Tue+DateData.Wed+DateData.Thu+DateData.Fri+DateData.Sat+DateData.Sun.toFloat()
                showComplexityByDate()
            }
            override fun onFailure(call: Call<List<complexityByDate>>, t: Throwable) {
                Log.d("retrofit", "요일 복잡도 -  응답 실패 / t: $t")
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavi(false)
        _binding = null
    }
}