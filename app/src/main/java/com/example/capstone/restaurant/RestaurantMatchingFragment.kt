package com.example.capstone.restaurant

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.capstone.R
import com.example.capstone.Restaurants
import com.example.capstone.databinding.FragmentRestaurantMatchingBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet

class RestaurantMatchingFragment : Fragment() {
    private var _binding: FragmentRestaurantMatchingBinding? = null
    private val binding get() = _binding!!
    lateinit var resInfo:Restaurants

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestaurantMatchingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.chart.setUsePercentValues(true)
        binding.chart.holeRadius=30f
        binding.chart.transparentCircleRadius=35f
        // 성별 데이터 입력
        val sex_entries = ArrayList<PieEntry>()
        sex_entries.add(PieEntry(25f, "남"))
        sex_entries.add(PieEntry(60f, "여"))
        sex_entries.add(PieEntry(15f, "기타"))

        // 차트 색
        val colorsItems = ArrayList<Int>()
        colorsItems.add(Color.parseColor("#0A4A9B"))
        colorsItems.add(Color.parseColor("#FF8D8D"))
        colorsItems.add(Color.parseColor("#FCAF17"))

        val pieDataSet = PieDataSet(sex_entries, "")
        pieDataSet.apply {
            colors = colorsItems
            valueTextColor = Color.WHITE
            valueTextSize = 10f
        }
        val pieData = PieData(pieDataSet)
        binding.chart
            .apply {
            data = pieData
            description.isEnabled = false
            isRotationEnabled = false
            setEntryLabelColor(Color.WHITE)
            animateY(1400, Easing.EaseInOutQuad)
            animate()
        }

        //--------------------------------------------------------------------------
        var barChart: BarChart = binding.barChart // barChart 생성

        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(1f,20.0f))
        entries.add(BarEntry(2f,70.0f))
        entries.add(BarEntry(3f,30.0f))
        entries.add(BarEntry(4f,90.0f))
        entries.add(BarEntry(5f,70.0f))

        barChart.run {
            description.isEnabled = false // 차트 옆에 별도로 표기되는 description을 안보이게 설정 (false)
            setMaxVisibleValueCount(5) // 최대 보이는 그래프 개수를 5개로 지정
            setPinchZoom(false) // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
            setDrawBarShadow(false) //그래프의 그림자
            setDrawGridBackground(false)//격자구조 넣을건지
            axisLeft.run { //왼쪽 축. 즉 Y방향 축을 뜻한다.
                axisMaximum = 100f //100 위치에 선을 그리기 위해 101f로 맥시멈값 설정
                axisMinimum = 0f // 최소값 0
                granularity = 50f // 50 단위마다 선을 그리려고 설정.
                setDrawLabels(true) // 값 적는거 허용 (0, 50, 100)
                setDrawGridLines(true) //격자 라인 활용
                setDrawAxisLine(false) // 축 그리기 설정
                axisLineColor = ContextCompat.getColor(context, R.color.LightGray) // 축 색깔 설정
                gridColor = ContextCompat.getColor(context, R.color.LightGray) // 축 아닌 격자 색깔 설정
                textColor = ContextCompat.getColor(context, R.color.black) // 라벨 텍스트 컬러 설정
                textSize = 10f //라벨 텍스트 크기
            }
            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM //X축을 아래에다가 둔다.
                granularity = 1f // 1 단위만큼 간격 두기
                setDrawAxisLine(true) // 축 그림
                setDrawGridLines(false) // 격자
                textColor = ContextCompat.getColor(context, R.color.black) //라벨 색상
                textSize = 10f // 텍스트 크기
                valueFormatter = MyXAxisFormatter() // X축 라벨값(밑에 표시되는 글자) 바꿔주기 위해 설정
            }
            axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌.
            setTouchEnabled(false) // 그래프 터치해도 아무 변화없게 막음
            legend.isEnabled = false //차트 범례 설정
        }

        var set = BarDataSet(entries,"DataSet") // 데이터셋 초기화
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

        //---------------------------------------------------------------------------------
        val bundle = arguments
        resInfo=bundle!!.getSerializable("restaurant") as Restaurants

        if(resInfo.keyWord !=null){
            var arr:List<String> =listOf("", "", "")
            for (addr in resInfo.keyWord) {
                val splitedAddr = resInfo.keyWord.split("[\"", "\", \"", "\"]")
                arr = splitedAddr
            }
            Log.d("hy", arr.toString())
            binding.BestKeyword.text="#"+arr[1]
            binding.Bestkeyword2.text="#"+arr[2]
            binding.Bestkeyword3.text="#"+arr[3]
        }
        return root

    }
    class MyXAxisFormatter : ValueFormatter() {
        private val days = arrayOf("10대","20대","30대","40대","50+")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()-1) ?: value.toString()
        }
    }
}