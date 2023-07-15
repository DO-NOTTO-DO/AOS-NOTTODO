package kr.co.nottodo.presentation.recommendation.mission.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ActivityRecommendMissionBinding
import kr.co.nottodo.presentation.addition.view.AdditionActivity
import kr.co.nottodo.presentation.recommendation.action.view.RecommendActionActivity
import kr.co.nottodo.presentation.recommendation.mission.adapter.RecommendMissionAdapter
import kr.co.nottodo.presentation.recommendation.mission.adapter.RecommendMissionAdapter.RecommendMissionItemDecoration
import kr.co.nottodo.presentation.recommendation.mission.viewmodel.RecommendMissionViewModel
import kr.co.nottodo.presentation.recommendation.model.RecommendMissionUiModel
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent
import kr.co.nottodo.util.NotTodoAmplitude.trackEventWithProperty
import kr.co.nottodo.util.showToast


class RecommendMissionActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRecommendMissionBinding.inflate(layoutInflater) }
    private var recommendMissionAdapter: RecommendMissionAdapter? = null
    private val viewModel: RecommendMissionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trackEvent(getString(R.string.view_recommend_mission))
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
            trackEvent(getString(R.string.click_self_create_mission))
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
                trackEventWithProperty(
                    getString(R.string.click_recommend_mission), mapOf(
                        getString(R.string.situation) to situation,
                        getString(R.string.title) to title
                    )
                )
                startActivity(
                    Intent(this, RecommendActionActivity::class.java).putExtra(
                        MISSION_DETAIL, RecommendMissionUiModel(id, title, situation, image)
                    )
                )
                if (!isFinishing) finish()
            }
        recommendMissionAdapter = RecommendMissionAdapter(startRecommendActionActivity)
        binding.rvRecommendMission.adapter = recommendMissionAdapter
        binding.rvRecommendMission.addItemDecoration(RecommendMissionItemDecoration())
    }

    private fun setData() {
        viewModel.getRecommendMissionList()
    }

    companion object {
        const val MISSION_DETAIL = "MISSION_DETAIL"
    }
}