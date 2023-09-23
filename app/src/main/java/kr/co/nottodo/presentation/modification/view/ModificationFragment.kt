package kr.co.nottodo.presentation.modification.view

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import kr.co.nottodo.MainActivity
import kr.co.nottodo.R
import kr.co.nottodo.data.local.ParcelizeBottomDetail
import kr.co.nottodo.data.local.ParcelizeBottomDetail.Action
import kr.co.nottodo.data.remote.model.addition.RequestAdditionDto
import kr.co.nottodo.data.remote.model.modification.ResponseModificationDto.Modification
import kr.co.nottodo.databinding.FragmentModificationBinding
import kr.co.nottodo.presentation.addition.adapter.MissionHistoryAdapter
import kr.co.nottodo.presentation.base.fragment.DataBindingFragment
import kr.co.nottodo.presentation.modification.viewmodel.ModificationViewModel
import kr.co.nottodo.presentation.recommendation.action.view.RecommendActionActivity
import kr.co.nottodo.presentation.recommendation.model.RecommendUiModel
import kr.co.nottodo.util.NotTodoAmplitude
import kr.co.nottodo.util.PublicString.NO_INTERNET_CONDITION_ERROR
import kr.co.nottodo.util.addButtons
import kr.co.nottodo.util.containToday
import kr.co.nottodo.util.containTomorrow
import kr.co.nottodo.util.getParcelable
import kr.co.nottodo.util.hideKeyboard
import kr.co.nottodo.util.showKeyboard
import kr.co.nottodo.util.showNotTodoSnackBar
import kr.co.nottodo.util.showToast
import kr.co.nottodo.view.calendar.monthly.util.convertDateStringToInt
import kr.co.nottodo.view.calendar.monthly.util.convertDateToString
import java.util.Date

class ModificationFragment :
    DataBindingFragment<FragmentModificationBinding>(R.layout.fragment_modification) {
    private val viewModel by viewModels<ModificationViewModel>()
    private var missionHistoryAdapter: MissionHistoryAdapter? = null
    private val contextNonNull by lazy { requireContext() }
    private val activityNonNull by lazy { requireActivity() }
    private val dataFromHome: ParcelizeBottomDetail by lazy {
        // TODO : Safe Args를 통해 데이터 전달받기
        ParcelizeBottomDetail(
            id = 1L,
            title = "낫투두 예시",
            situation = "상황 예시",
            actions = listOf(Action("실천 행동 1"), Action("실천 행동 2")),
            count = 1,
            goal = "",
            date = "2023.09.22"
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()
        setViews()
        setObservers()
        setClickEvents()
    }

    private fun trackEnterUpdateMission(
        notTodoData: ParcelizeBottomDetail,
        dateIntList: List<Int>,
    ) {
        with(notTodoData) {
            val viewUpdateMissionEventPropertyMap = mutableMapOf(
                getString(R.string.title) to title,
                getString(R.string.situation) to situation,
                getString(R.string.date) to dateIntList
            )
            if (!goal.isNullOrBlank()) viewUpdateMissionEventPropertyMap.plus(getString(R.string.goal) to goal)
            if (!actions.isNullOrEmpty()) viewUpdateMissionEventPropertyMap.plus(
                getString(R.string.action) to actions.toTypedArray()
            )
            NotTodoAmplitude.trackEventWithProperty(
                getString(R.string.view_update_mission), viewUpdateMissionEventPropertyMap
            )
        }
    }

    private fun trackEnterCreateMission() {
        NotTodoAmplitude.trackEvent(getString(R.string.view_create_mission))
    }

    private fun setData() {
        getRecentMissionList()
        getRecommendSituationList()
        // TODO : Intent가 아닌 Bundle에서 받아와야 함
        // getDataFromRecommendActivity()
    }

    private fun getDataFromRecommendActivity() {
        val recommendUiModel: RecommendUiModel = activityNonNull.intent?.getParcelable(
            RecommendActionActivity.MISSION_ACTION_DETAIL, RecommendUiModel::class.java
        ) ?: return

        with(viewModel) {
            mission.value = recommendUiModel.title
            situation.value = recommendUiModel.situation
        }
        setActionList(recommendUiModel.actionList)
    }

    private fun setActionList(actionList: List<String>) {
        fun List<String>.second() = this[1]
        fun List<String>.third() = this[2]

        actionList.run {
            viewModel.actionCount.value = this.size
            if (this.size >= 1) setFirstAction(this.first())
            if (this.size >= 2) setSecondAction(this.second())
            if (this.size >= 3) setThirdAction(this.third())
        }
    }

    private fun setFirstAction(firstAction: String) {
        viewModel.firstAction.value = firstAction
    }

    private fun setSecondAction(secondAction: String) {
        viewModel.firstAction.value = secondAction
    }

    private fun setThirdAction(thirdAction: String) {
        viewModel.firstAction.value = thirdAction
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
            binding.rvModificationMission.adapter = adapter
        }
    }

    private val setMissionName: (String) -> Unit = { missionName: String ->
        trackSetMissionName(missionName)
        binding.etModificationMission.run {
            setText(missionName)
            requestFocus()
            setSelection(this.length())
            contextNonNull.showKeyboard(this)
        }
    }

    private fun trackSetMissionName(missionName: String) {
        NotTodoAmplitude.trackEventWithProperty(
            getString(R.string.click_mission_history), getString(R.string.title), missionName
        )
    }

    private fun setObservers() {
        observeMission()
        observeSituation()
        observeGoal()
        observeGetRecommendSituationList()
        observeGetRecentMissionListResponse()
        observeModifyNottodoResponse()
        observeGetMissionDatesResponse()

    }

    private fun observeGetMissionDatesResponse() {
        observeGetMissionDatesSuccessResponse()
        observeGetMissionDatesErrorResponse()
    }

    private fun observeGetMissionDatesSuccessResponse() {
        viewModel.dates.observe(viewLifecycleOwner) { dates ->
            trackEnterUpdateMission(dataFromHome, dates.map { it.convertDateStringToInt() })
        }
    }

    private fun observeGetMissionDatesErrorResponse() {
        viewModel.getMissionDatesErrorResponse.observe(viewLifecycleOwner) { errorMessage ->
            contextNonNull.showToast(if (errorMessage == NO_INTERNET_CONDITION_ERROR) NO_INTERNET_CONDITION_ERROR else errorMessage)
            if (!activityNonNull.isFinishing) activityNonNull.finish()
        }
    }

    private fun observeModifyNottodoResponse() {
        observeModifyNottodoSuccessResponse()
        observeModifyNottodoFailureResponse()
    }

    private fun observeGetRecentMissionListResponse() {
        observeGetRecentMissionListSuccessResponse()
        observeGetRecentMissionListErrorResponse()
    }

    private fun observeGetRecentMissionListErrorResponse() {
        viewModel.getRecentMissionListListErrorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage.showErrorMessage()
        }
    }

    private fun String.showErrorMessage(isHtmlTagExist: Boolean = false) {
        when (this) {
            NO_INTERNET_CONDITION_ERROR -> binding.root.showNotTodoSnackBar(
                NO_INTERNET_CONDITION_ERROR
            )

            else -> {
                if (isHtmlTagExist) {
                    val errorMessageWithHtmlTag =
                        HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    contextNonNull.showNotTodoSnackBar(binding.root, errorMessageWithHtmlTag)
                } else {
                    binding.root.showNotTodoSnackBar(
                        this
                    )
                    trackAdditionFailureEvent(this)
                }
            }
        }
    }

    private fun observeGetRecentMissionListSuccessResponse() {
        viewModel.getRecentMissionListSuccessResponse.observe(viewLifecycleOwner) { response ->
            missionHistoryAdapter?.submitList(response.data.map { recentMission ->
                recentMission.title
            })
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
        viewModel.getRecommendSituationListErrorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage.showErrorMessage()
        }
    }

    private fun observeModifyNottodoFailureResponse() {
        viewModel.modifyNottodoErrorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage.showErrorMessage(isHtmlTagExist = true)
        }
    }

    private fun trackAdditionFailureEvent(errorMessage: String) {
        when (errorMessage.first()) {
            '해' -> NotTodoAmplitude.trackEvent(getString(R.string.appear_same_mission_issue_message))
            '낫' -> NotTodoAmplitude.trackEvent(getString(R.string.appear_maxed_issue_message))
        }
    }

    private fun observeModifyNottodoSuccessResponse() {
        viewModel.modifyNottodoSuccessResponse.observe(viewLifecycleOwner) { response ->
            contextNonNull.showToast(getString(R.string.complete_modify_nottodo))
            activityNonNull.setResult(AppCompatActivity.RESULT_OK)
            if (!activityNonNull.isFinishing) activityNonNull.finish()
            trackCompleteModifyMission(response)
        }
    }

    private fun trackCompleteModifyMission(missionData: Modification) {
        with(missionData) {
            val completeModifyMissionEventPropertyMap = mutableMapOf(
                getString(R.string.date) to viewModel.getDateToIntList(),
                getString(R.string.title) to title,
                getString(R.string.situation) to situation
            )
            if (!goal.isNullOrBlank()) completeModifyMissionEventPropertyMap.plus(getString(R.string.goal) to goal)
            if (!actions.isNullOrEmpty()) completeModifyMissionEventPropertyMap.plus(
                getString(R.string.action) to actions.toTypedArray()
            )
            NotTodoAmplitude.trackEventWithProperty(
                getString(R.string.complete_update_mission), completeModifyMissionEventPropertyMap
            )
        }
    }

    private fun navigateToMainWithFirstDay(firstDate: String) {
        startActivity(
            Intent(contextNonNull, MainActivity::class.java).setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            ).putExtra(FIRST_DATE, firstDate)
        )
        if (!activityNonNull.isFinishing) activityNonNull.finish()
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

    private fun setEnterKeyClickEvents() {
        binding.etModificationMission.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeMissionToggle()
                contextNonNull.hideKeyboard(binding.root)
            }
            return@setOnEditorActionListener false
        }

        binding.etModificationSituation.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeSituationToggle()
                contextNonNull.hideKeyboard(binding.root)
            }
            return@setOnEditorActionListener false
        }

        binding.etModificationAction.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && binding.etModificationAction.text.isNotBlank()) {
                viewModel.actionCount.value?.let { addAction(it) }
            }
            return@setOnEditorActionListener true
        }

        binding.etModificationGoal.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeGoalToggle()
                contextNonNull.hideKeyboard(binding.root)
            }
            return@setOnEditorActionListener false
        }
    }

    private fun addAction(actionCount: Int) {
        when (actionCount) {
            0 -> viewModel.run {
                firstAction.value = action.value
                action.value = ""
            }

            1 -> viewModel.run {
                secondAction.value = action.value
                action.value = ""
            }

            2 -> {
                viewModel.run {
                    thirdAction.value = action.value
                    action.value = ""
                }
                contextNonNull.hideKeyboard(binding.root)
            }
        }
    }

    private fun setDeleteButtonsClickEvents() {
        firstDeleteBtnClickEvent()
        secondDeleteBtnClickEvent()
        thirdDeleteBtnClickEvent()
    }

    private fun firstDeleteBtnClickEvent() {
        binding.ivModificationActionFirstDelete.setOnClickListener {
            when (viewModel.actionCount.value) {
                1 -> {
                    viewModel.firstAction.value = ""
                }

                2 -> {
                    viewModel.run {
                        firstAction.value = secondAction.value
                        secondAction.value = ""
                    }
                }

                3 -> {
                    viewModel.run {
                        firstAction.value = secondAction.value
                        secondAction.value = thirdAction.value
                        thirdAction.value = ""
                    }
                    requestFocusWithShowingKeyboard(binding.etModificationAction)
                }
            }
        }
    }

    private fun secondDeleteBtnClickEvent() {
        binding.ivModificationActionSecondDelete.setOnClickListener {
            when (viewModel.actionCount.value) {
                2 -> {
                    viewModel.secondAction.value = ""
                }

                3 -> {
                    viewModel.run {
                        secondAction.value = thirdAction.value
                        thirdAction.value = ""
                    }
                    requestFocusWithShowingKeyboard(binding.etModificationAction)
                }
            }
        }
    }

    private fun thirdDeleteBtnClickEvent() {
        binding.ivModificationActionThirdDelete.setOnClickListener {
            viewModel.thirdAction.value = ""
            requestFocusWithShowingKeyboard(binding.etModificationAction)
        }
    }

    private fun setFinishButtonClickEvent() {
        binding.ivModificationDelete.setOnClickListener { if (!activityNonNull.isFinishing) activityNonNull.finish() }
    }

    private fun setSituationRecommendations(situationList: List<String>) {
        binding.layoutModificationSituationRecommend.addButtons(
            situationList, binding.etModificationSituation
        )
    }

    private fun setAddButtonClickEvent() {
        binding.btnModificationModify.setOnClickListener {
            var actionList: MutableList<String>? = mutableListOf<String>().apply {
                if (!viewModel.firstAction.value.isNullOrBlank()) add(viewModel.firstAction.value!!)
                if (!viewModel.secondAction.value.isNullOrBlank()) add(viewModel.secondAction.value!!)
                if (!viewModel.thirdAction.value.isNullOrBlank()) add(viewModel.thirdAction.value!!)
            }
            if (actionList?.isEmpty() == true) actionList = null

            var goal: String? = viewModel.goal.value
            if (goal?.isBlank() == true) goal = null

            val dateList: List<String> =
                binding.calendarModificationDateOpened.selectedDays.mapNotNull { selectedDay ->
                    selectedDay.convertDateToString()
                }.sortedDescending()

            val requestAdditionDto = RequestAdditionDto(
                title = viewModel.mission.value ?: throw NullPointerException(),
                situation = viewModel.situation.value ?: throw NullPointerException(),
                actions = actionList,
                goal = goal,
                dates = dateList
            )
            trackClickCreateMission(requestAdditionDto)
            viewModel.modifyNottodo()
        }
    }

    private fun trackClickCreateMission(missionData: RequestAdditionDto) {
        with(missionData) {
            val clickCreateMissionEventPropertyMap = mutableMapOf<String, Any>(
                getString(R.string.date) to dates.map { date -> date.convertDateStringToInt() },
                getString(R.string.title) to title,
                getString(R.string.situation) to situation
            )
            if (goal != null) clickCreateMissionEventPropertyMap.plus(getString(R.string.goal) to goal)
            if (actions != null) clickCreateMissionEventPropertyMap.plus(
                getString(R.string.action) to actions.toTypedArray()
            )
            NotTodoAmplitude.trackEventWithProperty(
                getString(R.string.click_create_mission), clickCreateMissionEventPropertyMap
            )
        }
    }

    private fun observeGoal() {
        viewModel.isGoalFilled.observe(viewLifecycleOwner) { isGoalFilled ->
            binding.layoutModificationGoalClosed.isActivated = isGoalFilled
        }
    }

    private fun observeSituation() {
        viewModel.isSituationFilled.observe(viewLifecycleOwner) { isSituationFilled ->
            binding.layoutModificationSituationClosed.isActivated = isSituationFilled
        }
    }

    private fun observeMission() {
        viewModel.isMissionFilled.observe(viewLifecycleOwner) { isMissionFilled ->
            binding.layoutModificationMissionClosed.isActivated = isMissionFilled
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
            contextNonNull.hideKeyboard(binding.root)
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

    private fun setDateDescTv() {
        val selectedDays: MutableList<Date> =
            binding.calendarModificationDateOpened.selectedDays.apply {
                sortDescending()
            }
        if (selectedDays.isEmpty()) return
        binding.tvModificationDate.text = selectedDays.last().convertDateToString()

        if (selectedDays.size > 1) {
            binding.tvModificationDateEndDesc.apply {
                visibility = View.VISIBLE
                text = getString(R.string.other_days, selectedDays.size - 1)
            }
        } else {
            binding.tvModificationDateEndDesc.apply {
                visibility = View.GONE
            }
        }

        if (selectedDays.containToday()) binding.tvModificationDateStartDesc.apply {
            visibility = View.VISIBLE
            text = getString(R.string.today)
            return
        }
        if (selectedDays.containTomorrow()) binding.tvModificationDateStartDesc.apply {
            visibility = View.VISIBLE
            text = getString(R.string.tomorrow)
            return
        }

        binding.tvModificationDateStartDesc.visibility = View.GONE
    }

    private fun closeGoalToggle() {
        viewModel.isGoalToggleVisible.value = false
    }

    private fun requestFocusWithShowingKeyboard(editText: EditText) {
        editText.run {
            requestFocus()
            setSelection(editText.length())
            contextNonNull.showKeyboard(this)
        }
    }

    private fun openGoalToggle() {
        viewModel.isGoalToggleVisible.value = true
        requestFocusWithShowingKeyboard(binding.etModificationGoal)
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

    private fun openSituationToggle() {
        viewModel.isSituationToggleVisible.value = true
        requestFocusWithShowingKeyboard(binding.etModificationSituation)
    }

    private fun closeSituationToggle() {
        viewModel.isSituationToggleVisible.value = false
    }

    private fun closeMissionToggle() {
        viewModel.isMissionToggleVisible.value = false
    }

    private fun openMissionToggle() {
        viewModel.isMissionToggleVisible.value = true
        requestFocusWithShowingKeyboard(binding.etModificationMission)
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
                ForegroundColorSpan(contextNonNull.getColor(R.color.white)),
                0,
                this.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(contextNonNull.getColor(R.color.green_1_98ffa9)),
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
                ForegroundColorSpan(contextNonNull.getColor(R.color.white)),
                0,
                this.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(contextNonNull.getColor(R.color.green_1_98ffa9)),
                10,
                12,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }.also { spannedString -> binding.tvModificationSituationOpenedDesc.text = spannedString }
    }

    private fun setActionOpenedDescSpan() {
        SpannableStringBuilder(getString(R.string.action_desc)).apply {
            setSpan(
                ForegroundColorSpan(contextNonNull.getColor(R.color.white)),
                0,
                this.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(contextNonNull.getColor(R.color.green_1_98ffa9)),
                3,
                5,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }.also { spannedString -> binding.tvModificationActionOpenedDesc.text = spannedString }
    }

    private fun setGoalOpenedDescSpan() {
        SpannableStringBuilder(getString(R.string.goal_desc)).apply {
            setSpan(
                ForegroundColorSpan(contextNonNull.getColor(R.color.white)),
                0,
                this.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(contextNonNull.getColor(R.color.green_1_98ffa9)),
                12,
                14,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }.also { spannedString -> binding.tvModificationGoalOpenedDesc.text = spannedString }
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