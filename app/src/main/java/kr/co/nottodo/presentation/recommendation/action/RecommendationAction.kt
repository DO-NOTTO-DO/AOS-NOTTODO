package kr.co.nottodo.presentation.recommendation.action

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentRecommendationActionTitleBinding
import kr.co.nottodo.presentation.toplevel.recommendation.viewmodel.RecommendationViewModel

class RecommendationAction : AppCompatActivity() {
    private lateinit var binding: FragmentRecommendationActionTitleBinding
    private lateinit var titleAdapter: RecommendationActionTitleAdapter
    private val viewModel = RecommendationViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentRecommendationActionTitleBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        initRecyclerView()

        // 데이터 가져오기
        val situation = intent.getStringExtra("situation")
        val title = intent.getStringExtra("title")

        // 데이터 화면에 표시
        findViewById<TextView>(R.id.tv_recommendation_action_when).text = situation
        findViewById<TextView>(R.id.tv_recommendation_action).text = title

        viewModel.actionTitleList.observe(this) { actionTitleList ->
            titleAdapter.submitList(actionTitleList)
        }


//        viewModel.getRecommendationList( )

        binding.ivRecommendationActionBack.setOnClickListener {
            finish()
        }
    }
//
//    private fun initRecyclerView() {
//        titleAdapter = RecommendationActionTitleAdapter { view, action ->
//            viewModel.getRecommendationActionList(action.id) { recommendationActionList ->
//            }
//        }
//
//        binding.rvRecommendationActionTitle.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = titleAdapter
//        }
//    }
}