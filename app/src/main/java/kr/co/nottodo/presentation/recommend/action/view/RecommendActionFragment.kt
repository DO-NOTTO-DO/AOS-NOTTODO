package kr.co.nottodo.presentation.recommend.action.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentRecommendActionBinding
import kr.co.nottodo.presentation.addition.view.AdditionActivity
import kr.co.nottodo.presentation.base.fragment.DataBindingFragment
import kr.co.nottodo.presentation.recommend.action.adapter.RecommendActionAdapter
import kr.co.nottodo.presentation.recommend.action.viewmodel.RecommendActionViewModel
import kr.co.nottodo.presentation.recommend.mission.view.RecommendMissionActivity
import kr.co.nottodo.presentation.recommend.mission.view.RecommendMissionActivity.Companion.MISSION_DETAIL
import kr.co.nottodo.presentation.recommend.model.RecommendMissionUiModel
import kr.co.nottodo.presentation.recommend.model.RecommendUiModel
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent
import kr.co.nottodo.util.NotTodoAmplitude.trackEventWithProperty
import kr.co.nottodo.util.PublicString.NO_INTERNET_CONDITION_ERROR
import kr.co.nottodo.util.getParcelable
import kr.co.nottodo.util.showNotTodoSnackBar
import kr.co.nottodo.util.showToast

class RecommendActionFragment :
    DataBindingFragment<FragmentRecommendActionBinding>(R.layout.fragment_recommend_action) {

    private var recommendActionAdapter: RecommendActionAdapter? = null
    private val viewModel by viewModels<RecommendActionViewModel>()
    private val dataFromRecommendMissionActivity by lazy {
        val recommendMissionUiModel = requireActivity().intent.getParcelable(
            MISSION_DETAIL, RecommendMissionUiModel::class.java
        )
        if (recommendMissionUiModel == null) {
            if (!requireActivity().isFinishing) requireActivity().finish()
        }
        requireNotNull(recommendMissionUiModel) {
            getString(
                R.string._is_null, getString(R.string.recommend_mission_ui_model)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setData()
        setViews()
        setClickEvents()
        setObservers()
    }

    private fun setData() {
        getDataFromRecommendMissionActivity()
        getRecommendActionList()
    }

    private fun getRecommendActionList() {
        viewModel.getRecommendActionList()
    }

    private fun getDataFromRecommendMissionActivity() {
        trackEventWithProperty(
            getString(R.string.view_recommend_mission_detail), mapOf(
                getString(R.string.situation) to dataFromRecommendMissionActivity.situation,
                getString(R.string.title) to dataFromRecommendMissionActivity.title

            )
        )
        viewModel.setMissionId(
            dataFromRecommendMissionActivity.id
        )
    }

    private fun setViews() {
        setRecommendActionRecyclerView()
        setMissionTextView()
        setSituationTextView()
        setMissionImageView()
    }

    private fun setMissionTextView() {
        binding.tvRecommendActionMission.text = dataFromRecommendMissionActivity.title
    }

    private fun setSituationTextView() {
        binding.tvRecommendActionSituation.text = dataFromRecommendMissionActivity.situation
    }

    private fun setMissionImageView() {
        binding.ivRecommendActionMissionSituation.load(dataFromRecommendMissionActivity.image)
    }

    private fun setRecommendActionRecyclerView() {
        recommendActionAdapter = RecommendActionAdapter(
            plusSelectedActionsCount = viewModel.plusSelectedActionsCount,
            minusSelectedActionsCount = viewModel.minusSelectedActionsCount,
            isSelectedActionsCountThree = viewModel.isSelectedActionsCountThree
        )
        binding.rvRecommendAction.adapter = recommendActionAdapter
        binding.rvRecommendAction.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean = false
        }
    }

    private fun setClickEvents() {
        backIvClickEvent()
        writeDirectTvClickEvent()
        continueBtnClickEvent()
    }

    private fun continueBtnClickEvent() {
        binding.btnRecommendActionContinue.setOnClickListener {
            val selectedActionList = recommendActionAdapter?.getSelectedActionList()
            trackClickCreateRecommendMissionEvent(selectedActionList)
            val recommendUiModel = RecommendUiModel(
                title = dataFromRecommendMissionActivity.title,
                situation = dataFromRecommendMissionActivity.situation,
                actionList = selectedActionList ?: emptyList()
            )
            startActivity(
                Intent(context, AdditionActivity::class.java).putExtra(
                    MISSION_ACTION_DETAIL, recommendUiModel
                )
            )
            if (!requireActivity().isFinishing) requireActivity().finish()
        }
    }

    private fun trackClickCreateRecommendMissionEvent(selectedActionList: List<String>?) {
        val clickCreateRecommendMissionEventPropertyMap = mutableMapOf<String, Any>(
            getString(R.string.situation) to dataFromRecommendMissionActivity.situation,
            getString(R.string.title) to dataFromRecommendMissionActivity.title

        )
        if (selectedActionList != null) clickCreateRecommendMissionEventPropertyMap.plus(
            getString(R.string.action) to selectedActionList.toTypedArray()
        )

        trackEventWithProperty(
            getString(R.string.click_create_recommend_mission),
            clickCreateRecommendMissionEventPropertyMap
        )
    }

    private fun writeDirectTvClickEvent() {
        binding.tvRecommendActionWriteDirect.setOnClickListener {
            trackEvent(getString(R.string.click_self_create_action))
            val recommendUiModel = RecommendUiModel(
                title = dataFromRecommendMissionActivity.title,
                situation = dataFromRecommendMissionActivity.situation,
                actionList = emptyList()
            )
            startActivity(
                Intent(context, AdditionActivity::class.java).putExtra(
                    MISSION_ACTION_DETAIL, recommendUiModel
                )
            )
            if (!requireActivity().isFinishing) requireActivity().finish()
        }
    }

    private fun backIvClickEvent() {
        binding.ivRecommendationActionBack.setOnClickListener {
            startActivity(Intent(context, RecommendMissionActivity::class.java))
            if (!requireActivity().isFinishing) requireActivity().finish()
        }
    }

    private fun setObservers() {
        setRecommendActionObservers()
    }

    private fun setRecommendActionObservers() {
        setRecommendActionSuccessObserver()
        setRecommendActionErrorObserver()
    }

    private fun setRecommendActionErrorObserver() {
        viewModel.recommendActionListErrorResponse.observe(this) { errorMessage ->
            if (errorMessage == NO_INTERNET_CONDITION_ERROR) requireContext().showNotTodoSnackBar(
                binding.root, NO_INTERNET_CONDITION_ERROR
            )
            else requireContext().showToast(errorMessage)
        }
    }

    private fun setRecommendActionSuccessObserver() {
        viewModel.recommendActionListSuccessResponse.observe(this) { actionList ->
            recommendActionAdapter?.submitList(actionList)
        }
    }

    override fun bindViewModelWithBinding() {
        binding.vm = viewModel
    }

    companion object {
        const val MISSION_ACTION_DETAIL = "MISSION_ACTION_DETAIL"
    }
}