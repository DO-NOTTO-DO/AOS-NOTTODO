package kr.co.nottodo.presentation.modification.view

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kr.co.nottodo.R
import kr.co.nottodo.data.local.ParcelizeBottomDetail
import kr.co.nottodo.data.remote.model.modification.ResponseModificationDto.Modification
import kr.co.nottodo.databinding.FragmentModificationBinding
import kr.co.nottodo.presentation.addition.adapter.MissionHistoryAdapter
import kr.co.nottodo.presentation.base.fragment.DataBindingFragment
import kr.co.nottodo.presentation.modification.model.NotTodoData
import kr.co.nottodo.presentation.modification.viewmodel.ModificationNewViewModel
import kr.co.nottodo.util.NotTodoAmplitude
import kr.co.nottodo.util.PublicString
import kr.co.nottodo.util.PublicString.NO_INTERNET_CONDITION_ERROR
import kr.co.nottodo.util.addButtons
import kr.co.nottodo.util.hideKeyboard
import kr.co.nottodo.util.showKeyboard
import kr.co.nottodo.util.showNotTodoSnackBar
import kr.co.nottodo.util.showToast
import kr.co.nottodo.view.calendar.monthly.util.convertDateStringToInt

class ModificationFragment :
    DataBindingFragment<FragmentModificationBinding>(R.layout.fragment_modification) {
    private val viewModel by viewModels<ModificationNewViewModel>()
    private var missionHistoryAdapter: MissionHistoryAdapter? = null
    private val args: ModificationFragmentArgs by navArgs()
    private val toModificationUiModel by lazy {
        args.toModificationUiModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()
        setViews()
        setClickEvents()
        setObservers()
    }

    private fun trackEnterUpdateMission(
        notTodoData: ParcelizeBottomDetail,
        dateIntList: List<Int>,
    ) {
        notTodoData.run {
            mapOf(
                getString(R.string.title) to title,
                getString(R.string.situation) to situation,
                getString(R.string.date) to dateIntList
            ).apply {
                if (!goal.isNullOrBlank()) plus(getString(R.string.goal) to goal)
                if (!actions.isNullOrEmpty()) plus(getString(R.string.action) to actions.toTypedArray())
            }.also {
                NotTodoAmplitude.trackEventWithProperty(getString(R.string.view_update_mission), it)
            }
        }
    }

    private fun getData() {
        getRecentMissionList()
        getRecommendSituationList()
        getDataFromHome()
        getMissionDates()
    }

    private fun getRecentMissionList() {
        viewModel.getRecentMissionList()
    }

    private fun getRecommendSituationList() {
        viewModel.getRecommendSituationList()
    }

    private fun getDataFromHome() {
        NotTodoData(
            toModificationUiModel.title,
            toModificationUiModel.situation,
            toModificationUiModel.actions?.map { action -> action.name.toString() }?.toList(),
            toModificationUiModel.goal,
            toModificationUiModel.id
        ).also { viewModel.setOriginalData(it) }
    }

    private fun getMissionDates() {
        viewModel.getMissionDates()
    }

    private fun setViews() {
        setOpenedDesc()
        setRecyclerViews()
        setActions()
    }

    private fun setOpenedDesc() {
        setMissionOpenedDescSpan()
        setSituationOpenedDescSpan()
        setActionOpenedDescSpan()
        setGoalOpenedDescSpan()
    }

    private fun setMissionOpenedDescSpan() {
        SpannableStringBuilder(getString(R.string.mission_desc)).apply {
            setSpan(
                ForegroundColorSpan(requireContext().getColor(R.color.white)),
                0,
                this.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(requireContext().getColor(R.color.green_1_98ffa9)),
                3,
                6,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }.also { spannedString -> binding.tvModificationMissionOpenedDesc.text = spannedString }
    }

    private fun setSituationOpenedDescSpan() {
        SpannableStringBuilder(
            getString(R.string.situation_desc)
        ).apply {
            setSpan(
                ForegroundColorSpan(requireContext().getColor(R.color.white)),
                0,
                this.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(requireContext().getColor(R.color.green_1_98ffa9)),
                10,
                12,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }.also { spannedString -> binding.tvModificationSituationOpenedDesc.text = spannedString }
    }

    private fun setActionOpenedDescSpan() {
        SpannableStringBuilder(getString(R.string.action_desc)).apply {
            setSpan(
                ForegroundColorSpan(requireContext().getColor(R.color.white)),
                0,
                this.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(requireContext().getColor(R.color.green_1_98ffa9)),
                3,
                5,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }.also { spannedString -> binding.tvModificationActionOpenedDesc.text = spannedString }
    }

    private fun setGoalOpenedDescSpan() {
        SpannableStringBuilder(getString(R.string.goal_desc)).apply {
            setSpan(
                ForegroundColorSpan(requireContext().getColor(R.color.white)),
                0,
                this.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(requireContext().getColor(R.color.green_1_98ffa9)),
                12,
                14,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }.also { spannedString -> binding.tvModificationGoalOpenedDesc.text = spannedString }
    }

    private fun setRecyclerViews() {
        setMissionHistoryRecyclerView()
    }

    private fun setMissionHistoryRecyclerView() {
        missionHistoryAdapter = MissionHistoryAdapter(setMissionName).also { adapter ->
            binding.rvModificationMission.adapter = adapter
        }
    }

    private val setMissionName: (String) -> Unit = { missionName: String ->
        trackSetMissionName(missionName)
        binding.etModificationMission.run {
            setText(missionName)
            requestFocus()
            setSelection(this.length())
            requireContext().showKeyboard(this)
        }
    }

    private fun trackSetMissionName(missionName: String) {
        NotTodoAmplitude.trackEventWithProperty(
            getString(R.string.click_mission_history), getString(R.string.title), missionName
        )
    }

    private fun setActions() {
        viewModel.actionCount.observe(viewLifecycleOwner) {
            setActionBoxIsFilled()
        }
    }

    private fun setActionBoxIsFilled() {
        binding.layoutModificationActionClosed.isActivated =
            viewModel.isFirstActionExist.value ?: false
    }

    private fun setClickEvents() {
        setEnterKeyClickEvents()
        setModifyBtnClickEvent()
        setFinishButtonClickEvent()
        setDeleteButtonsClickEvents()
        setTogglesClickEvents()
    }

    private fun setEnterKeyClickEvents() {
        binding.etModificationMission.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeMissionToggle()
                requireContext().hideKeyboard(binding.root)
            }
            return@setOnEditorActionListener false
        }

        binding.etModificationSituation.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeSituationToggle()
                requireContext().hideKeyboard(binding.root)
            }
            return@setOnEditorActionListener false
        }

        binding.etModificationAction.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && binding.etModificationAction.text.isNotBlank()) {
                viewModel.actionCount.value?.let { addAction() }
            }
            return@setOnEditorActionListener true
        }

        binding.etModificationGoal.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeGoalToggle()
                requireContext().hideKeyboard(binding.root)
            }
            return@setOnEditorActionListener false
        }
    }

    private fun addAction() {
        if (viewModel.actionCount.value == 3 || viewModel.action.value == null) return

        val newActionList = viewModel.actionList.value?.plus(viewModel.action.value!!)
        viewModel.actionList.value = newActionList
        viewModel.action.value = PublicString.EMPTY_STRING

        if (newActionList?.size == 3) requireContext().hideKeyboard(binding.root)
    }

    private fun setDeleteButtonsClickEvents() {
        firstDeleteBtnClickEvent()
        secondDeleteBtnClickEvent()
        thirdDeleteBtnClickEvent()
    }

    private fun firstDeleteBtnClickEvent() {
        binding.ivModificationActionFirstDelete.setOnClickListener {
            deleteAction(indexToRemove = 0)
        }
    }

    private fun secondDeleteBtnClickEvent() {
        binding.ivModificationActionSecondDelete.setOnClickListener {
            deleteAction(indexToRemove = 1)
        }
    }

    private fun thirdDeleteBtnClickEvent() {
        binding.ivModificationActionThirdDelete.setOnClickListener {
            deleteAction(indexToRemove = 2)
        }
    }

    private fun deleteAction(indexToRemove: Int) {
        val newActionList =
            viewModel.actionList.value?.filterIndexed { index, _ -> index != indexToRemove }
        viewModel.actionList.value = newActionList

        if (newActionList?.size == 2) requestFocusWithShowingKeyboard(binding.etModificationAction)
    }

    private fun setFinishButtonClickEvent() {
        binding.ivModificationDelete.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setModifyBtnClickEvent() {
        binding.btnModificationModify.setOnClickListener {
            trackClickUpdateMission()
            viewModel.modifyNottodo()
        }
    }

    private fun trackClickUpdateMission() {
        viewModel.run {
            mapOf(
                getString(R.string.date) to getDateToIntList(),
                getString(R.string.title) to mission.value.toString(),
                getString(R.string.situation) to situation.value.toString()
            ).apply {
                if (!goal.value.isNullOrBlank()) plus(getString(R.string.goal) to goal.value)
                if (!actionList.value.isNullOrEmpty()) plus(getString(R.string.action) to actionList.value!!.toTypedArray())
            }.also {
                NotTodoAmplitude.trackEventWithProperty(
                    getString(R.string.click_update_mission), it
                )
            }
        }
    }

    private fun setTogglesClickEvents() {
        setMissionToggleClickEvent()
        setSituationToggleClickEvent()
        setActionToggleClickEvent()
        setGoalToggleClickEvent()
    }

    private fun setMissionToggleClickEvent() {
        binding.layoutModificationMissionClosed.setOnClickListener {
            openMissionToggle()
            closeSituationToggle()
            closeActionToggle()
            closeGoalToggle()
        }
    }

    private fun setSituationToggleClickEvent() {
        binding.layoutModificationSituationClosed.setOnClickListener {
            openSituationToggle()
            closeMissionToggle()
            closeActionToggle()
            closeGoalToggle()
        }
    }

    private fun setActionToggleClickEvent() {
        binding.layoutModificationActionClosed.setOnClickListener {
            openActionToggle()
            closeMissionToggle()
            closeSituationToggle()
            closeGoalToggle()
        }

        binding.tvModificationActionComplete.setOnClickListener {
            closeActionToggle()
            requireContext().hideKeyboard(binding.root)
        }
    }

    private fun setGoalToggleClickEvent() {
        binding.layoutModificationGoalClosed.setOnClickListener {
            openGoalToggle()
            closeMissionToggle()
            closeSituationToggle()
            closeActionToggle()
        }
    }

    private fun requestFocusWithShowingKeyboard(editText: EditText) {
        editText.run {
            requestFocus()
            setSelection(editText.length())
            requireContext().showKeyboard(this)
        }
    }

    private fun openMissionToggle() {
        viewModel.isMissionToggleVisible.value = true
        requestFocusWithShowingKeyboard(binding.etModificationMission)
    }

    private fun closeMissionToggle() {
        viewModel.isMissionToggleVisible.value = false
    }

    private fun openSituationToggle() {
        viewModel.isSituationToggleVisible.value = true
        requestFocusWithShowingKeyboard(binding.etModificationSituation)
    }

    private fun closeSituationToggle() {
        viewModel.isSituationToggleVisible.value = false
    }

    private fun openActionToggle() {
        viewModel.isActionToggleVisible.value = true
        if ((viewModel.actionCount.value
                ?: 0) < 3
        ) requestFocusWithShowingKeyboard(binding.etModificationAction)
    }

    private fun closeActionToggle() {
        viewModel.isActionToggleVisible.value = false
    }

    private fun openGoalToggle() {
        viewModel.isGoalToggleVisible.value = true
        requestFocusWithShowingKeyboard(binding.etModificationGoal)
    }

    private fun closeGoalToggle() {
        viewModel.isGoalToggleVisible.value = false
    }

    private fun setObservers() {
        observeMission()
        observeSituation()
        observeGoal()
        observeGetRecommendSituationList()
        observeGetRecentMissionListResponse()
        observeModifyNottodoResponse()
        observeGetMissionDatesResponse()
        observeActionListToString()
    }

    private fun observeMission() {
        viewModel.isMissionFilled.observe(viewLifecycleOwner) { isMissionFilled ->
            binding.layoutModificationMissionClosed.isActivated = isMissionFilled
        }
    }

    private fun observeSituation() {
        viewModel.isSituationFilled.observe(viewLifecycleOwner) { isSituationFilled ->
            binding.layoutModificationSituationClosed.isActivated = isSituationFilled
        }
    }

    private fun observeGoal() {
        viewModel.isGoalFilled.observe(viewLifecycleOwner) { isGoalFilled ->
            binding.layoutModificationGoalClosed.isActivated = isGoalFilled
        }
    }

    private fun observeGetRecommendSituationList() {
        observeGetRecommendSituationListSuccessResponse()
        observeGetRecommendSituationListErrorResponse()
    }

    private fun observeGetRecommendSituationListSuccessResponse() {
        viewModel.getRecommendSituationListSuccessResponse.observe(viewLifecycleOwner) { response ->
            setSituationRecommendations(response.data.map { situation -> situation.name })
        }
    }

    private fun setSituationRecommendations(situationList: List<String>) {
        binding.layoutModificationSituationRecommend.addButtons(
            situationList, binding.etModificationSituation
        )
    }

    private fun observeGetRecommendSituationListErrorResponse() {
        viewModel.getRecommendSituationListErrorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage.showErrorMessage()
        }
    }

    private fun observeGetRecentMissionListResponse() {
        observeGetRecentMissionListSuccessResponse()
        observeGetRecentMissionListErrorResponse()
    }

    private fun observeGetRecentMissionListSuccessResponse() {
        viewModel.getRecentMissionListSuccessResponse.observe(viewLifecycleOwner) { response ->
            missionHistoryAdapter?.submitList(response.data.map { recentMission ->
                recentMission.title
            })
        }
    }

    private fun observeGetRecentMissionListErrorResponse() {
        viewModel.getRecentMissionListListErrorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage.showErrorMessage()
        }
    }


    private fun observeGetMissionDatesResponse() {
        observeGetMissionDatesSuccessResponse()
        observeGetMissionDatesErrorResponse()
    }

    private fun observeGetMissionDatesSuccessResponse() {
        viewModel.dates.observe(viewLifecycleOwner) { dates ->
            trackEnterUpdateMission(toModificationUiModel,
                dates.map { it.convertDateStringToInt() })
        }
    }

    private fun observeGetMissionDatesErrorResponse() {
        viewModel.getMissionDatesErrorResponse.observe(viewLifecycleOwner) { errorMessage ->
            requireContext().showToast(if (errorMessage == NO_INTERNET_CONDITION_ERROR) NO_INTERNET_CONDITION_ERROR else errorMessage)
            if (!requireActivity().isFinishing) requireActivity().finish()
        }
    }

    private fun observeModifyNottodoResponse() {
        observeModifyNottodoSuccessResponse()
        observeModifyNottodoFailureResponse()
    }

    private fun observeModifyNottodoSuccessResponse() {
        viewModel.modifyNottodoSuccessResponse.observe(viewLifecycleOwner) { response ->
            requireContext().showToast(getString(R.string.complete_modify_nottodo))
            trackCompleteModifyMission(response)
            findNavController().popBackStack()
        }
    }

    private fun trackCompleteModifyMission(missionData: Modification) {
        with(missionData) {
            mapOf(
                getString(R.string.date) to viewModel.getDateToIntList(),
                getString(R.string.title) to title,
                getString(R.string.situation) to situation
            ).apply {
                if (!goal.isNullOrBlank()) plus(getString(R.string.goal) to goal)
                if (!actions.isNullOrEmpty()) plus(getString(R.string.action) to actions.toTypedArray())
            }.also {
                NotTodoAmplitude.trackEventWithProperty(
                    getString(R.string.complete_update_mission), it
                )
            }
        }
    }

    private fun observeModifyNottodoFailureResponse() {
        viewModel.modifyNottodoErrorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage.showErrorMessage(isHtmlTagExist = true)
        }
    }

    private fun String.showErrorMessage(isHtmlTagExist: Boolean = false) {
        when (this) {
            NO_INTERNET_CONDITION_ERROR -> binding.root.showNotTodoSnackBar(
                NO_INTERNET_CONDITION_ERROR
            )

            else -> {
                if (isHtmlTagExist) {
                    HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT).also { htmlText ->
                        requireContext().showNotTodoSnackBar(binding.root, htmlText)
                    }
                } else {
                    binding.root.showNotTodoSnackBar(this)
                    trackModifyFailureEvent(this)
                }
            }
        }
    }

    private fun trackModifyFailureEvent(errorMessage: String) {
        when (errorMessage.first()) {
            '해' -> NotTodoAmplitude.trackEvent(getString(R.string.appear_same_mission_issue_message))
            '낫' -> NotTodoAmplitude.trackEvent(getString(R.string.appear_maxed_issue_message))
        }
    }

    private fun observeActionListToString() {
        viewModel.actionListToString.observe(viewLifecycleOwner) { actionListToString ->
            binding.tvModificationActionClosedInput.text = actionListToString
        }
    }

    override fun onDestroyView() {
        missionHistoryAdapter = null
        super.onDestroyView()
    }

    override fun bindViewModelWithBinding() {
        binding.vm = viewModel
    }
}