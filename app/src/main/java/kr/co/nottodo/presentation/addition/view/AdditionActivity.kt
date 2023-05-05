package kr.co.nottodo.presentation.addition.view

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import kr.co.nottodo.MainActivity.Companion.BLANK
import kr.co.nottodo.R
import kr.co.nottodo.data.remote.model.RequestAdditionDto
import kr.co.nottodo.databinding.ActivityAdditionBinding
import kr.co.nottodo.presentation.addition.adapter.MissionHistoryAdapter
import kr.co.nottodo.presentation.addition.viewmodel.AdditionViewModel
import kr.co.nottodo.util.addButtons
import kr.co.nottodo.util.hideKeyboard
import kr.co.nottodo.util.showKeyboard
import kr.co.nottodo.util.showToast

class AdditionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdditionBinding
    private val viewModel by viewModels<AdditionViewModel>()
    private var isDateToggleVisible: Boolean = false
    private var isMissionToggleVisible: Boolean = false
    private var isSituationToggleVisible: Boolean = false
    private var isActionToggleVisible: Boolean = false
    private var isGoalToggleVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDataBinding()
        initRecyclerView(setMissionName)
        setSituationRecommendations()
        initOpenedDesc()
        initToggles()

        observeMission()
        observeSituation()
        observeAction()
        observeGoal()
        observeSuccessResponse()
        observeFailureResponse()

        setAddButton()
        setFinishButton()
        setDeleteButtons()
        setEnterKey()
        setActions()
    }

    private fun observeFailureResponse() {
        viewModel.errorResponse.observe(this) {
            showToast(it)
        }
    }

    private fun observeSuccessResponse() {
        viewModel.additionResponse.observe(this) {
            showToast("추가 성공")
        }
    }

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
            if (actionId == EditorInfo.IME_ACTION_DONE) {
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

    private val setMissionName: (String) -> Unit = { missionName: String ->
        binding.etAdditionMission.setText(missionName)
        binding.etAdditionMission.requestFocus()
        binding.etAdditionMission.setSelection(binding.etAdditionMission.length())
        this.showKeyboard(binding.etAdditionMission)
    }

    private fun setSituationRecommendations() {
        binding.layoutAdditionSituationRecommend.addButtons(
            listOf("업무 시간 중", "작업 중", "기상 시간", "공부 시간", "취침 전", "출근 중"), binding.etAdditionSituation
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
            if (binding.btnAdditionAdd.currentTextColor == getColor(R.color.gray_1_2a2a2e)) {
                // 낫투두 추가
                // 액션 리스트 생성
                var actionList: List<String>? = listOf()
                if (binding.tvAdditionActionFirst.visibility == View.GONE)
                    actionList?.plus(binding.tvAdditionActionFirst.text)
                if (binding.tvAdditionActionSecond.visibility == View.GONE)
                    actionList?.plus(binding.tvAdditionActionSecond.text)
                if (binding.tvAdditionActionThird.visibility == View.GONE)
                    actionList?.plus(binding.tvAdditionActionThird.text)
                if (actionList.isNullOrEmpty()) actionList = null

                viewModel.postAddition(
//                    RequestAdditionDto(
//                        title = binding.tvAdditionMissionClosedName.text.toString(),
//                        situation = binding.tvAdditionSituationName.text.toString(),
//                        actions = actionList,
//                        goal = null,
//                        dates = listOf("2020.12.12")
//                    )
                    RequestAdditionDto(
                        title = "123",
                        situation = "123",
                        actions = null,
                        goal = null,
                        dates = listOf("2002.12.12")
                    )
                )
            }
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

    private fun closeDateToggle() {
        binding.layoutAdditionDateClosed.visibility = View.VISIBLE
        binding.layoutAdditionDateOpened.visibility = View.GONE
        isDateToggleVisible = false
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

    private fun initRecyclerView(setMissionName: (String) -> Unit) {
        binding.rvAdditionMission.adapter = MissionHistoryAdapter(this, setMissionName)
    }

    companion object {
        const val maxTextSize = "/20"
        const val missionOpenedDesc = "어떤 낫투두를 설정해볼까요?"
        const val situationOpenedDesc = "어떤 상황에서\n낫투두를 실천하고 싶나요?"
        const val actionOpenedDesc = "낫투두를 이루기 위해서\n어떤 행동이 필요한가요?"
        const val goalOpenedDesc = "낫투두를 통해서\n어떤 목표를 이루려 하나요?"
    }
}