package kr.co.nottodo.presentation.addition.view

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import kr.co.nottodo.MainActivity
import kr.co.nottodo.MainActivity.Companion.BLANK
import kr.co.nottodo.R
import kr.co.nottodo.data.remote.model.addition.RequestAdditionDto
import kr.co.nottodo.databinding.ActivityAdditionBinding
import kr.co.nottodo.presentation.addition.adapter.MissionHistoryAdapter
import kr.co.nottodo.presentation.addition.viewmodel.AdditionViewModel
import kr.co.nottodo.presentation.recommendation.action.view.RecommendActionActivity.Companion.MISSION_ACTION_DETAIL
import kr.co.nottodo.presentation.recommendation.model.RecommendMissionActionUiModel
import kr.co.nottodo.util.addButtons
import kr.co.nottodo.util.containToday
import kr.co.nottodo.util.containTomorrow
import kr.co.nottodo.util.getParcelable
import kr.co.nottodo.util.hideKeyboard
import kr.co.nottodo.util.showKeyboard
import kr.co.nottodo.util.showToast
import kr.co.nottodo.view.calendar.monthly.util.convertDateToString
import java.util.Date

class AdditionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdditionBinding
    private val viewModel by viewModels<AdditionViewModel>()
    private var isDateToggleVisible: Boolean = false
    private var isMissionToggleVisible: Boolean = false
    private var isSituationToggleVisible: Boolean = false
    private var isActionToggleVisible: Boolean = false
    private var isGoalToggleVisible: Boolean = false
    private var missionHistoryAdapter: MissionHistoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        setData()
        setViews()
        setObservers()
        setClickEvent()
        setEnterKey()
    }

    private fun setData() {
        getRecentMissionList()
        getRecommendSituationList()
        getDataFromRecommendActivity()
    }

    private fun getDataFromRecommendActivity() {
        val dataFromRecommendActivity: RecommendMissionActionUiModel =
            intent.getParcelable(MISSION_ACTION_DETAIL, RecommendMissionActionUiModel::class.java)
                ?: return

        with(viewModel) {
            mission.value = dataFromRecommendActivity.title
            situation.value = dataFromRecommendActivity.situation
        }
        val recommendActionList = dataFromRecommendActivity.actionList
        initActionList(recommendActionList)
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
        binding.tvAdditionActionFirst.text = firstAction
        binding.tvAdditionActionFirst.visibility = View.VISIBLE
        binding.ivAdditionActionFirstDelete.visibility = View.VISIBLE
    }

    private fun setSecondAction(secondAction: String) {
        binding.tvAdditionActionSecond.text = secondAction
        binding.tvAdditionActionSecond.visibility = View.VISIBLE
        binding.ivAdditionActionSecondDelete.visibility = View.VISIBLE
    }

    private fun setThirdAction(thirdAction: String) {
        binding.tvAdditionActionThird.text = thirdAction
        binding.tvAdditionActionThird.visibility = View.VISIBLE
        binding.ivAdditionActionThirdDelete.visibility = View.VISIBLE
        binding.etAdditionAction.visibility = View.GONE
        binding.tvAdditionActionTextCount.visibility = View.GONE
    }

    private fun initAdapters() {
        initMissionHistoryAdapter()
    }

    private fun initMissionHistoryAdapter() {
        missionHistoryAdapter = MissionHistoryAdapter(this, setMissionName)
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
        viewModel.getRecentMissionListListErrorResponse.observe(this) { errorMessage ->
            showToast(errorMessage)
        }
    }

    private val setMissionName: (String) -> Unit = { missionName: String ->
        binding.etAdditionMission.setText(missionName)
        binding.etAdditionMission.requestFocus()
        binding.etAdditionMission.setSelection(binding.etAdditionMission.length())
        this.showKeyboard(binding.etAdditionMission)
    }

    private fun observeGetRecentMissionListSuccessResponse() {
        viewModel.getRecentMissionListSuccessResponse.observe(this) { response ->
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
        viewModel.getRecommendSituationListSuccessResponse.observe(this) { response ->
            setSituationRecommendations(response.data.map { situation -> situation.name })
        }
    }

    private fun observeGetRecommendSituationListErrorResponse() {
        viewModel.getRecommendSituationListErrorResponse.observe(this) { errorMessage ->
            showToast(errorMessage)
        }
    }

    private fun observeFailureResponse() {
        viewModel.errorResponse.observe(this) {
            showToast(it)
        }
    }

    private fun observeSuccessResponse() {
        viewModel.additionResponse.observe(this) {
            showToast("낫투두 생성 완료 !")
            navigateToMain()
            if (!isFinishing) finish()
        }
    }

    private fun navigateToMain() = startActivity(
        Intent(this, MainActivity::class.java).setFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
    )

    private fun setActionBox(isActionFilled: Boolean) {
        if (isActionFilled) {
            binding.layoutAdditionActionClosed.background = AppCompatResources.getDrawable(
                this, R.drawable.rectangle_solid_gray_1_radius_12
            )
            binding.ivAdditionActionClosedCheck.visibility = View.VISIBLE
            binding.tvAdditionActionClosedChoice.visibility = View.GONE
            binding.tvAdditionActionClosedInput.setTextColor(getColor(R.color.white))
        } else {
            binding.layoutAdditionActionClosed.background = AppCompatResources.getDrawable(
                this, R.drawable.rectangle_stroke_1_gray_3_radius_12
            )
            binding.ivAdditionActionClosedCheck.visibility = View.GONE
            binding.tvAdditionActionClosedChoice.visibility = View.VISIBLE
            binding.tvAdditionActionClosedInput.setTextColor(getColor(R.color.gray_3_5d5d6b))
            binding.tvAdditionActionClosedInput.text = getString(R.string.addition_input)
        }
    }

    private fun setActions() {
        viewModel.actionCount.observe(this) { actionCount ->
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
                    binding.tvAdditionActionClosedInput.text =
                        "${binding.tvAdditionActionFirst.text}\n${binding.tvAdditionActionSecond.text}"
                }

                3 -> {
                    setActionBox(isActionFilled = true)
                    binding.tvAdditionActionClosedInput.text =
                        "${binding.tvAdditionActionFirst.text}\n${binding.tvAdditionActionSecond.text}\n${binding.tvAdditionActionThird.text}"
                }
            }
        }
    }

    private fun setEnterKey() {
        binding.etAdditionMission.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeMissionToggle()
                hideKeyboard(binding.root)
            }
            return@setOnEditorActionListener false
        }

        binding.etAdditionSituation.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeSituationToggle()
                hideKeyboard(binding.root)
            }
            return@setOnEditorActionListener false
        }

        binding.etAdditionAction.setOnEditorActionListener { _, actionId, _ ->
            //상황 추가 입력창 키보드 엔터 오버라이딩 -> 텍스트뷰 추가
            if (actionId == EditorInfo.IME_ACTION_DONE && binding.etAdditionAction.text.isNotBlank()) {
                viewModel.actionCount.value?.let { addAction(it) }
            }
            return@setOnEditorActionListener true
        }

        binding.etAdditionGoal.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeGoalToggle()
                hideKeyboard(binding.root)
            }
            return@setOnEditorActionListener false
        }
    }

    private fun addAction(actionCount: Int) {
        when (actionCount) {
            0 -> {
                with(binding) {
                    tvAdditionActionFirst.text = viewModel.action.value
                    viewModel.action.value = BLANK
                    tvAdditionActionFirst.visibility = View.VISIBLE
                    ivAdditionActionFirstDelete.visibility = View.VISIBLE
                }
                viewModel.actionCount.value = 1
            }

            1 -> {
                with(binding) {
                    tvAdditionActionSecond.text = viewModel.action.value
                    viewModel.action.value = BLANK
                    tvAdditionActionSecond.visibility = View.VISIBLE
                    ivAdditionActionSecondDelete.visibility = View.VISIBLE
                }
                viewModel.actionCount.value = 2
            }

            2 -> {
                with(binding) {
                    tvAdditionActionThird.text = viewModel.action.value
                    viewModel.action.value = BLANK
                    tvAdditionActionThird.visibility = View.VISIBLE
                    ivAdditionActionThirdDelete.visibility = View.VISIBLE
                    etAdditionAction.visibility = View.GONE
                    tvAdditionActionTextCount.visibility = View.GONE
                    hideKeyboard(root)
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
        binding.tvAdditionActionFirst.text = BLANK
        binding.tvAdditionActionFirst.visibility = View.GONE
        binding.ivAdditionActionFirstDelete.visibility = View.GONE
    }

    private fun hideActionSecond() {
        binding.tvAdditionActionSecond.text = BLANK
        binding.tvAdditionActionSecond.visibility = View.GONE
        binding.ivAdditionActionSecondDelete.visibility = View.GONE
    }

    private fun hideActionThird() {
        binding.tvAdditionActionThird.text = BLANK
        binding.tvAdditionActionThird.visibility = View.GONE
        binding.ivAdditionActionThirdDelete.visibility = View.GONE
        binding.etAdditionAction.visibility = View.VISIBLE
        binding.tvAdditionActionTextCount.visibility = View.VISIBLE
        requestFocusWithShowingKeyboard(binding.etAdditionAction)
    }

    private fun setFinishButton() {
        binding.ivAdditionDelete.setOnClickListener { finish() }

    }

    private fun setSituationRecommendations(situationList: List<String>) {
        binding.layoutAdditionSituationRecommend.addButtons(
            situationList, binding.etAdditionSituation
        )
    }

    private fun setAddButton() {
        viewModel.isAbleToAdd.observe(this) { isAbleToAdd ->
            if (isAbleToAdd == true) {
                binding.btnAdditionAdd.setTextColor(getColor(R.color.gray_1_2a2a2e))
                binding.btnAdditionAdd.setBackgroundResource(R.drawable.rectangle_green_2_radius_26)
            } else {
                binding.btnAdditionAdd.setTextColor(getColor(R.color.gray_3_5d5d6b))
                binding.btnAdditionAdd.setBackgroundResource(R.drawable.rectangle_gray_2_radius_26)
            }
        }

        binding.btnAdditionAdd.setOnClickListener {
            if (binding.btnAdditionAdd.currentTextColor != getColor(R.color.gray_1_2a2a2e)) return@setOnClickListener

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
                }
            viewModel.postAddition(
                RequestAdditionDto(
                    title = binding.tvAdditionMissionClosedName.text.toString(),
                    situation = binding.tvAdditionSituationName.text.toString(),
                    actions = actionList,
                    goal = goal,
                    dates = dateList
                )
            )
        }
    }

    private fun observeGoal() {
        viewModel.goal.observe(this) { goal ->
            binding.tvAdditionGoalTextCount.text = goal.length.toString() + maxTextSize
            if (goal.isNotBlank()) {
                binding.layoutAdditionGoalClosed.background = AppCompatResources.getDrawable(
                    this, R.drawable.rectangle_solid_gray_1_radius_12
                )
                binding.ivAdditionGoalCheck.visibility = View.VISIBLE
                binding.tvAdditionGoalClosedChoice.visibility = View.GONE
                with(binding.tvAdditionGoalInput) {
                    text = viewModel.goal.value
                    setTextColor(getColor(R.color.white))
                }

            } else {
                binding.layoutAdditionGoalClosed.background = AppCompatResources.getDrawable(
                    this, R.drawable.rectangle_stroke_1_gray_3_radius_12
                )
                binding.ivAdditionGoalCheck.visibility = View.GONE
                binding.tvAdditionGoalClosedChoice.visibility = View.VISIBLE
                with(binding.tvAdditionGoalInput) {
                    text = getText(R.string.addition_input)
                    setTextColor(getColor(R.color.gray_3_5d5d6b))
                }
            }
        }
    }

    private fun observeAction() {
        viewModel.action.observe(this) { action ->
            binding.tvAdditionActionTextCount.text = action.length.toString() + maxTextSize
        }
    }

    private fun observeSituation() {
        viewModel.situation.observe(this) { situation ->
            binding.tvAdditionSituationTextCount.text = situation.length.toString() + maxTextSize
            if (situation.isNotBlank()) {
                binding.layoutAdditionSituationClosed.background = AppCompatResources.getDrawable(
                    this, R.drawable.rectangle_solid_gray_1_radius_12
                )
                binding.ivAdditionSituationCheck.visibility = View.VISIBLE
                with(binding.tvAdditionSituationName) {
                    text = viewModel.situation.value
                    setTextColor(getColor(R.color.white))
                }

            } else {
                binding.layoutAdditionSituationClosed.background = AppCompatResources.getDrawable(
                    this, R.drawable.rectangle_stroke_1_gray_3_radius_12
                )
                binding.ivAdditionSituationCheck.visibility = View.GONE
                with(binding.tvAdditionSituationName) {
                    text = getText(R.string.addition_input)
                    setTextColor(getColor(R.color.gray_3_5d5d6b))
                }
            }
        }
    }

    private fun observeMission() {
        viewModel.mission.observe(this) { mission ->
            binding.tvAdditionMissionTextCount.text = mission.length.toString() + maxTextSize
            if (mission.isNotBlank()) {
                binding.layoutAdditionMissionClosed.background = AppCompatResources.getDrawable(
                    this, R.drawable.rectangle_solid_gray_1_radius_12
                )
                binding.ivAdditionMissionClosedCheck.visibility = View.VISIBLE
                with(binding.tvAdditionMissionClosedName) {
                    text = viewModel.mission.value
                    setTextColor(getColor(R.color.white))
                }
                binding.tvAdditionMissionRvTitle.visibility = View.GONE
                binding.rvAdditionMission.visibility = View.GONE

            } else {
                binding.layoutAdditionMissionClosed.background = AppCompatResources.getDrawable(
                    this, R.drawable.rectangle_stroke_1_gray_3_radius_12
                )
                binding.ivAdditionMissionClosedCheck.visibility = View.GONE
                with(binding.tvAdditionMissionClosedName) {
                    text = getText(R.string.addition_input)
                    setTextColor(getColor(R.color.gray_3_5d5d6b))
                }
                binding.tvAdditionMissionRvTitle.visibility = View.VISIBLE
                binding.rvAdditionMission.visibility = View.VISIBLE
            }
        }
    }

    private fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_addition)
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    private fun initToggles() {
        binding.layoutAdditionMissionClosed.setOnClickListener {
            if (!isMissionToggleVisible) {
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
            if (!isSituationToggleVisible) {
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
            if (!isActionToggleVisible) {
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
            if (!isGoalToggleVisible) {
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
            if (!isDateToggleVisible) {
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
        isDateToggleVisible = false
        setDateDescTextView()
    }

    private fun openDateToggle() {
        binding.layoutAdditionDateClosed.visibility = View.GONE
        binding.layoutAdditionDateOpened.visibility = View.VISIBLE
        isDateToggleVisible = true
    }

    private fun closeGoalToggle() {
        binding.layoutAdditionGoalClosed.visibility = View.VISIBLE
        binding.layoutAdditionGoalOpened.visibility = View.GONE
        isGoalToggleVisible = false
    }

    private fun requestFocusWithShowingKeyboard(editText: EditText) {
        editText.requestFocus()
        editText.setSelection(editText.length())
        showKeyboard(editText)
    }

    private fun openGoalToggle() {
        binding.layoutAdditionGoalClosed.visibility = View.GONE
        binding.layoutAdditionGoalOpened.visibility = View.VISIBLE
        isGoalToggleVisible = true
        requestFocusWithShowingKeyboard(binding.etAdditionGoal)
    }

    private fun openActionToggle() {
        binding.layoutAdditionActionClosed.visibility = View.GONE
        binding.layoutAdditionActionOpened.visibility = View.VISIBLE
        isActionToggleVisible = true
        requestFocusWithShowingKeyboard(binding.etAdditionAction)
    }

    private fun closeActionToggle() {
        binding.layoutAdditionActionClosed.visibility = View.VISIBLE
        binding.layoutAdditionActionOpened.visibility = View.GONE
        isActionToggleVisible = false
    }

    private fun openSituationToggle() {
        binding.layoutAdditionSituationClosed.visibility = View.GONE
        binding.layoutAdditionSituationOpened.visibility = View.VISIBLE
        isSituationToggleVisible = true
        requestFocusWithShowingKeyboard(binding.etAdditionSituation)
    }

    private fun closeSituationToggle() {
        binding.layoutAdditionSituationClosed.visibility = View.VISIBLE
        binding.layoutAdditionSituationOpened.visibility = View.GONE
        isSituationToggleVisible = false
    }

    private fun closeMissionToggle() {
        binding.layoutAdditionMissionClosed.visibility = View.VISIBLE
        binding.layoutAdditionMissionOpened.visibility = View.GONE
        isMissionToggleVisible = false
    }

    private fun openMissionToggle() {
        binding.layoutAdditionMissionClosed.visibility = View.GONE
        binding.layoutAdditionMissionOpened.visibility = View.VISIBLE
        isMissionToggleVisible = true
        requestFocusWithShowingKeyboard(binding.etAdditionMission)
    }

    private fun initOpenedDesc() {
        val missionOpenedDesc = SpannableStringBuilder(missionOpenedDesc)
        missionOpenedDesc.setSpan(
            ForegroundColorSpan(getColor(R.color.white)), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        missionOpenedDesc.setSpan(
            ForegroundColorSpan(getColor(R.color.green_1_98ffa9)),
            3,
            6,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        missionOpenedDesc.setSpan(
            ForegroundColorSpan(getColor(R.color.white)), 6, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvAdditionMissionOpenedDesc.text = missionOpenedDesc

        val situationOpenedDesc = SpannableStringBuilder(
            situationOpenedDesc
        )
        situationOpenedDesc.setSpan(
            ForegroundColorSpan(getColor(R.color.white)), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        situationOpenedDesc.setSpan(
            ForegroundColorSpan(getColor(R.color.green_1_98ffa9)),
            3,
            5,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        situationOpenedDesc.setSpan(
            ForegroundColorSpan(getColor(R.color.white)), 5, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvAdditionSituationOpenedDesc.text = situationOpenedDesc

        val actionOpenedDesc = SpannableStringBuilder(actionOpenedDesc)
        actionOpenedDesc.setSpan(
            ForegroundColorSpan(getColor(R.color.white)), 0, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        actionOpenedDesc.setSpan(
            ForegroundColorSpan(getColor(R.color.green_1_98ffa9)),
            16,
            18,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        actionOpenedDesc.setSpan(
            ForegroundColorSpan(getColor(R.color.white)), 18, 26, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvAdditionActionOpenedDesc.text = actionOpenedDesc

        val goalOpenedDesc = SpannableStringBuilder(goalOpenedDesc)
        goalOpenedDesc.setSpan(
            ForegroundColorSpan(getColor(R.color.white)), 0, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        goalOpenedDesc.setSpan(
            ForegroundColorSpan(getColor(R.color.green_1_98ffa9)),
            12,
            14,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        goalOpenedDesc.setSpan(
            ForegroundColorSpan(getColor(R.color.white)), 14, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvAdditionGoalOpenedDesc.text = goalOpenedDesc
    }

    private fun initMissionHistoryRecyclerView() {
        binding.rvAdditionMission.adapter = missionHistoryAdapter
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard()
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        const val maxTextSize = "/20"
        const val missionOpenedDesc = "어떤 낫투두를 설정해볼까요?"
        const val situationOpenedDesc = "어떤 상황에서\n낫투두를 실천하고 싶나요?"
        const val actionOpenedDesc = "낫투두를 이루기 위해서\n어떤 행동이 필요한가요?"
        const val goalOpenedDesc = "낫투두를 통해서\n어떤 목표를 이루려 하나요?"
    }
}