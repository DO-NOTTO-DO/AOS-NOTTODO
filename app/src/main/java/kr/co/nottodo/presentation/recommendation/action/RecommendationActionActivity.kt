package kr.co.nottodo.presentation.recommendation.action

import RecommendationActionListAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.R
import kr.co.nottodo.presentation.recommendation.main.RecommendationMainActivity
import kr.co.nottodo.presentation.recommendation.viewmodel.RecommendationViewModel

class RecommendationActionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecommendationActionListAdapter
    private lateinit var viewModel: RecommendationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendation_action_title)

        val backButton: ImageView = findViewById(R.id.iv_recommendation_action_back)
        backButton.setOnClickListener {

            // RecommendationMainActivity로 전환
            val intent = Intent(this, RecommendationMainActivity::class.java)
            startActivity(intent)
        }

        // RecommendationViewModel 인스턴스 생성
        viewModel = ViewModelProvider(this).get(RecommendationViewModel::class.java)

        // 리사이클러뷰 초기화
        recyclerView = findViewById(R.id.rv_recommendation_action_category)
        adapter = RecommendationActionListAdapter()
        recyclerView.adapter = adapter

        // LinearLayoutManager 생성 및 설정
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager


        // 아이템 클릭 리스너 설정
        adapter.setOnItemClickListener(object :
            RecommendationActionListAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // 클릭된 아이템의 상태 변경
                adapter.setSelectedItem(position)

                // 변경된 상태를 반영하여 아이템 갱신
                adapter.notifyItemRangeChanged(0, adapter.itemCount)
            }
        })


        val dummyData = listOf(
            RecommendationActionListDTO.ActionList.CategoryList("Category 1"),
            RecommendationActionListDTO.ActionList.CategoryList("Category 2"),
            RecommendationActionListDTO.ActionList.CategoryList("Category 3")
        )

//      //어댑터에 더미 데이터 설정
        adapter.submitList(dummyData)

//        // RecommendationViewModel에서 categoryList 데이터를 관찰
//        // 리사이클러뷰에 데이터 설정
//        viewModel.categoryList.observe(this) { categoryList ->
//            adapter.submitList(categoryList)
//        }
//
//        // RecommendationViewModel에서 categoryId 데이터를 관찰
//        // fetchRecommendationCategoryList 호출
//        viewModel.categoryId.observe(this) { categoryId ->
//            viewModel.fetchRecommendationCategoryList(categoryId)
//        }
//
//        // categoryId를 Intent에서 가져와서 RecommendationViewModel fetchRecommendationCategoryList 호출
//        val categoryId = intent.getIntExtra("categoryId", -1)
//        if (categoryId != -1) {
//            viewModel.fetchRecommendationCategoryList(categoryId)
//        }
    }
}