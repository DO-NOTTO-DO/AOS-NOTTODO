package kr.co.nottodo.presentation.recommendation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.nottodo.databinding.ActivityRecommendationMainBinding
import kr.co.nottodo.presentation.addition.view.AdditionActivity

import kr.co.nottodo.presentation.recommendation.action.RecommendationActionActivity
import kr.co.nottodo.presentation.recommendation.viewmodel.RecommendationMissionViewModel


class RecommendationMissionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecommendationMainBinding
    private lateinit var adapter: RecommendationMissionAdapter
    private val viewModel: RecommendationMissionViewModel by viewModels()

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
        adapter = RecommendationMissionAdapter()

        // RecyclerView에 어댑터 설정
        recyclerView.adapter = adapter

        // 추천 메인 목록을 관찰하여 데이터 갱신
        viewModel.mainList.observe(this) { MainMissionList ->
            adapter.submitList(MainMissionList)
        }

        // 임의의 더미 데이터 대신 서버에서 추천 메인 목록을 가져옴
        viewModel.fetchRecommendationMainList()

        adapter.setOnItemClickListener(object :
            RecommendationMissionAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = adapter.getItemAtPosition(position)
                val intent = Intent(
                    this@RecommendationMissionActivity,
                    RecommendationActionActivity::class.java
                )
                intent.putExtra("situation", item.situation)
                intent.putExtra("title", item.title)
                startActivity(intent)
            }
        })

        val exitButton = binding.ivRecommendationMainExit
        exitButton.setOnClickListener { finish() }

        binding.btnWriteDirectly.setOnClickListener {
            startActivity(Intent(this, AdditionActivity::class.java))
        }
    }
}