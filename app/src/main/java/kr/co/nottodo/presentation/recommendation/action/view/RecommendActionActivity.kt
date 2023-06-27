package kr.co.nottodo.presentation.recommendation.action.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kr.co.nottodo.databinding.ActivityRecommendationActionBinding
import kr.co.nottodo.presentation.recommendation.action.adapter.RecommendationActionAdapter
import kr.co.nottodo.presentation.recommendation.action.viewmodel.RecommendActionViewModel

class RecommendActionActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRecommendationActionBinding.inflate(layoutInflater) }
    private lateinit var recommendationActionAdapter: RecommendationActionAdapter
    private val viewModel by viewModels<RecommendActionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        // 레이아웃의 TextView를 참조
//
//        // RecommendationMainActivity에서 전달된 데이터 수신
//        val situation = intent.getStringExtra("situation")
//        val title = intent.getStringExtra("title")
//
//        // 수신한 데이터를 해당 TextView에 설정
//        binding = situation
//        tvTitle.text = title
//
//        val backButton: ImageView = findViewById(R.id.iv_recommendation_action_back)
//        backButton.setOnClickListener {
//
//            // RecommendationMainActivity로 전환
//            val intent = Intent(this, RecommendMissionActivity::class.java)
//            startActivity(intent)
//        }
//
//        // RecommendMissionViewModel 인스턴스 생성
//        viewModel = ViewModelProvider(this).get(RecommendMissionViewModel::class.java)
//
//        // 리사이클러뷰 초기화
//        recyclerView = findViewById(R.id.rv_recommendation_action_category)
//        adapter = RecommendationActionAdapter()
//        recyclerView.adapter = adapter
//
//        // LinearLayoutManager 생성 및 설정
//        val layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = layoutManager
//
//        // 카테고리 목록을 관찰하여 데이터 갱신
//        viewModel.categoryList.observe(this, { categoryList ->
//            adapter.submitList(categoryList)
//        })
//
//        // 추천 카테고리 목록을 가져오는 함수 호출
//        val categoryId = intent.getIntExtra("categoryId", -1)
//        viewModel.fetchRecommendationCategoryList(categoryId)
//
//        // 아이템 클릭 리스너 설정
//        adapter.setOnItemClickListener(object :
//            RecommendationActionAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int) {
//                // 클릭된 아이템의 상태 변경
//                adapter.setSelectedItem(position)
//
//                // 변경된 상태를 반영하여 아이템 갱신
//                adapter.notifyItemRangeChanged(0, adapter.itemCount)
//            }
//        })
    }
}