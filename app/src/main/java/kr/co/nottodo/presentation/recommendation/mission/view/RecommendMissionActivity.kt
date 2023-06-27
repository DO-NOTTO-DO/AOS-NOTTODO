package kr.co.nottodo.presentation.recommendation.mission.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kr.co.nottodo.databinding.ActivityRecommendMissionBinding
import kr.co.nottodo.presentation.recommendation.action.view.RecommendActionActivity
import kr.co.nottodo.presentation.recommendation.mission.adapter.RecommendMissionAdapter
import kr.co.nottodo.presentation.recommendation.mission.viewmodel.RecommendMissionViewModel
import kr.co.nottodo.presentation.recommendation.model.ParcelizeMissionDetail
import kr.co.nottodo.util.showToast


class RecommendMissionActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRecommendMissionBinding.inflate(layoutInflater) }
    private var recommendMissionAdapter: RecommendMissionAdapter? = null
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
            recommendMissionAdapter?.submitList(emptyList())
        }
    }

    private fun setRecommendMissionSuccessObserver() {
        viewModel.recommendMissionListSuccessResponse.observe(this) { missionList ->
            recommendMissionAdapter?.submitList(missionList)
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
        recommendMissionAdapter = RecommendMissionAdapter(startRecommendActionActivity)
        binding.rvRecommendationMission.adapter = recommendMissionAdapter
    }

    private val startRecommendActionActivity = { id: Int, title: String, situation: String ->
        startActivity(
            Intent(this, RecommendActionActivity::class.java).putExtra(
                MISSION_DETAIL, ParcelizeMissionDetail(id, title, situation)
            )
        )
    }

    private fun setData() {
        viewModel.getRecommendMissionList()
    }

    companion object {
        const val MISSION_DETAIL = "MISSION_DETAIL"
    }
}