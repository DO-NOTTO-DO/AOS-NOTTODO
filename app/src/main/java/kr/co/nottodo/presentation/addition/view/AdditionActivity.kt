package kr.co.nottodo.presentation.addition.view

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ActivityAdditionBinding
import kr.co.nottodo.presentation.addition.adapter.AdditionAdapter

class AdditionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdditionBinding
    private var isDateToggleVisible: Boolean = false
    private var isMissionToggleVisible: Boolean = false
    private var isSituationToggleVisible: Boolean = false
    private var isActionToggleVisible: Boolean = false
    private var isGoalToggleVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdditionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initSsbs()
        initToggles()
    }

    private fun initToggles() {
        binding.layoutAdditionMission.setOnClickListener {
            if (!isMissionToggleVisible) {
                binding.layoutAdditionMissionClosed.visibility = View.GONE
                binding.layoutAdditionMissionOpened.visibility = View.VISIBLE
                isMissionToggleVisible = true
            } else {
                binding.layoutAdditionMissionClosed.visibility = View.VISIBLE
                binding.layoutAdditionMissionOpened.visibility = View.GONE
                isMissionToggleVisible = false
            }
        }

        binding.layoutAdditionSituation.setOnClickListener {
            if (!isSituationToggleVisible) {
                binding.layoutAdditionSituationClosed.visibility = View.GONE
                binding.layoutAdditionSituationOpened.visibility = View.VISIBLE
                isSituationToggleVisible = true
            } else {
                binding.layoutAdditionSituationClosed.visibility = View.VISIBLE
                binding.layoutAdditionSituationOpened.visibility = View.GONE
                isSituationToggleVisible = false
            }
        }

        binding.layoutAdditionAction.setOnClickListener {
            if (!isActionToggleVisible) {
                binding.layoutAdditionActionClosed.visibility = View.GONE
                binding.layoutAdditionActionOpened.visibility = View.VISIBLE
                isActionToggleVisible = true
            } else {
                binding.layoutAdditionActionClosed.visibility = View.VISIBLE
                binding.layoutAdditionActionOpened.visibility = View.GONE
                isActionToggleVisible = false
            }
        }

        binding.layoutAdditionGoal.setOnClickListener {
            if (!isGoalToggleVisible) {
                binding.layoutAdditionGoalClosed.visibility = View.GONE
                binding.layoutAdditionGoalOpened.visibility = View.VISIBLE
                isGoalToggleVisible = true
            } else {
                binding.layoutAdditionGoalClosed.visibility = View.VISIBLE
                binding.layoutAdditionGoalOpened.visibility = View.GONE
                isGoalToggleVisible = false
            }
        }

        binding.layoutAdditionDate.setOnClickListener {
            if (!isDateToggleVisible) {
                binding.layoutAdditionDateClosed.visibility = View.GONE
                binding.layoutAdditionDateOpened.visibility = View.VISIBLE
                isDateToggleVisible = true
            } else {
                binding.layoutAdditionDateClosed.visibility = View.VISIBLE
                binding.layoutAdditionDateOpened.visibility = View.GONE
                isDateToggleVisible = false
            }
        }
    }

    private fun initSsbs() {
        val missionOpenedDesc = SpannableStringBuilder("어떤 낫투두를 설정해볼까요?")
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
            "어떤 상황에서\n낫투두를 실천하고 싶나요?"
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

        val actionOpenedDesc = SpannableStringBuilder("낫투두를 이루기 위해서\n어떤 행동이 필요한가요?")
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

        val goalOpenedDesc = SpannableStringBuilder("낫투두를 통해서\n어떤 목표를 이루려 하나요?")
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

    private fun initRecyclerView() {
        binding.rvAdditionMission.adapter = AdditionAdapter(this)
    }
}