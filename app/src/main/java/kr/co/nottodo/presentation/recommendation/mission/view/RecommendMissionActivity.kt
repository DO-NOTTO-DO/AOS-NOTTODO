package kr.co.nottodo.presentation.recommendation.mission.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kr.co.nottodo.databinding.ActivityRecommendMissionBinding
import kr.co.nottodo.presentation.recommendation.mission.adapter.RecommendationMissionAdapter
import kr.co.nottodo.presentation.recommendation.mission.viewmodel.RecommendMissionViewModel
import kr.co.nottodo.util.showToast


class RecommendMissionActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRecommendMissionBinding.inflate(layoutInflater) }
    private var recommendationMissionAdapter: RecommendationMissionAdapter? = null
    private val viewModel: RecommendMissionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setData()
        setViews()
        setClickEvents()
        setObservers()
    }

    private fun setObservers() {
        setRecommendMissionObserver()
    }

    private fun setRecommendMissionObserver() {
        setRecommendMissionSuccessObserver()
        setRecommendMissionFailureObserver()
    }

    private fun setRecommendMissionFailureObserver() {
        viewModel.recommendMissionListErrorResponse.observe(this) { errorMessage ->
            showToast(errorMessage)
            recommendationMissionAdapter?.submitList(emptyList())
        }
    }

    private fun setRecommendMissionSuccessObserver() {
        viewModel.recommendMissionListSuccessResponse.observe(this) { missionList ->
            recommendationMissionAdapter?.submitList(missionList)
        }
    }

    private fun setClickEvents() {
        setDestroyBtnClickEvent()
    }

    private fun setDestroyBtnClickEvent() {
        binding.ivRecommendationMissionDestroy.setOnClickListener {
            if (!isFinishing) finish()
        }
    }

    private fun setViews() {
        setContentView(binding.root)
        setRecyclerViews()
    }

    private fun setRecyclerViews() {
        setRecommendMissionRecyclerView()
    }

    private fun setRecommendMissionRecyclerView() {
        setAdapter()
    }

    private fun setAdapter() {
        recommendationMissionAdapter = RecommendationMissionAdapter()
        binding.rvRecommendationMission.adapter = recommendationMissionAdapter
    }

    private fun setData() {
        viewModel.getRecommendMissionList()
    }
}