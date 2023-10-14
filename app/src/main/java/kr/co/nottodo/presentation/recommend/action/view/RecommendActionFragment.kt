package kr.co.nottodo.presentation.recommend.action.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentRecommendActionBinding
import kr.co.nottodo.presentation.addition.view.AdditionActivity
import kr.co.nottodo.presentation.base.fragment.ViewBindingFragment
import kr.co.nottodo.presentation.recommend.action.adapter.RecommendActionAdapter
import kr.co.nottodo.presentation.recommend.action.viewmodel.RecommendActionViewModel
import kr.co.nottodo.presentation.recommend.model.ToAdditionUiModel
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent
import kr.co.nottodo.util.NotTodoAmplitude.trackEventWithProperty
import kr.co.nottodo.util.PublicString.MISSION_ACTION_DETAIL
import kr.co.nottodo.util.PublicString.NO_INTERNET_CONDITION_ERROR
import kr.co.nottodo.util.showNotTodoSnackBar
import kr.co.nottodo.util.showToast

class RecommendActionFragment : ViewBindingFragment<FragmentRecommendActionBinding>() {

    private var recommendActionAdapter: RecommendActionAdapter? = null
    private val viewModel by viewModels<RecommendActionViewModel>()
    private val args: RecommendActionFragmentArgs by navArgs()
    private val toRecommendActionUiModel by lazy {
        args.toRecommendActionUiModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()
        setViews()
        setClickEvents()
        setObservers()
    }

    private fun setData() {
        getDataFromRecommendMissionActivity()
        getRecommendActionList()
    }

    private fun getDataFromRecommendMissionActivity() {
        trackEnterRecommendActionView()
        viewModel.setMissionId(toRecommendActionUiModel.id)
    }

    private fun trackEnterRecommendActionView() {
        trackEventWithProperty(
            getString(R.string.view_recommend_mission_detail), mapOf(
                getString(R.string.situation) to toRecommendActionUiModel.situation,
                getString(R.string.title) to toRecommendActionUiModel.title
            )
        )
    }

    private fun getRecommendActionList() {
        viewModel.getRecommendActionList()
    }

    private fun setViews() {
        setMissionTextView()
        setSituationTextView()
        setMissionImageView()
        setRecommendActionRecyclerView()
    }

    private fun setMissionTextView() {
        binding.tvRecommendActionMission.text = toRecommendActionUiModel.title
    }

    private fun setSituationTextView() {
        binding.tvRecommendActionSituation.text = toRecommendActionUiModel.situation
    }

    private fun setMissionImageView() {
        binding.ivRecommendActionMissionSituation.load(toRecommendActionUiModel.image)
    }

    private fun setRecommendActionRecyclerView() {
        recommendActionAdapter = RecommendActionAdapter(
            plusSelectedActionsCount = viewModel.plusSelectedActionsCount,
            minusSelectedActionsCount = viewModel.minusSelectedActionsCount,
            isSelectedActionsCountThree = viewModel.isSelectedActionsCountThree
        )

        binding.rvRecommendAction.run {
            adapter = recommendActionAdapter
            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean = false
            }
        }
    }

    private fun setClickEvents() {
        backIvClickEvent()
        continueBtnClickEvent()
        writeDirectTvClickEvent()
    }

    private fun backIvClickEvent() {
        binding.ivRecommendationActionBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun continueBtnClickEvent() {
        binding.btnRecommendActionContinue.setOnClickListener {
            navigateToAdditionFragment()
        }
    }

    private fun navigateToAdditionFragment() {
        val selectedActionList = recommendActionAdapter?.getSelectedActionList()
        val toAdditionUiModel = ToAdditionUiModel(
            title = toRecommendActionUiModel.title,
            situation = toRecommendActionUiModel.situation,
            actionList = selectedActionList ?: emptyList()
        )

        RecommendActionFragmentDirections.actionRecommendActionFragmentToAdditionFragment(
            toAdditionUiModel
        ).also { action ->
            findNavController().navigate(action)
        }

        viewModel.resetActionsCount()

        trackClickCreateRecommendMissionEvent(selectedActionList)
    }

    private fun trackClickCreateRecommendMissionEvent(selectedActionList: List<String>?) {
        mutableMapOf<String, Any>(
            getString(R.string.situation) to toRecommendActionUiModel.situation,
            getString(R.string.title) to toRecommendActionUiModel.title
        ).apply {
            if (selectedActionList != null) plus(getString(R.string.action) to selectedActionList.toTypedArray())
        }.also {
            it.trackCreateRecommendMissionEvent()
        }
    }

    private fun Map<String, Any>.trackCreateRecommendMissionEvent() {
        trackEventWithProperty(
            getString(R.string.click_create_recommend_mission),
            this@trackCreateRecommendMissionEvent
        )
    }

    private fun writeDirectTvClickEvent() {
        binding.tvRecommendActionWriteDirect.setOnClickListener {
            trackSelfCreateActionEvent()
            ToAdditionUiModel(
                title = toRecommendActionUiModel.title,
                situation = toRecommendActionUiModel.situation,
                actionList = emptyList()
            ).also {
                startActivity(
                    Intent(context, AdditionActivity::class.java).putExtra(
                        MISSION_ACTION_DETAIL, it
                    )
                )
                if (!requireActivity().isFinishing) requireActivity().finish()
            }
        }
    }

    private fun trackSelfCreateActionEvent() {
        trackEvent(getString(R.string.click_self_create_action))
    }

    private fun setObservers() {
        setRecommendActionObservers()
        setIsActionSelectedObserver()
    }

    private fun setRecommendActionObservers() {
        setRecommendActionSuccessObserver()
        setRecommendActionErrorObserver()
    }

    private fun setRecommendActionSuccessObserver() {
        viewModel.recommendActionListSuccessResponse.observe(viewLifecycleOwner) { actionList ->
            recommendActionAdapter?.submitList(actionList)
        }
    }

    private fun setRecommendActionErrorObserver() {
        viewModel.recommendActionListErrorResponse.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage == NO_INTERNET_CONDITION_ERROR) requireContext().showNotTodoSnackBar(
                binding.root, NO_INTERNET_CONDITION_ERROR
            )
            else requireContext().showToast(errorMessage)
        }
    }

    private fun setIsActionSelectedObserver() {
        viewModel.isActionSelected.observe(viewLifecycleOwner) { isActionSelected ->
            binding.btnRecommendActionContinue.isVisible = isActionSelected
        }
    }

    override fun setBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentRecommendActionBinding =
        FragmentRecommendActionBinding.inflate(inflater, container, false)
}