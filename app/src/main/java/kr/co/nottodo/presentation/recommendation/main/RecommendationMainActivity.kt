package kr.co.nottodo.presentation.recommendation.main

import RecommendationMainListViewAdapter
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.nottodo.databinding.ActivityRecommendationMainBinding

import kr.co.nottodo.presentation.recommendation.action.RecommendationActionActivity
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


        // 임의의 더미 데이터 생성
        val dummyData = listOf(
            RecommendationMainListDTO.MainList(
                1,
                "업무시간 중",
                "유튜브 보지 않기",
                "유튜브를 보지 않는 것이 당신의 일상에\n 어떠한 변화를 일으킬까요?\n행복한 중독해소를 위해 제안해요!",
                "image1.jpg"
            ),
            RecommendationMainListDTO.MainList(
                2,
                "취침 전",
                "커피 마시지 않기",
                "한국인들은 평균 2잔의 커피를 마신대요.\n적당한 섭취를 위해 제안해요!",
                "image2.jpg"
            ),
            RecommendationMainListDTO.MainList(
                3,
                "취침 전",
                "커피 마시지 않기",
                "한국인들은 평균 2잔의 커피를 마신대요.\n적당한 섭취를 위해 제안해요!",
                "image2.jpg"
            )
        )


        // 어댑터에 더미 데이터 설정
        adapter.submitList(dummyData)

        adapter.setOnItemClickListener(object :
            RecommendationMainListViewAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = adapter.getItemAtPosition(position)
                val intent = Intent(
                    this@RecommendationMainActivity,
                    RecommendationActionActivity::class.java
                )
                intent.putExtra("situation", item.situation)
                intent.putExtra("title", item.title)
                startActivity(intent)
            }
        })


        val exitButton = binding.ivRecommendationMainExit
        exitButton.setOnClickListener { finish() }
    }
}
