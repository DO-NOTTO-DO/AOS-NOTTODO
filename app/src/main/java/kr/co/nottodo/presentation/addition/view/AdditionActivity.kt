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
    lateinit var binding: ActivityAdditionBinding
    var isMissionToggleVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdditionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        initMissionSsb()

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
    }

    private fun initMissionSsb() {
        val ssb = SpannableStringBuilder("어떤 낫투두를 설정해볼까요?")
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.white)),
            0,
            2,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.green_1_98ffa9)),
            3,
            6,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.white)),
            6,
            15,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvAdditionMissionOpenedDesc.text = ssb
    }

    private fun initRecyclerView() {
        binding.rvAdditionMission.adapter = AdditionAdapter(this)
    }
}