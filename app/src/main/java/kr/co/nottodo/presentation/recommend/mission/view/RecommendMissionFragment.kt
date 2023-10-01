package kr.co.nottodo.presentation.recommend.mission.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentRecommendMissionBinding
import kr.co.nottodo.presentation.addition.view.AdditionActivity
import kr.co.nottodo.presentation.base.fragment.ViewBindingFragment
import kr.co.nottodo.presentation.recommend.action.view.RecommendActionActivity
import kr.co.nottodo.presentation.recommend.mission.adapter.RecommendMissionAdapter
import kr.co.nottodo.presentation.recommend.mission.adapter.RecommendMissionAdapter.RecommendMissionItemDecoration
import kr.co.nottodo.presentation.recommend.mission.viewmodel.RecommendMissionViewModel
import kr.co.nottodo.presentation.recommend.model.RecommendMissionUiModel
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent
import kr.co.nottodo.util.NotTodoAmplitude.trackEventWithProperty
import kr.co.nottodo.util.PublicString.NO_INTERNET_CONDITION_ERROR
import kr.co.nottodo.util.showNotTodoSnackBar
import kr.co.nottodo.util.showToast


class RecommendMissionFragment : ViewBindingFragment<FragmentRecommendMissionBinding>() {
    private val viewModel: RecommendMissionViewModel by viewModels()
    private var recommendMissionAdapter: RecommendMissionAdapter? = null

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
            if (errorMessage == NO_INTERNET_CONDITION_ERROR) requireContext().showNotTodoSnackBar(
                binding.root, NO_INTERNET_CONDITION_ERROR
            ) else {
                requireContext().showToast(errorMessage)
            }
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
            startActivity(Intent(requireContext(), AdditionActivity::class.java))
            if (!requireActivity().isFinishing) requireActivity().finish()
        }
    }

    private fun setDestroyBtnClickEvent() {
        binding.ivRecommendMissionDestroy.setOnClickListener {
            if (!requireActivity().isFinishing) requireActivity().finish()
        }
    }

    private fun setViews() {
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
                    Intent(requireContext(), RecommendActionActivity::class.java).putExtra(
                        MISSION_DETAIL, RecommendMissionUiModel(id, title, situation, image)
                    )
                )
                if (!requireActivity().isFinishing) requireActivity().finish()
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

    override fun setBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentRecommendMissionBinding =
        FragmentRecommendMissionBinding.inflate(inflater, container, false)
}