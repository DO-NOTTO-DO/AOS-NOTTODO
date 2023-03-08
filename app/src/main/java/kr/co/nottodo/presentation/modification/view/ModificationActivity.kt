package kr.co.nottodo.presentation.modification.view

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
import kr.co.nottodo.databinding.ActivityModificationBinding
import kr.co.nottodo.presentation.addition.adapter.MissionHistoryAdapter
import kr.co.nottodo.presentation.modification.viewmodel.ModificationViewModel
import kr.co.nottodo.util.addButtons
import kr.co.nottodo.util.hideKeyboard
import kr.co.nottodo.util.showKeyboard
import kr.co.nottodo.util.showToast

class ModificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModificationBinding
    private val viewModel by viewModels<ModificationViewModel>()
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
        binding.etModificationMission.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyCode == KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                closeMissionToggle()
                hideKeyboard(binding.root)
            }
            return@setOnKeyListener false
        }

        binding.etModificationSituation.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyCode == KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                closeSituationToggle()
                hideKeyboard(binding.root)
            }
            return@setOnKeyListener false
        }

        binding.etModificationAction.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyCode == KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                closeActionToggle()
                hideKeyboard(binding.root)
            }
            return@setOnKeyListener false
        }

        binding.etModificationGoal.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyCode == KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                closeGoalToggle()
                hideKeyboard(binding.root)
            }
            return@setOnKeyListener false
        }
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

    private fun setAddButton() {
        viewModel.isAbleToAdd.observe(this) {
            if (it == true) {
                binding.tvModificationAdd.setTextColor(getColor(R.color.white))
            } else {
                binding.tvModificationAdd.setTextColor(getColor(R.color.gray_3_5d5d6b))
            }
        }
        binding.tvModificationAdd.setOnClickListener {
            if (binding.tvModificationAdd.currentTextColor == getColor(R.color.white)) {
                // 낫투두 추가
                this.showToast("낫투두 수정 완료")
            }
        }
    }

    private fun observeGoal() {
        viewModel.goal.observe(this) {
            binding.tvModificationGoalTextCount.text = it.length.toString() + maxTextSize
            if (it.isNotBlank()) {
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
                    this, R.drawable.rectangle_stroke_gray3_1_radius_12
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
        viewModel.action.observe(this) {
            binding.tvModificationActionTextCount.text = it.length.toString() + maxTextSize
            if (it.isNotBlank()) {
                binding.layoutModificationActionClosed.background = AppCompatResources.getDrawable(
                    this, R.drawable.rectangle_solid_gray_1_radius_12
                )
                binding.ivModificationActionClosedCheck.visibility = View.VISIBLE
                binding.tvModificationActionClosedChoice.visibility = View.GONE
                with(binding.tvModificationActionClosedInput) {
                    text = viewModel.action.value
                    setTextColor(getColor(R.color.white))
                }

            } else {
                binding.layoutModificationActionClosed.background = AppCompatResources.getDrawable(
                    this, R.drawable.rectangle_stroke_gray3_1_radius_12
                )
                binding.ivModificationActionClosedCheck.visibility = View.GONE
                binding.tvModificationActionClosedChoice.visibility = View.VISIBLE
                with(binding.tvModificationActionClosedInput) {
                    text = getText(R.string.addition_input)
                    setTextColor(getColor(R.color.gray_3_5d5d6b))
                }
            }
        }
    }

    private fun observeSituation() {
        viewModel.situation.observe(this) {
            binding.tvModificationSituationTextCount.text = it.length.toString() + maxTextSize
            if (it.isNotBlank()) {
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
                        this, R.drawable.rectangle_stroke_gray3_1_radius_12
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
        viewModel.mission.observe(this) {
            binding.tvModificationMissionTextCount.text = it.length.toString() + maxTextSize
            if (it.isNotBlank()) {
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
                    this, R.drawable.rectangle_stroke_gray3_1_radius_12
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
                closeDateToggle()
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
                closeDateToggle()
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
                closeDateToggle()
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
                closeDateToggle()
                closeMissionToggle()
                closeSituationToggle()
                closeActionToggle()
            } else {
                closeGoalToggle()
            }
        }

        binding.layoutModificationDateClosed.setOnClickListener {
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

        binding.tvModificationDateOpenedComplete.setOnClickListener {
            closeDateToggle()
        }
    }

    private fun closeDateToggle() {
        binding.layoutModificationDateClosed.visibility = View.VISIBLE
        binding.layoutModificationDateOpened.visibility = View.GONE
        isDateToggleVisible = false
    }

    private fun openDateToggle() {
        binding.layoutModificationDateClosed.visibility = View.GONE
        binding.layoutModificationDateOpened.visibility = View.VISIBLE
        isDateToggleVisible = true
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

    companion object {
        const val maxTextSize = "/20"
        const val missionOpenedDesc = "어떤 낫투두를 설정해볼까요?"
        const val situationOpenedDesc = "어떤 상황에서\n낫투두를 실천하고 싶나요?"
        const val actionOpenedDesc = "낫투두를 이루기 위해서\n어떤 행동이 필요한가요?"
        const val goalOpenedDesc = "낫투두를 통해서\n어떤 목표를 이루려 하나요?"
    }
}