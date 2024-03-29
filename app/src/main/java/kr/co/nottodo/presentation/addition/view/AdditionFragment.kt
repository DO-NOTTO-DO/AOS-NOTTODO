package kr.co.nottodo.presentation.addition.view

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.text.HtmlCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kr.co.nottodo.R
import kr.co.nottodo.data.remote.model.addition.RequestAdditionDto
import kr.co.nottodo.data.remote.model.addition.ResponseAdditionDto
import kr.co.nottodo.databinding.FragmentAdditionBinding
import kr.co.nottodo.presentation.addition.adapter.MissionHistoryAdapter
import kr.co.nottodo.presentation.addition.viewmodel.AdditionNewViewModel
import kr.co.nottodo.presentation.base.fragment.DataBindingFragment
import kr.co.nottodo.presentation.home.viewmodel.HomeViewModel
import kr.co.nottodo.util.NotTodoAmplitude
import kr.co.nottodo.util.PublicString.EMPTY_STRING
import kr.co.nottodo.util.PublicString.NO_INTERNET_CONDITION_ERROR
import kr.co.nottodo.util.addButtons
import kr.co.nottodo.util.containToday
import kr.co.nottodo.util.containTomorrow
import kr.co.nottodo.util.hideKeyboard
import kr.co.nottodo.util.showKeyboard
import kr.co.nottodo.util.showNotTodoSnackBar
import kr.co.nottodo.util.showToast
import kr.co.nottodo.view.calendar.monthly.util.achievementConvertStringToDate
import kr.co.nottodo.view.calendar.monthly.util.convertDateStringToInt
import kr.co.nottodo.view.calendar.monthly.util.convertDateToString
import java.util.Date

class AdditionFragment : DataBindingFragment<FragmentAdditionBinding>(R.layout.fragment_addition) {
    private val viewModel by viewModels<AdditionNewViewModel>()
    private val homeViewModel by activityViewModels<HomeViewModel>()
    private var missionHistoryAdapter: MissionHistoryAdapter? = null
    private val args: AdditionFragmentArgs by navArgs()
    private val toAdditionFragmentUiModel by lazy {
        args.toAdditionUiModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackEnterCreateMission()
        setData()
        setViews()
        setObservers()
        setClickEvents()
    }

    private fun trackEnterCreateMission() {
        NotTodoAmplitude.trackEvent(getString(R.string.view_create_mission))
    }

    private fun setData() {
        getDataFromRecommendActivity()
        getRecentMissionList()
        getRecommendSituationList()
    }

    private fun getDataFromRecommendActivity() {
        with(viewModel) {
            mission.value = toAdditionFragmentUiModel.title
            situation.value = toAdditionFragmentUiModel.situation
            actionList.value = toAdditionFragmentUiModel.actionList.toList()
        }
    }

    private fun getRecommendSituationList() {
        viewModel.getRecommendSituationList()
    }

    private fun getRecentMissionList() {
        viewModel.getRecentMissionList()
    }

    private fun setClickEvents() {
        setAddButtonClickEvent()
        setFinishButtonClickEvent()
        setDeleteButtonsClickEvents()
        setTogglesClickEvents()
        setEnterKeyClickEvents()
    }

    private fun setViews() {
        setOpenedDesc()
        setRecyclerViews()
        setActions()
    }

    private fun setRecyclerViews() {
        setMissionHistoryRecyclerView()
    }

    private fun setMissionHistoryRecyclerView() {
        missionHistoryAdapter = MissionHistoryAdapter(setMissionName).also { adapter ->
            binding.rvAdditionMission.adapter = adapter
        }
    }

    private val setMissionName: (String) -> Unit = { missionName: String ->
        trackSetMissionName(missionName)
        binding.etAdditionMission.run {
            setText(missionName)
            requestFocus()
            setSelection(binding.etAdditionMission.length())
        }
        requireContext().showKeyboard(binding.etAdditionMission)
    }

    private fun trackSetMissionName(missionName: String) {
        NotTodoAmplitude.trackEventWithProperty(
            getString(R.string.click_mission_history),
            getString(R.string.title),
            missionName,
        )
    }

    private fun setObservers() {
        observeMission()
        observeSituation()
        observeGoal()
        observeActionListToString()
        observeGetRecommendSituationList()
        observeGetRecentMissionListResponse()
        observePostNottodoResponse()
    }

    private fun observePostNottodoResponse() {
        observePostNottodoSuccessResponse()
        observePostNottodoFailureResponse()
    }

    private fun observeGetRecentMissionListResponse() {
        observeGetRecentMissionListSuccessResponse()
        observeGetRecentMissionListErrorResponse()
    }

    private fun observeGetRecentMissionListErrorResponse() {
        viewModel.getRecentMissionListListErrorResponse.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage.showErrorMessage()
        }
    }

    private fun String.showErrorMessage(isHtmlTagExist: Boolean = false) {
        when (this) {
            NO_INTERNET_CONDITION_ERROR -> binding.root.showNotTodoSnackBar(
                NO_INTERNET_CONDITION_ERROR,
            )

            else -> {
                if (isHtmlTagExist) {
                    val errorMessageWithHtmlTag =
                        HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    requireContext().showNotTodoSnackBar(binding.root, errorMessageWithHtmlTag)
                } else {
                    binding.root.showNotTodoSnackBar(
                        this,
                    )
                    trackAdditionFailureEvent(this)
                }
            }
        }
    }

    private fun observeGetRecentMissionListSuccessResponse() {
        viewModel.getRecentMissionListSuccessResponse.observe(viewLifecycleOwner) { response ->
            missionHistoryAdapter?.submitList(
                response.data.map { recentMission ->
                    recentMission.title
                },
            )
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

    private fun observeGetRecommendSituationListErrorResponse() {
        viewModel.getRecommendSituationListErrorResponse.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage.showErrorMessage()
        }
    }

    private fun observePostNottodoFailureResponse() {
        viewModel.postNottodoErrorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage.showErrorMessage(isHtmlTagExist = true)
        }
    }

    private fun trackAdditionFailureEvent(errorMessage: String) {
        when (errorMessage.first()) {
            '해' -> NotTodoAmplitude.trackEvent(getString(R.string.appear_same_mission_issue_message))
            '낫' -> NotTodoAmplitude.trackEvent(getString(R.string.appear_maxed_issue_message))
        }
    }

    private fun observePostNottodoSuccessResponse() {
        viewModel.postNottodoSuccessResponse.observe(viewLifecycleOwner) { response ->
            requireContext().showToast(getString(R.string.complete_create_nottodo))
            trackCompleteCreateMission(response)
            val sortedList =
                response.dates.sortedBy { date -> date.achievementConvertStringToDate() }
            navigateToMainWithFirstDay(sortedList.first().toString())
        }
    }

    private fun trackCompleteCreateMission(missionData: ResponseAdditionDto.Addition) {
        with(missionData) {
            val completeCreateMissionEventPropertyMap = mutableMapOf<String, Any>(
                getString(R.string.date) to dates.map { date -> date.convertDateStringToInt() },
                getString(R.string.title) to title,
                getString(R.string.situation) to situation,
            )
            if (goal != null) completeCreateMissionEventPropertyMap.plus(getString(R.string.goal) to goal)
            if (actions != null) {
                completeCreateMissionEventPropertyMap.plus(
                    getString(R.string.action) to actions.toTypedArray(),
                )
            }
            NotTodoAmplitude.trackEventWithProperty(
                getString(R.string.complete_create_mission),
                completeCreateMissionEventPropertyMap,
            )
        }
    }

    private fun navigateToMainWithFirstDay(firstDate: String) {
        homeViewModel.getFirstDateOnAdd.value = firstDate
        findNavController().popBackStack()
    }

    private fun setActions() {
        viewModel.actionCount.observe(viewLifecycleOwner) {
            setActionBoxIsFilled()
        }
    }

    private fun setActionBoxIsFilled() {
        binding.layoutAdditionActionClosed.isActivated = viewModel.isFirstActionExist.value ?: false
    }

    private fun setEnterKeyClickEvents() {
        binding.etAdditionMission.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeMissionToggle()
                requireContext().hideKeyboard(binding.root)
            }
            return@setOnEditorActionListener false
        }

        binding.etAdditionSituation.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeSituationToggle()
                requireContext().hideKeyboard(binding.root)
            }
            return@setOnEditorActionListener false
        }

        binding.etAdditionAction.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && binding.etAdditionAction.text.isNotBlank()) {
                addAction()
            }
            return@setOnEditorActionListener true
        }

        binding.etAdditionGoal.setOnEditorActionListener { _, actionId, _ ->
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
        viewModel.action.value = EMPTY_STRING

        if (newActionList?.size == 3) requireContext().hideKeyboard(binding.root)
    }

    private fun setDeleteButtonsClickEvents() {
        firstDeleteBtnClickEvent()
        secondDeleteBtnClickEvent()
        thirdDeleteBtnClickEvent()
    }

    private fun firstDeleteBtnClickEvent() {
        binding.ivAdditionActionFirstDelete.setOnClickListener {
            deleteAction(indexToRemove = 0)
        }
    }

    private fun secondDeleteBtnClickEvent() {
        binding.ivAdditionActionSecondDelete.setOnClickListener {
            deleteAction(indexToRemove = 1)
        }
    }

    private fun thirdDeleteBtnClickEvent() {
        binding.ivAdditionActionThirdDelete.setOnClickListener {
            deleteAction(indexToRemove = 2)
        }
    }

    private fun deleteAction(indexToRemove: Int) {
        val newActionList =
            viewModel.actionList.value?.filterIndexed { index, _ -> index != indexToRemove }
        viewModel.actionList.value = newActionList

        if (newActionList?.size == 2) requestFocusWithShowingKeyboard(binding.etAdditionAction)
    }

    private fun setFinishButtonClickEvent() {
        binding.ivAdditionDelete.setOnClickListener {
            if (!requireActivity().isFinishing) {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun setSituationRecommendations(situationList: List<String>) {
        binding.layoutAdditionSituationRecommend.addButtons(
            situationList,
            binding.etAdditionSituation,
        )
    }

    private fun setAddButtonClickEvent() {
        binding.btnAdditionAdd.setOnClickListener {
            var actionList: MutableList<String>? = mutableListOf<String>().apply {
                if (!viewModel.firstAction.value.isNullOrBlank()) add(viewModel.firstAction.value!!)
                if (!viewModel.secondAction.value.isNullOrBlank()) add(viewModel.secondAction.value!!)
                if (!viewModel.thirdAction.value.isNullOrBlank()) add(viewModel.thirdAction.value!!)
            }
            if (actionList?.isEmpty() == true) actionList = null

            var goal: String? = viewModel.goal.value
            if (goal?.isBlank() == true) goal = null

            val dateList: List<String> =
                binding.calendarAdditionDateOpened.selectedDays.mapNotNull { selectedDay ->
                    selectedDay.convertDateToString()
                }.sortedDescending()

            val requestAdditionDto = RequestAdditionDto(
                title = viewModel.mission.value ?: throw NullPointerException(),
                situation = viewModel.situation.value ?: throw NullPointerException(),
                actions = actionList,
                goal = goal,
                dates = dateList,
            )
            trackClickCreateMission(requestAdditionDto)
            viewModel.postNottodo(requestAdditionDto)
        }
    }

    private fun trackClickCreateMission(missionData: RequestAdditionDto) {
        with(missionData) {
            val clickCreateMissionEventPropertyMap = mutableMapOf<String, Any>(
                getString(R.string.date) to dates.map { date -> date.convertDateStringToInt() },
                getString(R.string.title) to title,
                getString(R.string.situation) to situation,
            )
            if (goal != null) clickCreateMissionEventPropertyMap.plus(getString(R.string.goal) to goal)
            if (actions != null) {
                clickCreateMissionEventPropertyMap.plus(
                    getString(R.string.action) to actions.toTypedArray(),
                )
            }
            NotTodoAmplitude.trackEventWithProperty(
                getString(R.string.click_create_mission),
                clickCreateMissionEventPropertyMap,
            )
        }
    }

    private fun observeGoal() {
        viewModel.isGoalFilled.observe(viewLifecycleOwner) { isGoalFilled ->
            binding.layoutAdditionGoalClosed.isActivated = isGoalFilled
        }
    }

    private fun observeActionListToString() {
        viewModel.actionListToString.observe(viewLifecycleOwner) { actionListToString ->
            binding.tvAdditionActionClosedInput.text = actionListToString
        }
    }

    private fun observeSituation() {
        viewModel.isSituationFilled.observe(viewLifecycleOwner) { isSituationFilled ->
            binding.layoutAdditionSituationClosed.isActivated = isSituationFilled
        }
    }

    private fun observeMission() {
        viewModel.isMissionFilled.observe(viewLifecycleOwner) { isMissionFilled ->
            binding.layoutAdditionMissionClosed.isActivated = isMissionFilled
        }
    }

    private fun setTogglesClickEvents() {
        setDateToggleClickEvent()
        setMissionToggleClickEvent()
        setSituationToggleClickEvent()
        setActionToggleClickEvent()
        setGoalToggleClickEvent()
    }

    private fun setDateToggleClickEvent() {
        binding.layoutAdditionDateClosed.setOnClickListener {
            openDateToggle()
            closeMissionToggle()
            closeSituationToggle()
            closeActionToggle()
            closeGoalToggle()
        }

        binding.tvAdditionDateOpenedComplete.setOnClickListener {
            closeDateToggle()
        }
    }

    private fun setMissionToggleClickEvent() {
        binding.layoutAdditionMissionClosed.setOnClickListener {
            openMissionToggle()
            closeDateToggle()
            closeSituationToggle()
            closeActionToggle()
            closeGoalToggle()
        }
    }

    private fun setSituationToggleClickEvent() {
        binding.layoutAdditionSituationClosed.setOnClickListener {
            openSituationToggle()
            closeDateToggle()
            closeMissionToggle()
            closeActionToggle()
            closeGoalToggle()
        }
    }

    private fun setActionToggleClickEvent() {
        binding.layoutAdditionActionClosed.setOnClickListener {
            openActionToggle()
            closeDateToggle()
            closeMissionToggle()
            closeSituationToggle()
            closeGoalToggle()
        }

        binding.tvAdditionActionComplete.setOnClickListener {
            closeActionToggle()
            requireContext().hideKeyboard(binding.root)
        }
    }

    private fun setGoalToggleClickEvent() {
        binding.layoutAdditionGoalClosed.setOnClickListener {
            openGoalToggle()
            closeDateToggle()
            closeMissionToggle()
            closeSituationToggle()
            closeActionToggle()
        }
    }

    private fun setDateDescTv() {
        val selectedDays: MutableList<Date> =
            binding.calendarAdditionDateOpened.selectedDays.apply {
                sortDescending()
            }
        if (selectedDays.isEmpty()) return
        binding.tvAdditionDate.text = selectedDays.last().convertDateToString()

        if (selectedDays.size > 1) {
            binding.tvAdditionDateEndDesc.apply {
                visibility = View.VISIBLE
                text = getString(R.string.other_days, selectedDays.size - 1)
            }
        } else {
            binding.tvAdditionDateEndDesc.apply {
                visibility = View.GONE
            }
        }

        if (selectedDays.containToday()) {
            binding.tvAdditionDateStartDesc.apply {
                visibility = View.VISIBLE
                text = getString(R.string.today)
                return
            }
        }
        if (selectedDays.containTomorrow()) {
            binding.tvAdditionDateStartDesc.apply {
                visibility = View.VISIBLE
                text = getString(R.string.tomorrow)
                return
            }
        }

        binding.tvAdditionDateStartDesc.visibility = View.GONE
    }

    private fun closeDateToggle() {
        viewModel.isDateToggleVisible.value = false
        setDateDescTv()
    }

    private fun openDateToggle() {
        viewModel.isDateToggleVisible.value = true
    }

    private fun closeGoalToggle() {
        viewModel.isGoalToggleVisible.value = false
    }

    private fun requestFocusWithShowingKeyboard(editText: EditText) {
        editText.run {
            requestFocus()
            setSelection(editText.length())
            requireContext().showKeyboard(this)
        }
    }

    private fun openGoalToggle() {
        viewModel.isGoalToggleVisible.value = true
        requestFocusWithShowingKeyboard(binding.etAdditionGoal)
    }

    private fun openActionToggle() {
        viewModel.isActionToggleVisible.value = true
        if ((
                viewModel.actionCount.value
                    ?: 0
                ) < 3
        ) {
            requestFocusWithShowingKeyboard(binding.etAdditionAction)
        }
    }

    private fun closeActionToggle() {
        viewModel.isActionToggleVisible.value = false
    }

    private fun openSituationToggle() {
        viewModel.isSituationToggleVisible.value = true
        requestFocusWithShowingKeyboard(binding.etAdditionSituation)
    }

    private fun closeSituationToggle() {
        viewModel.isSituationToggleVisible.value = false
    }

    private fun closeMissionToggle() {
        viewModel.isMissionToggleVisible.value = false
    }

    private fun openMissionToggle() {
        viewModel.isMissionToggleVisible.value = true
        requestFocusWithShowingKeyboard(binding.etAdditionMission)
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
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            setSpan(
                ForegroundColorSpan(requireContext().getColor(R.color.green_1_98ffa9)),
                3,
                6,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }.also { spannedString -> binding.tvAdditionMissionOpenedDesc.text = spannedString }
    }

    private fun setSituationOpenedDescSpan() {
        SpannableStringBuilder(
            getString(R.string.situation_desc),
        ).apply {
            setSpan(
                ForegroundColorSpan(requireContext().getColor(R.color.white)),
                0,
                this.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            setSpan(
                ForegroundColorSpan(requireContext().getColor(R.color.green_1_98ffa9)),
                10,
                12,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }.also { spannedString -> binding.tvAdditionSituationOpenedDesc.text = spannedString }
    }

    private fun setActionOpenedDescSpan() {
        SpannableStringBuilder(getString(R.string.action_desc)).apply {
            setSpan(
                ForegroundColorSpan(requireContext().getColor(R.color.white)),
                0,
                this.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            setSpan(
                ForegroundColorSpan(requireContext().getColor(R.color.green_1_98ffa9)),
                3,
                5,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }.also { spannedString -> binding.tvAdditionActionOpenedDesc.text = spannedString }
    }

    private fun setGoalOpenedDescSpan() {
        SpannableStringBuilder(getString(R.string.goal_desc)).apply {
            setSpan(
                ForegroundColorSpan(requireContext().getColor(R.color.white)),
                0,
                this.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            setSpan(
                ForegroundColorSpan(requireContext().getColor(R.color.green_1_98ffa9)),
                12,
                14,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }.also { spannedString -> binding.tvAdditionGoalOpenedDesc.text = spannedString }
    }

    override fun onDestroyView() {
        missionHistoryAdapter = null
        super.onDestroyView()
    }

    override fun bindViewModelWithBinding() {
        binding.vm = viewModel
    }

    companion object {
        const val FIRST_DATE = "FIRST_DATE"
    }
}
