package kr.co.nottodo.presentation.addition.view

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import kr.co.nottodo.MainActivity
import kr.co.nottodo.R
import kr.co.nottodo.data.remote.model.addition.RequestAdditionDto
import kr.co.nottodo.data.remote.model.addition.ResponseAdditionDto
import kr.co.nottodo.databinding.FragmentAdditionBinding
import kr.co.nottodo.presentation.addition.adapter.MissionHistoryAdapter
import kr.co.nottodo.presentation.addition.viewmodel.AdditionViewModel
import kr.co.nottodo.presentation.base.fragment.BaseViewBindingFragment
import kr.co.nottodo.presentation.recommendation.action.view.RecommendActionActivity
import kr.co.nottodo.presentation.recommendation.model.RecommendUiModel
import kr.co.nottodo.util.NotTodoAmplitude
import kr.co.nottodo.util.PublicString
import kr.co.nottodo.util.addButtons
import kr.co.nottodo.util.containToday
import kr.co.nottodo.util.containTomorrow
import kr.co.nottodo.util.getParcelable
import kr.co.nottodo.util.hideKeyboard
import kr.co.nottodo.util.showKeyboard
import kr.co.nottodo.util.showNotTodoSnackBar
import kr.co.nottodo.util.showToast
import kr.co.nottodo.view.calendar.monthly.util.achievementConvertStringToDate
import kr.co.nottodo.view.calendar.monthly.util.convertDateStringToInt
import kr.co.nottodo.view.calendar.monthly.util.convertDateToString
import java.util.Date

class AdditionFragment : BaseViewBindingFragment<FragmentAdditionBinding>() {
    private val viewModel by viewModels<AdditionViewModel>()

    private var missionHistoryAdapter: MissionHistoryAdapter? = null
    private val context by lazy { requireContext() }
    private val activity by lazy { requireActivity() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NotTodoAmplitude.trackEvent(getString(R.string.view_create_mission))
        setData()
        setViews()
        setObservers()
        setClickEvent()
        setEnterKey()
    }

    private fun setData() {
        getRecentMissionList()
        getRecommendSituationList()
        // TODO : Intent가 아닌 Bundle에서 받아와야 함
        // getDataFromRecommendActivity()
    }

    private fun getDataFromRecommendActivity() {
        val recommendUiModel: RecommendUiModel =
            activity.intent?.getParcelable(
                RecommendActionActivity.MISSION_ACTION_DETAIL,
                RecommendUiModel::class.java
            ) ?: return

        with(viewModel) {
            mission.value = recommendUiModel.title
            situation.value = recommendUiModel.situation
        }
        initActionList(recommendUiModel.actionList)
    }

    private fun initActionList(actionList: List<String>) {
        when (actionList.size) {
            1 -> {
                setFirstAction(actionList[0])
            }

            2 -> {
                setFirstAction(actionList[0])
                setSecondAction(actionList[1])
            }

            3 -> {
                setFirstAction(actionList[0])
                setSecondAction(actionList[1])
                setThirdAction(actionList[2])
            }
        }
        viewModel.actionCount.value = actionList.size
    }

    private fun setFirstAction(firstAction: String) {
        binding.run {
            tvAdditionActionFirst.text = firstAction
            tvAdditionActionFirst.visibility = View.VISIBLE
            ivAdditionActionFirstDelete.visibility = View.VISIBLE
        }
    }

    private fun setSecondAction(secondAction: String) {
        binding.run {
            tvAdditionActionSecond.text = secondAction
            tvAdditionActionSecond.visibility = View.VISIBLE
            ivAdditionActionSecondDelete.visibility = View.VISIBLE
        }
    }

    private fun setThirdAction(thirdAction: String) {
        binding.run {
            tvAdditionActionThird.text = thirdAction
            tvAdditionActionThird.visibility = View.VISIBLE
            ivAdditionActionThirdDelete.visibility = View.VISIBLE
            etAdditionAction.visibility = View.GONE
            tvAdditionActionTextCount.visibility = View.GONE
        }
    }

    private fun initAdapters() {
        initMissionHistoryAdapter()
    }

    private fun initMissionHistoryAdapter() {
        missionHistoryAdapter = MissionHistoryAdapter(context, setMissionName)
    }

    private fun getRecommendSituationList() {
        viewModel.getRecommendSituationList()
    }

    private fun getRecentMissionList() {
        viewModel.getRecentMissionList()
    }

    private fun setClickEvent() {
        setAddButton()
        setFinishButton()
        setDeleteButtons()
    }

    private fun setViews() {
        initOpenedDesc()
        initToggles()
        initRecyclerViews()
        setActions()
    }

    private fun initRecyclerViews() {
        initAdapters()
        initMissionHistoryRecyclerView()
    }

    private fun setObservers() {
        observeMission()
        observeSituation()
        observeAction()
        observeGoal()
        observeSuccessResponse()
        observeFailureResponse()
        observeGetRecommendSituationList()
        observeGetRecentMissionListResponse()
    }

    private fun observeGetRecentMissionListResponse() {
        observeGetRecentMissionListSuccessResponse()
        observeGetRecentMissionListErrorResponse()
    }

    private fun observeGetRecentMissionListErrorResponse() {
        viewModel.getRecentMissionListListErrorResponse.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != PublicString.NO_INTERNET_CONDITION_ERROR) context.showToast(
                errorMessage
            )
        }
    }

    private val setMissionName: (String) -> Unit = { missionName: String ->
        NotTodoAmplitude.trackEventWithProperty(
            getString(R.string.click_mission_history), getString(R.string.title), missionName
        )
        binding.etAdditionMission.setText(missionName)
        binding.etAdditionMission.requestFocus()
        binding.etAdditionMission.setSelection(binding.etAdditionMission.length())
        context.showKeyboard(binding.etAdditionMission)
    }

    private fun observeGetRecentMissionListSuccessResponse() {
        viewModel.getRecentMissionListSuccessResponse.observe(viewLifecycleOwner) { response ->
            missionHistoryAdapter?.submitList(response.data.map {
                it.title
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
        viewModel.getRecommendSituationListErrorResponse.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage == PublicString.NO_INTERNET_CONDITION_ERROR) context.showNotTodoSnackBar(
                binding.root, PublicString.NO_INTERNET_CONDITION_ERROR
            ) else context.showToast(errorMessage)
        }
    }

    private fun observeFailureResponse() {
        viewModel.errorResponse.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage == PublicString.NO_INTERNET_CONDITION_ERROR) context.showNotTodoSnackBar(
                binding.root, PublicString.NO_INTERNET_CONDITION_ERROR
            ) else {
                val errorMessageWithHtmlTag =
                    HtmlCompat.fromHtml(errorMessage, HtmlCompat.FROM_HTML_MODE_COMPACT)
                context.showNotTodoSnackBar(binding.root, errorMessageWithHtmlTag)
                trackAdditionFailureEvent(errorMessage)
            }
        }
    }

    private fun trackAdditionFailureEvent(errorMessage: String) {
        when (errorMessage.first()) {
            '해' -> NotTodoAmplitude.trackEvent(getString(R.string.appear_same_mission_issue_message))
            '낫' -> NotTodoAmplitude.trackEvent(getString(R.string.appear_maxed_issue_message))
        }
    }

    private fun observeSuccessResponse() {
        viewModel.additionResponse.observe(viewLifecycleOwner) { response ->
            context.showToast(getString(R.string.complete_create_nottodo))
            trackCompleteCreateMission(response)
            val sortedList =
                response.dates.sortedBy { date -> date.achievementConvertStringToDate() }
            navigateToMain(sortedList.first().toString())
        }
    }

    private fun trackCompleteCreateMission(missionData: ResponseAdditionDto.Addition) {
        with(missionData) {
            val completeCreateMissionEventPropertyMap = mutableMapOf<String, Any>(
                getString(R.string.date) to dates.map { date -> date.convertDateStringToInt() },
                getString(R.string.title) to title,
                getString(R.string.situation) to situation
            )
            if (goal != null) completeCreateMissionEventPropertyMap.plus(getString(R.string.goal) to goal)
            if (actions != null) completeCreateMissionEventPropertyMap.plus(
                getString(R.string.action) to actions.toTypedArray()
            )
            NotTodoAmplitude.trackEventWithProperty(
                getString(R.string.complete_create_mission), completeCreateMissionEventPropertyMap
            )
        }
    }

    private fun navigateToMain(firstDate: String) {
        startActivity(
            Intent(context, MainActivity::class.java).setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            ).putExtra(FIRST_DATE, firstDate)
        )
        if (!activity.isFinishing) activity.finish()
    }

    private fun setActionBox(isActionFilled: Boolean) {
        if (isActionFilled) {
            binding.layoutAdditionActionClosed.background = AppCompatResources.getDrawable(
                context, R.drawable.rectangle_solid_gray_1_radius_12
            )
            binding.ivAdditionActionClosedCheck.visibility = View.VISIBLE
            binding.tvAdditionActionClosedChoice.visibility = View.GONE
            binding.tvAdditionActionClosedInput.setTextColor(context.getColor(R.color.white))
        } else {
            binding.layoutAdditionActionClosed.background = AppCompatResources.getDrawable(
                context, R.drawable.rectangle_stroke_1_gray_3_radius_12
            )
            binding.ivAdditionActionClosedCheck.visibility = View.GONE
            binding.tvAdditionActionClosedChoice.visibility = View.VISIBLE
            binding.tvAdditionActionClosedInput.setTextColor(context.getColor(R.color.gray_3_5d5d6b))
            binding.tvAdditionActionClosedInput.text = getString(R.string.addition_input)
        }
    }

    private fun setActions() {
        viewModel.actionCount.observe(viewLifecycleOwner) { actionCount ->
            when (actionCount) {
                0 -> {
                    setActionBox(isActionFilled = false)
                }

                1 -> {
                    setActionBox(isActionFilled = true)
                    binding.tvAdditionActionClosedInput.text = binding.tvAdditionActionFirst.text
                }

                2 -> {
                    setActionBox(isActionFilled = true)
                    binding.tvAdditionActionClosedInput.text = getString(
                        R.string.addition_action_2_text,
                        binding.tvAdditionActionFirst.text,
                        binding.tvAdditionActionSecond.text
                    )
                }

                3 -> {
                    setActionBox(isActionFilled = true)
                    binding.tvAdditionActionClosedInput.text = getString(
                        R.string.addition_action_3_text,
                        binding.tvAdditionActionFirst.text,
                        binding.tvAdditionActionSecond.text,
                        binding.tvAdditionActionThird.text
                    )
                }
            }
        }
    }

    private fun setEnterKey() {
        binding.etAdditionMission.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeMissionToggle()
                context.hideKeyboard(binding.root)
            }
            return@setOnEditorActionListener false
        }

        binding.etAdditionSituation.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeSituationToggle()
                context.hideKeyboard(binding.root)
            }
            return@setOnEditorActionListener false
        }

        binding.etAdditionAction.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && binding.etAdditionAction.text.isNotBlank()) {
                viewModel.actionCount.value?.let { addAction(it) }
            }
            return@setOnEditorActionListener true
        }

        binding.etAdditionGoal.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeGoalToggle()
                context.hideKeyboard(binding.root)
            }
            return@setOnEditorActionListener false
        }
    }

    private fun addAction(actionCount: Int) {
        when (actionCount) {
            0 -> {
                with(binding) {
                    tvAdditionActionFirst.text = viewModel.action.value
                    viewModel.action.value = MainActivity.BLANK
                    tvAdditionActionFirst.visibility = View.VISIBLE
                    ivAdditionActionFirstDelete.visibility = View.VISIBLE
                }
                viewModel.actionCount.value = 1
            }

            1 -> {
                with(binding) {
                    tvAdditionActionSecond.text = viewModel.action.value
                    viewModel.action.value = MainActivity.BLANK
                    tvAdditionActionSecond.visibility = View.VISIBLE
                    ivAdditionActionSecondDelete.visibility = View.VISIBLE
                }
                viewModel.actionCount.value = 2
            }

            2 -> {
                with(binding) {
                    tvAdditionActionThird.text = viewModel.action.value
                    viewModel.action.value = MainActivity.BLANK
                    tvAdditionActionThird.visibility = View.VISIBLE
                    ivAdditionActionThirdDelete.visibility = View.VISIBLE
                    etAdditionAction.visibility = View.GONE
                    tvAdditionActionTextCount.visibility = View.GONE
                    context.hideKeyboard(root)
                }
                viewModel.actionCount.value = 3
            }
        }
    }

    private fun setDeleteButtons() {
        binding.ivAdditionActionFirstDelete.setOnClickListener {
            when (viewModel.actionCount.value) {
                1 -> {
                    hideActionFirst()
                    viewModel.actionCount.value = 0
                }

                2 -> {
                    binding.tvAdditionActionFirst.text = binding.tvAdditionActionSecond.text
                    hideActionSecond()
                    viewModel.actionCount.value = 1
                }

                3 -> {
                    binding.tvAdditionActionFirst.text = binding.tvAdditionActionSecond.text
                    binding.tvAdditionActionSecond.text = binding.tvAdditionActionThird.text
                    hideActionThird()
                    viewModel.actionCount.value = 2
                }
            }
        }
        binding.ivAdditionActionSecondDelete.setOnClickListener {
            when (viewModel.actionCount.value) {
                2 -> {
                    hideActionSecond()
                    viewModel.actionCount.value = 1
                }

                3 -> {
                    binding.tvAdditionActionSecond.text = binding.tvAdditionActionThird.text
                    hideActionThird()
                    viewModel.actionCount.value = 2
                }
            }
        }
        binding.ivAdditionActionThirdDelete.setOnClickListener {
            hideActionThird()
            viewModel.actionCount.value = 2
        }
    }

    private fun hideActionFirst() {
        binding.tvAdditionActionFirst.text = MainActivity.BLANK
        binding.tvAdditionActionFirst.visibility = View.GONE
        binding.ivAdditionActionFirstDelete.visibility = View.GONE
    }

    private fun hideActionSecond() {
        binding.tvAdditionActionSecond.text = MainActivity.BLANK
        binding.tvAdditionActionSecond.visibility = View.GONE
        binding.ivAdditionActionSecondDelete.visibility = View.GONE
    }

    private fun hideActionThird() {
        binding.tvAdditionActionThird.text = MainActivity.BLANK
        binding.tvAdditionActionThird.visibility = View.GONE
        binding.ivAdditionActionThirdDelete.visibility = View.GONE
        binding.etAdditionAction.visibility = View.VISIBLE
        binding.tvAdditionActionTextCount.visibility = View.VISIBLE
        requestFocusWithShowingKeyboard(binding.etAdditionAction)
    }

    private fun setFinishButton() {
        binding.ivAdditionDelete.setOnClickListener { if (!activity.isFinishing) activity.finish() }

    }

    private fun setSituationRecommendations(situationList: List<String>) {
        binding.layoutAdditionSituationRecommend.addButtons(
            situationList, binding.etAdditionSituation
        )
    }

    private fun setAddButton() {
        viewModel.isAbleToAdd.observe(viewLifecycleOwner) { isAbleToAdd ->
            if (isAbleToAdd == true) {
                binding.btnAdditionAdd.setTextColor(context.getColor(R.color.gray_1_2a2a2e))
                binding.btnAdditionAdd.setBackgroundResource(R.drawable.rectangle_green_2_radius_26)
            } else {
                binding.btnAdditionAdd.setTextColor(context.getColor(R.color.gray_3_5d5d6b))
                binding.btnAdditionAdd.setBackgroundResource(R.drawable.rectangle_gray_2_radius_26)
            }
        }

        binding.btnAdditionAdd.setOnClickListener {
            if (binding.btnAdditionAdd.currentTextColor != context.getColor(R.color.gray_1_2a2a2e)) return@setOnClickListener

            var actionList: MutableList<String>? = mutableListOf()
            if (!binding.tvAdditionActionFirst.text.isNullOrBlank()) actionList?.add(binding.tvAdditionActionFirst.text.toString())
            if (!binding.tvAdditionActionSecond.text.isNullOrBlank()) actionList?.add(binding.tvAdditionActionSecond.text.toString())
            if (!binding.tvAdditionActionThird.text.isNullOrBlank()) actionList?.add(binding.tvAdditionActionThird.text.toString())
            if (actionList?.isEmpty() == true) actionList = null

            var goal: String? = viewModel.goal.value
            if (goal?.isBlank() == true) goal = null

            val dateList: List<String> =
                binding.calendarAdditionDateOpened.selectedDays.mapNotNull { selectedDay ->
                    selectedDay.convertDateToString()
                }.sortedDescending()

            val requestAdditionDto = RequestAdditionDto(
                title = binding.tvAdditionMissionClosedName.text.toString(),
                situation = binding.tvAdditionSituationName.text.toString(),
                actions = actionList,
                goal = goal,
                dates = dateList
            )
            trackClickCreateMission(requestAdditionDto)
            viewModel.postAddition(requestAdditionDto)
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
        viewModel.goal.observe(viewLifecycleOwner) { goal ->
            binding.tvAdditionGoalTextCount.text = getString(R.string.max_text_size_20, goal.length)
            if (goal.isNotBlank()) {
                binding.layoutAdditionGoalClosed.background = AppCompatResources.getDrawable(
                    context, R.drawable.rectangle_solid_gray_1_radius_12
                )
                binding.ivAdditionGoalCheck.visibility = View.VISIBLE
                binding.tvAdditionGoalClosedChoice.visibility = View.GONE
                with(binding.tvAdditionGoalInput) {
                    text = viewModel.goal.value
                    setTextColor(context.getColor(R.color.white))
                }

            } else {
                binding.layoutAdditionGoalClosed.background = AppCompatResources.getDrawable(
                    context, R.drawable.rectangle_stroke_1_gray_3_radius_12
                )
                binding.ivAdditionGoalCheck.visibility = View.GONE
                binding.tvAdditionGoalClosedChoice.visibility = View.VISIBLE
                with(binding.tvAdditionGoalInput) {
                    text = getText(R.string.addition_input)
                    setTextColor(context.getColor(R.color.gray_3_5d5d6b))
                }
            }
        }
    }

    private fun observeAction() {
        viewModel.action.observe(viewLifecycleOwner) { action ->
            binding.tvAdditionActionTextCount.text =
                getString(R.string.max_text_size_20, action.length)
        }
    }

    private fun observeSituation() {
        viewModel.situation.observe(viewLifecycleOwner) { situation ->
            binding.tvAdditionSituationTextCount.text =
                getString(R.string.max_text_size_20, situation.length)
            if (situation.isNotBlank()) {
                binding.layoutAdditionSituationClosed.background = AppCompatResources.getDrawable(
                    context, R.drawable.rectangle_solid_gray_1_radius_12
                )
                binding.ivAdditionSituationCheck.visibility = View.VISIBLE
                with(binding.tvAdditionSituationName) {
                    text = viewModel.situation.value
                    setTextColor(context.getColor(R.color.white))
                }

            } else {
                binding.layoutAdditionSituationClosed.background = AppCompatResources.getDrawable(
                    context, R.drawable.rectangle_stroke_1_gray_3_radius_12
                )
                binding.ivAdditionSituationCheck.visibility = View.GONE
                with(binding.tvAdditionSituationName) {
                    text = getText(R.string.addition_input)
                    setTextColor(context.getColor(R.color.gray_3_5d5d6b))
                }
            }
        }
    }

    private fun observeMission() {
        viewModel.mission.observe(viewLifecycleOwner) { mission ->
            binding.tvAdditionMissionTextCount.text =
                getString(R.string.max_text_size_20, mission.length)
            if (mission.isNotBlank()) {
                binding.layoutAdditionMissionClosed.background = AppCompatResources.getDrawable(
                    context, R.drawable.rectangle_solid_gray_1_radius_12
                )
                binding.ivAdditionMissionClosedCheck.visibility = View.VISIBLE
                with(binding.tvAdditionMissionClosedName) {
                    text = viewModel.mission.value
                    setTextColor(context.getColor(R.color.white))
                }
                binding.tvAdditionMissionRvTitle.visibility = View.GONE
                binding.rvAdditionMission.visibility = View.GONE

            } else {
                binding.layoutAdditionMissionClosed.background = AppCompatResources.getDrawable(
                    context, R.drawable.rectangle_stroke_1_gray_3_radius_12
                )
                binding.ivAdditionMissionClosedCheck.visibility = View.GONE
                with(binding.tvAdditionMissionClosedName) {
                    text = getText(R.string.addition_input)
                    setTextColor(context.getColor(R.color.gray_3_5d5d6b))
                }
                binding.tvAdditionMissionRvTitle.visibility = View.VISIBLE
                binding.rvAdditionMission.visibility = View.VISIBLE
            }
        }
    }

    private fun initToggles() {
        binding.layoutAdditionMissionClosed.setOnClickListener {
            if (!viewModel.isMissionToggleVisible) {
                openMissionToggle()
                closeDateToggle()
                closeSituationToggle()
                closeActionToggle()
                closeGoalToggle()
            } else {
                closeMissionToggle()
            }
        }

        binding.layoutAdditionSituationClosed.setOnClickListener {
            if (!viewModel.isSituationToggleVisible) {
                openSituationToggle()
                closeDateToggle()
                closeMissionToggle()
                closeActionToggle()
                closeGoalToggle()
            } else {
                closeSituationToggle()
            }
        }

        binding.layoutAdditionActionClosed.setOnClickListener {
            if (!viewModel.isActionToggleVisible) {
                openActionToggle()
                closeDateToggle()
                closeMissionToggle()
                closeSituationToggle()
                closeGoalToggle()
            } else {
                closeActionToggle()
            }
        }

        binding.layoutAdditionGoalClosed.setOnClickListener {
            if (!viewModel.isGoalToggleVisible) {
                openGoalToggle()
                closeDateToggle()
                closeMissionToggle()
                closeSituationToggle()
                closeActionToggle()
            } else {
                closeGoalToggle()
            }
        }

        binding.layoutAdditionDateClosed.setOnClickListener {
            if (!viewModel.isDateToggleVisible) {
                openDateToggle()
                closeMissionToggle()
                closeSituationToggle()
                closeActionToggle()
                closeGoalToggle()

            } else {
                closeDateToggle()
            }
        }
        binding.tvAdditionDateOpenedComplete.setOnClickListener {
            closeDateToggle()
        }

        binding.tvAdditionActionComplete.setOnClickListener {
            closeActionToggle()
        }
    }

    private fun setDateDescTextView() {
        val selectedDays: MutableList<Date> = binding.calendarAdditionDateOpened.selectedDays
        if (selectedDays.isEmpty()) return

        if (selectedDays.containToday()) {
            binding.tvAdditionDateStartDesc.apply {
                visibility = View.VISIBLE
                text = getString(R.string.today)
            }
        } else if (selectedDays.containTomorrow()) {
            binding.tvAdditionDateStartDesc.apply {
                visibility = View.VISIBLE
                text = getString(R.string.tomorrow)
            }
        } else {
            binding.tvAdditionDateStartDesc.visibility = View.GONE
        }

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

        selectedDays.sortDescending()
        binding.tvAdditionDate.text = selectedDays.last().convertDateToString()
    }

    private fun closeDateToggle() {
        binding.layoutAdditionDateClosed.visibility = View.VISIBLE
        binding.layoutAdditionDateOpened.visibility = View.GONE
        viewModel.isDateToggleVisible = false
        setDateDescTextView()
    }

    private fun openDateToggle() {
        binding.layoutAdditionDateClosed.visibility = View.GONE
        binding.layoutAdditionDateOpened.visibility = View.VISIBLE
        viewModel.isDateToggleVisible = true
    }

    private fun closeGoalToggle() {
        binding.layoutAdditionGoalClosed.visibility = View.VISIBLE
        binding.layoutAdditionGoalOpened.visibility = View.GONE
        viewModel.isGoalToggleVisible = false
    }

    private fun requestFocusWithShowingKeyboard(editText: EditText) {
        editText.requestFocus()
        editText.setSelection(editText.length())
        context.showKeyboard(editText)
    }

    private fun openGoalToggle() {
        binding.layoutAdditionGoalClosed.visibility = View.GONE
        binding.layoutAdditionGoalOpened.visibility = View.VISIBLE
        viewModel.isGoalToggleVisible = true
        requestFocusWithShowingKeyboard(binding.etAdditionGoal)
    }

    private fun openActionToggle() {
        binding.layoutAdditionActionClosed.visibility = View.GONE
        binding.layoutAdditionActionOpened.visibility = View.VISIBLE
        viewModel.isActionToggleVisible = true
        requestFocusWithShowingKeyboard(binding.etAdditionAction)
    }

    private fun closeActionToggle() {
        binding.layoutAdditionActionClosed.visibility = View.VISIBLE
        binding.layoutAdditionActionOpened.visibility = View.GONE
        viewModel.isActionToggleVisible = false
    }

    private fun openSituationToggle() {
        binding.layoutAdditionSituationClosed.visibility = View.GONE
        binding.layoutAdditionSituationOpened.visibility = View.VISIBLE
        viewModel.isSituationToggleVisible = true
        requestFocusWithShowingKeyboard(binding.etAdditionSituation)
    }

    private fun closeSituationToggle() {
        binding.layoutAdditionSituationClosed.visibility = View.VISIBLE
        binding.layoutAdditionSituationOpened.visibility = View.GONE
        viewModel.isSituationToggleVisible = false
    }

    private fun closeMissionToggle() {
        binding.layoutAdditionMissionClosed.visibility = View.VISIBLE
        binding.layoutAdditionMissionOpened.visibility = View.GONE
        viewModel.isMissionToggleVisible = false
    }

    private fun openMissionToggle() {
        binding.layoutAdditionMissionClosed.visibility = View.GONE
        binding.layoutAdditionMissionOpened.visibility = View.VISIBLE
        viewModel.isMissionToggleVisible = true
        requestFocusWithShowingKeyboard(binding.etAdditionMission)
    }

    private fun initOpenedDesc() {
        val missionOpenedDesc = SpannableStringBuilder(getString(R.string.mission_desc))
        missionOpenedDesc.setSpan(
            ForegroundColorSpan(context.getColor(R.color.white)),
            0,
            2,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        missionOpenedDesc.setSpan(
            ForegroundColorSpan(context.getColor(R.color.green_1_98ffa9)),
            3,
            6,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        missionOpenedDesc.setSpan(
            ForegroundColorSpan(context.getColor(R.color.white)),
            6,
            14,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvAdditionMissionOpenedDesc.text = missionOpenedDesc

        val situationOpenedDesc = SpannableStringBuilder(
            getString(R.string.situation_desc)
        )
        situationOpenedDesc.setSpan(
            ForegroundColorSpan(context.getColor(R.color.white)),
            0,
            9,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        situationOpenedDesc.setSpan(
            ForegroundColorSpan(context.getColor(R.color.green_1_98ffa9)),
            10,
            12,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        situationOpenedDesc.setSpan(
            ForegroundColorSpan(context.getColor(R.color.white)),
            12,
            21,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvAdditionSituationOpenedDesc.text = situationOpenedDesc

        val actionOpenedDesc = SpannableStringBuilder(getString(R.string.action_desc))
        actionOpenedDesc.setSpan(
            ForegroundColorSpan(context.getColor(R.color.white)),
            0,
            2,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        actionOpenedDesc.setSpan(
            ForegroundColorSpan(context.getColor(R.color.green_1_98ffa9)),
            3,
            5,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        actionOpenedDesc.setSpan(
            ForegroundColorSpan(context.getColor(R.color.white)),
            5,
            19,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvAdditionActionOpenedDesc.text = actionOpenedDesc

        val goalOpenedDesc = SpannableStringBuilder(getString(R.string.goal_desc))
        goalOpenedDesc.setSpan(
            ForegroundColorSpan(context.getColor(R.color.white)),
            0,
            11,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        goalOpenedDesc.setSpan(
            ForegroundColorSpan(context.getColor(R.color.green_1_98ffa9)),
            12,
            14,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        goalOpenedDesc.setSpan(
            ForegroundColorSpan(context.getColor(R.color.white)),
            14,
            24,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvAdditionGoalOpenedDesc.text = goalOpenedDesc
    }

    private fun initMissionHistoryRecyclerView() {
        binding.rvAdditionMission.adapter = missionHistoryAdapter
    }

    companion object {
        const val FIRST_DATE = "FIRST_DATE"
    }

    override fun setBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentAdditionBinding = FragmentAdditionBinding.inflate(inflater, container, false)
}