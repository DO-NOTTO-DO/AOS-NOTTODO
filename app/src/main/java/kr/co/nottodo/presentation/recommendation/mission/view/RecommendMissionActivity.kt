package kr.co.nottodo.presentation.recommendation.mission.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kr.co.nottodo.databinding.ActivityRecommendMissionBinding
import kr.co.nottodo.presentation.addition.view.AdditionActivity
import kr.co.nottodo.presentation.recommendation.action.view.RecommendActionActivity
import kr.co.nottodo.presentation.recommendation.mission.adapter.RecommendMissionAdapter
import kr.co.nottodo.presentation.recommendation.mission.viewmodel.RecommendMissionViewModel
import kr.co.nottodo.presentation.recommendation.model.RecommendMissionUiModel
import kr.co.nottodo.presentation.recommendation.util.showToast


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
        setWriteDirectlyBtnClickEvent()
    }

    private fun setWriteDirectlyBtnClickEvent() {
        binding.fabRecommendMissionWriteDirectly.setOnClickListener {
            startActivity(Intent(this, AdditionActivity::class.java))
            if (!isFinishing) finish()
        }
    }

    private fun setDestroyBtnClickEvent() {
        binding.ivRecommendMissionDestroy.setOnClickListener {
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
        val startRecommendActionActivity =
            { id: Int, title: String, situation: String, image: String ->
                startActivity(
                    Intent(this, RecommendActionActivity::class.java).putExtra(
                        MISSION_DETAIL, RecommendMissionUiModel(id, title, situation, image)
                    )
                )
                if (!isFinishing) finish()
            }
        recommendMissionAdapter = RecommendMissionAdapter(startRecommendActionActivity)
        binding.rvRecommendMission.adapter = recommendMissionAdapter
    }

    private fun setData() {
        viewModel.getRecommendMissionList()
    }

    companion object {
        const val MISSION_DETAIL = "MISSION_DETAIL"
    }
}