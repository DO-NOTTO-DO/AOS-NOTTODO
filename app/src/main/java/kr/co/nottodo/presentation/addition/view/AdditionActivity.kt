package kr.co.nottodo.presentation.addition.view

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import kr.co.nottodo.R
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

        setAddButton()
        setFinishButton()
        setEnterKey()
    }

    private fun setEnterKey() {
        binding.etAdditionMission.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyCode == KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                closeMissionToggle()
                hideKeyboard(binding.root)
            }
            return@setOnKeyListener false
        }

        binding.etAdditionSituation.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyCode == KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                closeSituationToggle()
                hideKeyboard(binding.root)
            }
            return@setOnKeyListener false
        }

        binding.etAdditionAction.setOnKeyListener { _, keyCode, keyEvent ->
            //상황 추가 입력창 키보드 엔터 오버라이딩 -> 텍스트뷰 추가
            if (keyCode == KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {

            }
            return@setOnKeyListener false
        }

        binding.etAdditionGoal.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyCode == KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                closeGoalToggle()
                hideKeyboard(binding.root)
            }
            return@setOnKeyListener false
        }
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
        viewModel.isAbleToAdd.observe(this) {
            if (it == true) {
                binding.tvAdditionAdd.setTextColor(getColor(R.color.white))
            } else {
                binding.tvAdditionAdd.setTextColor(getColor(R.color.gray_3_5d5d6b))
            }
        }
        binding.tvAdditionAdd.setOnClickListener {
            if (binding.tvAdditionAdd.currentTextColor == getColor(R.color.white)) {
                // 낫투두 추가
                this.showToast("낫투두 추가 완료")
                finish()
            }
        }
    }

    private fun observeGoal() {
        viewModel.goal.observe(this) {
            binding.tvAdditionGoalTextCount.text = it.length.toString() + maxTextSize
            if (it.isNotBlank()) {
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
                    this, R.drawable.rectangle_stroke_gray3_1_radius_12
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
        viewModel.action.observe(this) {
            binding.tvAdditionActionTextCount.text = it.length.toString() + maxTextSize
            if (it.isNotBlank()) {
                binding.layoutAdditionActionClosed.background = AppCompatResources.getDrawable(
                    this, R.drawable.rectangle_solid_gray_1_radius_12
                )
                binding.ivAdditionActionClosedCheck.visibility = View.VISIBLE
                binding.tvAdditionActionClosedChoice.visibility = View.GONE
                with(binding.tvAdditionActionClosedInput) {
                    text = viewModel.action.value
                    setTextColor(getColor(R.color.white))
                }

            } else {
                binding.layoutAdditionActionClosed.background = AppCompatResources.getDrawable(
                    this, R.drawable.rectangle_stroke_gray3_1_radius_12
                )
                binding.ivAdditionActionClosedCheck.visibility = View.GONE
                binding.tvAdditionActionClosedChoice.visibility = View.VISIBLE
                with(binding.tvAdditionActionClosedInput) {
                    text = getText(R.string.addition_input)
                    setTextColor(getColor(R.color.gray_3_5d5d6b))
                }
            }
        }
    }

    private fun observeSituation() {
        viewModel.situation.observe(this) {
            binding.tvAdditionSituationTextCount.text = it.length.toString() + maxTextSize
            if (it.isNotBlank()) {
                binding.layoutAdditionSituationClosed.background = AppCompatResources.getDrawable(
                    this, R.drawable.rectangle_solid_gray_1_radius_12
                )
                binding.ivAdditionSituationCheck.visibility = View.VISIBLE
                with(binding.tvAdditionSituationInput) {
                    text = viewModel.situation.value
                    setTextColor(getColor(R.color.white))
                }

            } else {
                binding.layoutAdditionSituationClosed.background = AppCompatResources.getDrawable(
                    this, R.drawable.rectangle_stroke_gray3_1_radius_12
                )
                binding.ivAdditionSituationCheck.visibility = View.GONE
                with(binding.tvAdditionSituationInput) {
                    text = getText(R.string.addition_input)
                    setTextColor(getColor(R.color.gray_3_5d5d6b))
                }
            }
        }
    }

    private fun observeMission() {
        viewModel.mission.observe(this) {
            binding.tvAdditionMissionTextCount.text = it.length.toString() + maxTextSize
            if (it.isNotBlank()) {
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
                    this, R.drawable.rectangle_stroke_gray3_1_radius_12
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
            hideKeyboard(binding.root)
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