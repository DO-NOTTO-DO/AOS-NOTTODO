package kr.co.nottodo.presentation.recommendation.main

import RecommendationActionActivity
import RecommendationMainListViewAdapter
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.nottodo.databinding.ActivityRecommendationMainBinding
import kr.co.nottodo.presentation.recommendation.viewmodel.RecommendationViewModel
class RecommendationMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecommendationMainBinding
    private lateinit var adapter: RecommendationMainListViewAdapter
    private val viewModel: RecommendationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView 가져오기
        val recyclerView = binding.rvRecommendation

        // LinearLayoutManager 생성 및 설정
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // 어댑터 인스턴스 생성
        adapter = RecommendationMainListViewAdapter()

        // RecyclerView에 어댑터 설정
        recyclerView.adapter = adapter

        // RecommendationViewModel에서 fetchRecommendationMainList() 호출하여 데이터 가져오기
        viewModel.fetchRecommendationMainList()

        // RecommendationViewModel의 mainList를 관찰하고 데이터 변경 시 어댑터에 데이터 업데이트
        viewModel.mainList.observe(this) { mainList ->
            adapter.submitList(mainList)
        }

        // 아이템 클릭 이벤트 처리
        adapter.setOnItemClickListener(object : RecommendationMainListViewAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = adapter.getItemAtPosition(position)
                val intent = Intent(this@RecommendationMainActivity, RecommendationActionActivity::class.java)
                intent.putExtra("situation", item.situation)
                intent.putExtra("title", item.title)
                startActivity(intent)
            }
        })

        val exitButton = binding.ivRecommendationMainExit
        exitButton.setOnClickListener { finish() }
    }
}
