package kr.co.nottodo.presentation.modification.view

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
import kr.co.nottodo.MainActivity.Companion.BLANK
import kr.co.nottodo.R
import kr.co.nottodo.data.local.ParcelizeBottomDetail
import kr.co.nottodo.data.local.ParcelizeBottomDetailRegister
import kr.co.nottodo.databinding.ActivityModificationBinding
import kr.co.nottodo.presentation.addition.adapter.MissionHistoryAdapter
import kr.co.nottodo.presentation.home.view.HomeMenuBottomSheetFragment.Companion.DETAIL
import kr.co.nottodo.presentation.modification.viewmodel.ModificationViewModel
import kr.co.nottodo.util.addButtons
import kr.co.nottodo.util.getParcelable
import kr.co.nottodo.util.hideKeyboard
import kr.co.nottodo.util.showKeyboard
import kr.co.nottodo.util.showToast

class ModificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModificationBinding
    private val viewModel by viewModels<ModificationViewModel>()
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
        initData()
        initActionList(viewModel.actionCount.value)

        observeMission()
        observeSituation()
        observeAction()
        observeGoal()
        observeSuccessResponse()
        observeFailureResponse()
        setActions()

        setModifyButton()
        setDeleteButtons()
        setFinishButton()
        setEnterKey()
    }

    private fun initActionList(actionCount: Int?) {
        when (actionCount) {
            1 -> {
                setFirstAction()
            }

            2 -> {
                setFirstAction()
                setSecondAction()
            }

            3 -> {
                setFirstAction()
                setSecondAction()
                setThirdAction()
            }
        }
    }

    private fun observeSuccessResponse() {
        viewModel.modificationResponse.observe(this) { response ->
            showToast("낫투두 수정 완료")
            val actionList = response.actions?.map {
                ParcelizeBottomDetailRegister.Action(it.name)
            } ?: emptyList()
            val resultExtraIntent = Intent().apply {
                putExtra(
                    DETAIL, ParcelizeBottomDetailRegister(
                        response.title, response.situation, actionList, response.goal
                    )
                )
            }
            setResult(RESULT_OK, resultExtraIntent)
            if (!isFinishing) finish()
        }
    }

    private fun observeFailureResponse() {
        viewModel.errorResponse.observe(this) { errorMessage ->
            showToast(errorMessage)
        }
    }

    private fun initData() {
        val dataFromHome = intent.getParcelable(DETAIL, ParcelizeBottomDetail::class.java)
        if (dataFromHome == null) {
            showToast("오류로 인해 해당 낫투두를 수정할 수 없습니다")
            if (!isFinishing) finish()
        }
        viewModel.setOriginalData(
            NotTodoData(
                "2023.01.15",
                dataFromHome!!.title,
                dataFromHome.situation,
                dataFromHome.actions?.map { action -> action.name.toString() },
                dataFromHome.goal,
                dataFromHome.id
            )
        )
    }

    private fun setThirdAction() {
        if (viewModel.actionList.value?.get(2) != null) {
            binding.tvModificationActionThird.text = viewModel.actionList.value?.get(2) ?: BLANK
            binding.tvModificationActionThird.visibility = View.VISIBLE
            binding.ivModificationActionThirdDelete.visibility = View.VISIBLE
        }
    }

    private fun setSecondAction() {
        if (viewModel.actionList.value?.get(1) != null) {
            binding.tvModificationActionSecond.text = viewModel.actionList.value?.get(1) ?: BLANK
            binding.tvModificationActionSecond.visibility = View.VISIBLE
            binding.ivModificationActionSecondDelete.visibility = View.VISIBLE
        }
    }

    private fun setFirstAction() {
        if (viewModel.actionList.value?.get(0) != null) {
            binding.tvModificationActionFirst.text = viewModel.actionList.value?.get(0) ?: BLANK
            binding.tvModificationActionFirst.visibility = View.VISIBLE
            binding.ivModificationActionFirstDelete.visibility = View.VISIBLE
        }
    }

    private fun setEnterKey() {
        binding.etModificationMission.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeMissionToggle()
                hideKeyboard(binding.root)
            }
            return@setOnEditorActionListener false
        }

        binding.etModificationSituation.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeSituationToggle()
                hideKeyboard(binding.root)
            }
            return@setOnEditorActionListener false
        }

        binding.etModificationAction.setOnEditorActionListener { _, actionId, _ ->
            //상황 추가 입력창 키보드 엔터 오버라이딩 -> 텍스트뷰 추가
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addAction(viewModel.actionCount.value)
            }
            return@setOnEditorActionListener true
        }

        binding.etModificationGoal.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeGoalToggle()
                hideKeyboard(binding.root)
            }
            return@setOnEditorActionListener false
        }
    }

    private fun addAction(actionCount: Int?) {
        when (actionCount) {
            0 -> {
                with(binding) {
                    tvModificationActionFirst.text = viewModel.action.value
                    viewModel.action.value = BLANK
                    tvModificationActionFirst.visibility = View.VISIBLE
                    ivModificationActionFirstDelete.visibility = View.VISIBLE
                }
                viewModel.actionCount.value = 1
            }

            1 -> {
                with(binding) {
                    tvModificationActionSecond.text = viewModel.action.value
                    viewModel.action.value = BLANK
                    tvModificationActionSecond.visibility = View.VISIBLE
                    ivModificationActionSecondDelete.visibility = View.VISIBLE
                }
                viewModel.actionCount.value = 2
            }

            2 -> {
                with(binding) {
                    tvModificationActionThird.text = viewModel.action.value
                    viewModel.action.value = BLANK
                    tvModificationActionThird.visibility = View.VISIBLE
                    ivModificationActionThirdDelete.visibility = View.VISIBLE
                }
                viewModel.actionCount.value = 3
            }
        }
    }

    private fun setDeleteButtons() {
        binding.ivModificationActionFirstDelete.setOnClickListener {
            when (viewModel.actionCount.value) {
                1 -> {
                    hideActionFirst()
                    viewModel.actionCount.value = 0
                }

                2 -> {
                    binding.tvModificationActionFirst.text = binding.tvModificationActionSecond.text
                    hideActionSecond()
                    viewModel.actionCount.value = 1
                }

                3 -> {
                    binding.tvModificationActionFirst.text = binding.tvModificationActionSecond.text
                    binding.tvModificationActionSecond.text = binding.tvModificationActionThird.text
                    hideActionThird()
                    viewModel.actionCount.value = 2
                }
            }
        }
        binding.ivModificationActionSecondDelete.setOnClickListener {
            when (viewModel.actionCount.value) {
                2 -> {
                    hideActionSecond()
                    viewModel.actionCount.value = 1
                }

                3 -> {
                    binding.tvModificationActionSecond.text = binding.tvModificationActionThird.text
                    hideActionThird()
                    viewModel.actionCount.value = 2
                }
            }
        }
        binding.ivModificationActionThirdDelete.setOnClickListener {
            hideActionThird()
            viewModel.actionCount.value = 2
        }
    }

    private fun hideActionFirst() {
        binding.tvModificationActionFirst.text = BLANK
        binding.tvModificationActionFirst.visibility = View.GONE
        binding.ivModificationActionFirstDelete.visibility = View.GONE
    }

    private fun hideActionSecond() {
        binding.tvModificationActionSecond.text = BLANK
        binding.tvModificationActionSecond.visibility = View.GONE
        binding.ivModificationActionSecondDelete.visibility = View.GONE
    }

    private fun hideActionThird() {
        binding.tvModificationActionThird.text = BLANK
        binding.tvModificationActionThird.visibility = View.GONE
        binding.ivModificationActionThirdDelete.visibility = View.GONE
        binding.etModificationAction.visibility = View.VISIBLE
        binding.tvModificationActionTextCount.visibility = View.VISIBLE
        requestFocusWithShowingKeyboard(binding.etModificationAction)
    }

    private fun setFinishButton() {
        binding.ivModificationDelete.setOnClickListener { finish() }

    }

    private val setMissionName: (String) -> Unit = { missionName: String ->
        binding.etModificationMission.setText(missionName)
        binding.etModificationMission.requestFocus()
        binding.etModificationMission.setSelection(binding.etModificationMission.length())
        this.showKeyboard(binding.etModificationMission)
    }

    private fun setSituationRecommendations() {
        binding.layoutModificationSituationRecommend.addButtons(
            listOf("업무 시간 중", "작업 중", "기상 시간", "공부 시간", "취침 전", "출근 중"),
            binding.etModificationSituation
        )
    }

    private fun setModifyButton() {
        viewModel.isAbleToModify.observe(this) { isAbleToModify ->
            if (isAbleToModify == true) {
                binding.btnModificationModify.setTextColor(getColor(R.color.gray_1_2a2a2e))
                binding.btnModificationModify.setBackgroundResource(R.drawable.rectangle_green_2_radius_26)
            } else {
                binding.btnModificationModify.setTextColor(getColor(R.color.gray_3_5d5d6b))
                binding.btnModificationModify.setBackgroundResource(R.drawable.rectangle_gray_2_radius_26)
            }
        }
        binding.btnModificationModify.setOnClickListener {
            if (binding.btnModificationModify.currentTextColor == getColor(R.color.gray_1_2a2a2e)) {
                // 낫투두 추가
                viewModel.putModifyMission()
            }
        }
    }

    private fun observeGoal() {
        viewModel.goal.observe(this) { goal ->
            binding.tvModificationGoalTextCount.text = goal.length.toString() + maxTextSize
            if (goal.isNotBlank()) {
                binding.layoutModificationGoalClosed.background = AppCompatResources.getDrawable(
                    this, R.drawable.rectangle_solid_gray_1_radius_12
                )
                binding.ivModificationGoalCheck.visibility = View.VISIBLE
                binding.tvModificationGoalClosedChoice.visibility = View.GONE
                with(binding.tvModificationGoalInput) {
                    text = viewModel.goal.value
                    setTextColor(getColor(R.color.white))
                }

            } else {
                binding.layoutModificationGoalClosed.background = AppCompatResources.getDrawable(
                    this, R.drawable.rectangle_stroke_1_gray_3_radius_12
                )
                binding.ivModificationGoalCheck.visibility = View.GONE
                binding.tvModificationGoalClosedChoice.visibility = View.VISIBLE
                with(binding.tvModificationGoalInput) {
                    text = getText(R.string.addition_input)
                    setTextColor(getColor(R.color.gray_3_5d5d6b))
                }
            }
        }
    }

    private fun observeAction() {
        viewModel.action.observe(this) { action ->
            binding.tvModificationActionTextCount.text = action.length.toString() + maxTextSize
        }
    }

    private fun setActionBox(isActionFilled: Boolean) {
        if (isActionFilled) {
            binding.layoutModificationActionClosed.background = AppCompatResources.getDrawable(
                this, R.drawable.rectangle_solid_gray_1_radius_12
            )
            binding.ivModificationActionClosedCheck.visibility = View.VISIBLE
            binding.tvModificationActionClosedChoice.visibility = View.GONE
            binding.tvModificationActionClosedInput.setTextColor(getColor(R.color.white))
        } else {
            binding.layoutModificationActionClosed.background = AppCompatResources.getDrawable(
                this, R.drawable.rectangle_stroke_1_gray_3_radius_12
            )
            binding.ivModificationActionClosedCheck.visibility = View.GONE
            binding.tvModificationActionClosedChoice.visibility = View.VISIBLE
            binding.tvModificationActionClosedInput.setTextColor(getColor(R.color.gray_3_5d5d6b))
            binding.tvModificationActionClosedInput.text = getString(R.string.addition_input)
        }
    }

    private fun setActions() {
        viewModel.actionCount.observe(this) { actionCount ->
            when (actionCount) {
                0 -> {
                    setActionBox(isActionFilled = false)
                    with(binding) {
                        etModificationAction.visibility = View.VISIBLE
                        tvModificationActionTextCount.visibility = View.VISIBLE
                        requestFocusWithShowingKeyboard(binding.etModificationAction)
                    }
                    viewModel.actionList.value = listOf()
                }

                1 -> {
                    setActionBox(isActionFilled = true)
                    binding.tvModificationActionClosedInput.text =
                        binding.tvModificationActionFirst.text
                    with(binding) {
                        etModificationAction.visibility = View.VISIBLE
                        tvModificationActionTextCount.visibility = View.VISIBLE
                        requestFocusWithShowingKeyboard(binding.etModificationAction)
                        viewModel.actionList.value =
                            listOf(binding.tvModificationActionFirst.text.toString())
                    }
                }

                2 -> {
                    setActionBox(isActionFilled = true)
                    binding.tvModificationActionClosedInput.text =
                        "${binding.tvModificationActionFirst.text}\n${binding.tvModificationActionSecond.text}"
                    with(binding) {
                        etModificationAction.visibility = View.VISIBLE
                        tvModificationActionTextCount.visibility = View.VISIBLE
                        requestFocusWithShowingKeyboard(binding.etModificationAction)
                        viewModel.actionList.value = listOf(
                            binding.tvModificationActionFirst.text.toString(),
                            binding.tvModificationActionSecond.text.toString()
                        )
                    }
                }

                3 -> {
                    setActionBox(isActionFilled = true)
                    binding.tvModificationActionClosedInput.text =
                        "${binding.tvModificationActionFirst.text}\n${binding.tvModificationActionSecond.text}\n${binding.tvModificationActionThird.text}"
                    with(binding) {
                        etModificationAction.visibility = View.GONE
                        tvModificationActionTextCount.visibility = View.GONE
                        hideKeyboard(root)
                        viewModel.actionList.value = listOf(
                            binding.tvModificationActionFirst.text.toString(),
                            binding.tvModificationActionSecond.text.toString(),
                            binding.tvModificationActionThird.text.toString()
                        )
                    }
                }
            }
        }
    }

    private fun observeSituation() {
        viewModel.situation.observe(this) { situation ->
            binding.tvModificationSituationTextCount.text =
                situation.length.toString() + maxTextSize
            if (situation.isNotBlank()) {
                binding.layoutModificationSituationClosed.background =
                    AppCompatResources.getDrawable(
                        this, R.drawable.rectangle_solid_gray_1_radius_12
                    )
                binding.ivModificationSituationCheck.visibility = View.VISIBLE
                with(binding.tvModificationSituationInput) {
                    text = viewModel.situation.value
                    setTextColor(getColor(R.color.white))
                }

            } else {
                binding.layoutModificationSituationClosed.background =
                    AppCompatResources.getDrawable(
                        this, R.drawable.rectangle_stroke_1_gray_3_radius_12
                    )
                binding.ivModificationSituationCheck.visibility = View.GONE
                with(binding.tvModificationSituationInput) {
                    text = getText(R.string.addition_input)
                    setTextColor(getColor(R.color.gray_3_5d5d6b))
                }
            }
        }
    }

    private fun observeMission() {
        viewModel.mission.observe(this) { mission ->
            binding.tvModificationMissionTextCount.text = mission.length.toString() + maxTextSize
            if (mission.isNotBlank()) {
                binding.layoutModificationMissionClosed.background = AppCompatResources.getDrawable(
                    this, R.drawable.rectangle_solid_gray_1_radius_12
                )
                binding.ivModificationMissionClosedCheck.visibility = View.VISIBLE
                with(binding.tvModificationMissionClosedName) {
                    text = viewModel.mission.value
                    setTextColor(getColor(R.color.white))
                }
                binding.tvModificationMissionRvTitle.visibility = View.GONE
                binding.rvModificationMission.visibility = View.GONE

            } else {
                binding.layoutModificationMissionClosed.background = AppCompatResources.getDrawable(
                    this, R.drawable.rectangle_stroke_1_gray_3_radius_12
                )
                binding.ivModificationMissionClosedCheck.visibility = View.GONE
                with(binding.tvModificationMissionClosedName) {
                    text = getText(R.string.addition_input)
                    setTextColor(getColor(R.color.gray_3_5d5d6b))
                }
                binding.tvModificationMissionRvTitle.visibility = View.VISIBLE
                binding.rvModificationMission.visibility = View.VISIBLE
            }
        }
    }

    private fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modification)
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    private fun initToggles() {
        binding.layoutModificationMissionClosed.setOnClickListener {
            if (!isMissionToggleVisible) {
                openMissionToggle()
                closeSituationToggle()
                closeActionToggle()
                closeGoalToggle()
            } else {
                closeMissionToggle()
            }
        }

        binding.layoutModificationSituationClosed.setOnClickListener {
            if (!isSituationToggleVisible) {
                openSituationToggle()
                closeMissionToggle()
                closeActionToggle()
                closeGoalToggle()
            } else {
                closeSituationToggle()
            }
        }

        binding.layoutModificationActionClosed.setOnClickListener {
            if (!isActionToggleVisible) {
                openActionToggle()
                closeMissionToggle()
                closeSituationToggle()
                closeGoalToggle()
            } else {
                closeActionToggle()
            }
        }

        binding.layoutModificationGoalClosed.setOnClickListener {
            if (!isGoalToggleVisible) {
                openGoalToggle()
                closeMissionToggle()
                closeSituationToggle()
                closeActionToggle()
            } else {
                closeGoalToggle()
            }
        }

        binding.tvModificationActionComplete.setOnClickListener {
            closeActionToggle()
            hideKeyboard(binding.root)
        }
    }

    private fun closeGoalToggle() {
        binding.layoutModificationGoalClosed.visibility = View.VISIBLE
        binding.layoutModificationGoalOpened.visibility = View.GONE
        isGoalToggleVisible = false
    }

    private fun requestFocusWithShowingKeyboard(editText: EditText) {
        editText.requestFocus()
        editText.setSelection(editText.length())
        showKeyboard(editText)
    }

    private fun openGoalToggle() {
        binding.layoutModificationGoalClosed.visibility = View.GONE
        binding.layoutModificationGoalOpened.visibility = View.VISIBLE
        isGoalToggleVisible = true
        requestFocusWithShowingKeyboard(binding.etModificationGoal)
    }

    private fun openActionToggle() {
        binding.layoutModificationActionClosed.visibility = View.GONE
        binding.layoutModificationActionOpened.visibility = View.VISIBLE
        isActionToggleVisible = true
        requestFocusWithShowingKeyboard(binding.etModificationAction)
    }

    private fun closeActionToggle() {
        binding.layoutModificationActionClosed.visibility = View.VISIBLE
        binding.layoutModificationActionOpened.visibility = View.GONE
        isActionToggleVisible = false
    }

    private fun openSituationToggle() {
        binding.layoutModificationSituationClosed.visibility = View.GONE
        binding.layoutModificationSituationOpened.visibility = View.VISIBLE
        isSituationToggleVisible = true
        requestFocusWithShowingKeyboard(binding.etModificationSituation)
    }

    private fun closeSituationToggle() {
        binding.layoutModificationSituationClosed.visibility = View.VISIBLE
        binding.layoutModificationSituationOpened.visibility = View.GONE
        isSituationToggleVisible = false
    }

    private fun closeMissionToggle() {
        binding.layoutModificationMissionClosed.visibility = View.VISIBLE
        binding.layoutModificationMissionOpened.visibility = View.GONE
        isMissionToggleVisible = false
    }

    private fun openMissionToggle() {
        binding.layoutModificationMissionClosed.visibility = View.GONE
        binding.layoutModificationMissionOpened.visibility = View.VISIBLE
        isMissionToggleVisible = true
        requestFocusWithShowingKeyboard(binding.etModificationMission)
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
        binding.tvModificationMissionOpenedDesc.text = missionOpenedDesc

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
        binding.tvModificationSituationOpenedDesc.text = situationOpenedDesc

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
        binding.tvModificationActionOpenedDesc.text = actionOpenedDesc

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
        binding.tvModificationGoalOpenedDesc.text = goalOpenedDesc
    }

    private fun initRecyclerView(setMissionName: (String) -> Unit) {
        binding.rvModificationMission.adapter = MissionHistoryAdapter(this, setMissionName)
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

        data class NotTodoData(
            val date: String,
            val mission: String,
            val situation: String,
            val actions: List<String>?,
            val goal: String?,
            val missionId: Long,
        )
    }
}